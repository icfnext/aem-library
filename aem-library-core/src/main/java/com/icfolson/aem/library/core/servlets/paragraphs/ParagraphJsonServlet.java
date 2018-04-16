package com.icfolson.aem.library.core.servlets.paragraphs;

import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.api.request.ComponentServletRequest;
import com.icfolson.aem.library.core.servlets.AbstractComponentServlet;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.WCMMode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.wrappers.SlingHttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Set;

import static com.icfolson.aem.library.core.constants.PathConstants.EXTENSION_HTML;
import static com.icfolson.aem.library.core.constants.PathConstants.EXTENSION_JSON;

/**
 * Retrieves HTML for all contained components on a Page. This differs from the
 * OOB Paragraph servlet, com.day.cq.wcm.foundation.ParagraphServlet, in that
 * the OOB servlet only pulls content from top level containers while this
 * servlet will pull content from any container on the Page regardless of depth
 * of nesting. Concretely, if your page has nested paragraph systems, the OOB
 * paragraph servlet will not recognize and collect components within the nested
 * paragraph systems while this servlet will.
 * <p>
 * This Servlet is suitable for overriding the OOB behavior of xtypes such as
 * paragraphreference, causing it to pull all components on a page as opposed to
 * the top level components.
 */
@SlingServlet(resourceTypes = { NameConstants.NT_PAGE }, selectors = { "ctparagraphs" },
	extensions = { EXTENSION_JSON }, methods = { "GET" })
public final class ParagraphJsonServlet extends AbstractComponentServlet {

	private static final long serialVersionUID = 1L;

	private static final String CONTAINER_COMPONENTS_FROM_LIBS_XPATH_QUERY =
		"/jcr:root/libs//element(*,cq:Component)[@cq:isContainer='true']";

	private static final String CONTAINER_COMPONENTS_FROM_APPS_XPATH_QUERY =
		"/jcr:root/apps//element(*,cq:Component)[@cq:isContainer='true']";

	private static final String LIBS_PATH_STRING = "/libs/";

	private static final String APPS_PATH_STRING = "/apps/";

	private static final Logger LOG = LoggerFactory.getLogger(ParagraphJsonServlet.class);

	@Override
	public void processGet(final ComponentServletRequest request) throws ServletException, IOException {
		final SlingHttpServletRequest slingRequest = request.getSlingRequest();
		final SlingHttpServletResponse slingResponse = request.getSlingResponse();

		WCMMode.DISABLED.toRequest(slingRequest);

		try {
			final List<Paragraph> paragraphs = getParagraphs(request);

			if (paragraphs != null) {
				LOG.debug("{} paragraphs found on page", paragraphs.size());

				writeJsonResponse(slingResponse, ImmutableMap.of("paragraphs", paragraphs));
			} else {
				LOG.info("null returned, indicating a lack of page or a lack of content");

				slingResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (RepositoryException e) {
			LOG.error("error requesting paragraph HTML for contained components", e);

			slingResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void processPost(final ComponentServletRequest request) throws ServletException, IOException {
		// nothing to do
	}

	/**
	 * @param request component servlet request
	 * @return A List of Paragraphs representing all components which exist
	 *         within containers under a given Page
	 * @throws RepositoryException
	 * @throws ServletException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private List<Paragraph> getParagraphs(final ComponentServletRequest request) throws RepositoryException,
		ServletException, IOException {
		// Request the current page
		final PageDecorator currentPage = request.getCurrentPage();

		if (currentPage == null) {
			LOG.info("The request is not within the context of a Page");

			return null;
		}

		// Request the page's content node
		final Resource pageContentResource = currentPage.getContentResource();

		if (pageContentResource == null) {
			LOG.info("The requested resource does not have a child content resource");

			return null;
		}

		// Get a handle to the query manager
		final Session session = request.getResourceResolver().adaptTo(Session.class);

		final QueryManager queryManager = session.getWorkspace().getQueryManager();

		// Find all container components which are children of the requested
		// resources content node
		final Set<String> containerComponentResourceTypes = getContainerComponents(queryManager);

		/*
		 * Construct a query which will request all resources under the current
		 * page's content node which are of a type indicated to be a container
		 * via the cq:isContainer property.
		 */
		final List<String> resourceTypeAttributeQueryStrings =
			Lists.newArrayListWithExpectedSize(containerComponentResourceTypes.size());

		for (final String curContainerResourceType : containerComponentResourceTypes) {
			resourceTypeAttributeQueryStrings.add("@sling:resourceType='" + curContainerResourceType + "'");
		}

		final String resourceTypeAttributeQueryString = StringUtils.join(resourceTypeAttributeQueryStrings, " or ");

		final String resourceQueryString =
			"/jcr:root" + pageContentResource.getPath() + "//element(*,nt:unstructured)["
				+ resourceTypeAttributeQueryString + "]";

		LOG.debug("resource query string = {}", resourceQueryString);

		// Execute the query
		final Query resourceQuery = queryManager.createQuery(resourceQueryString, Query.XPATH);

		final QueryResult resourceQueryResult = resourceQuery.execute();
		final NodeIterator resourceQueryResultIterator = resourceQueryResult.getNodes();

		final List<Paragraph> paragraphs = Lists.newArrayList();

		/*
		 * Go through the direct children of each container resource, adding
		 * them to the final list of Paragraphs
		 */
		while (resourceQueryResultIterator.hasNext()) {
			paragraphs.addAll(getChildParagraphs(request, resourceQueryResultIterator.nextNode().getPath(),
				containerComponentResourceTypes));
		}

		// Return the list of paragraphs
		return paragraphs;
	}

	/**
	 * @param request
	 * @param parentPath
	 * @param containerResourceTypes
	 * @return A list of Paragraphs representing the non-container resources
	 *         which are direct children of the resource indicated by the
	 *         parentPath
	 * @throws ServletException
	 * @throws IOException
	 */
	private List<Paragraph> getChildParagraphs(final ComponentServletRequest request, final String parentPath,
		final Set<String> containerResourceTypes) throws ServletException, IOException {
		final List<Paragraph> paragraphs = Lists.newArrayList();

		final Resource parentResource = request.getResourceResolver().getResource(parentPath);

		if (parentResource != null) {
			for (final Resource resource : parentResource.getChildren()) {
				if (!containerResourceTypes.contains(resource.getResourceType())) {
					paragraphs.add(new Paragraph(resource.getPath(), renderResourceHtml(resource,
						request.getSlingRequest(), request.getSlingResponse())));
				}
			}
		}

		return paragraphs;
	}

	@SuppressWarnings("deprecation")
	private Set<String> getContainerComponents(final QueryManager queryManager) throws RepositoryException {
		final Set<String> containerComponentSet = Sets.newHashSet();

		final Query containerComponentsFromLibsQuery =
			queryManager.createQuery(CONTAINER_COMPONENTS_FROM_LIBS_XPATH_QUERY, Query.XPATH);
		final Query containerComponentsFromAppsQuery =
			queryManager.createQuery(CONTAINER_COMPONENTS_FROM_APPS_XPATH_QUERY, Query.XPATH);

		final QueryResult containerComponentsFromLibsQueryResult = containerComponentsFromLibsQuery.execute();
		final QueryResult containerComponentsFromAppsQueryResult = containerComponentsFromAppsQuery.execute();

		final NodeIterator containerComponentsFromLibsQueryResultIterator =
			containerComponentsFromLibsQueryResult.getNodes();
		final NodeIterator containerComponentsFromAppsQueryResultIterator =
			containerComponentsFromAppsQueryResult.getNodes();

		LOG.debug("query execution complete");

		while (containerComponentsFromLibsQueryResultIterator.hasNext()) {
			final Node curContainerComponentNode = containerComponentsFromLibsQueryResultIterator.nextNode();

			final String curNodePath = curContainerComponentNode.getPath();

			LOG.debug("adding {} from libs as a container resource type", curNodePath);

			if (curNodePath.startsWith(LIBS_PATH_STRING)) {
				containerComponentSet.add(curNodePath.substring(LIBS_PATH_STRING.length()));
			} else {
				containerComponentSet.add(curContainerComponentNode.getPath());
			}
		}

		while (containerComponentsFromAppsQueryResultIterator.hasNext()) {
			final Node curContainerComponentNode = containerComponentsFromAppsQueryResultIterator.nextNode();

			final String curNodePath = curContainerComponentNode.getPath();

			LOG.debug("adding {} from apps as a container resource type", curNodePath);

			if (curNodePath.startsWith(APPS_PATH_STRING)) {
				containerComponentSet.add(curNodePath.substring(APPS_PATH_STRING.length()));
			} else {
				containerComponentSet.add(curContainerComponentNode.getPath());
			}
		}

		return containerComponentSet;
	}

	private String renderResourceHtml(final Resource resource, final SlingHttpServletRequest request,
		final SlingHttpServletResponse response) throws ServletException, IOException {
		final Writer outputBuffer = new StringWriter();

		final ServletOutputStream outputStream = new ServletOutputStream() {
			@Override
			public void write(final int b) throws IOException {
				outputBuffer.append((char) b);
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public void setWriteListener(WriteListener paramWriteListener) {

			}
		};

		final SlingHttpServletResponseWrapper responseWrapper = new SlingHttpServletResponseWrapper(response) {
			@Override
			public ServletOutputStream getOutputStream() {
				return outputStream;
			}

			@Override
			public PrintWriter getWriter() throws IOException {
				return new PrintWriter(outputBuffer);
			}
		};

		final RequestDispatcher requestDispatcher =
			request.getRequestDispatcher(resource.getPath() + "." + EXTENSION_HTML);

		requestDispatcher.include(request, responseWrapper);

		return outputBuffer.toString();
	}
}
