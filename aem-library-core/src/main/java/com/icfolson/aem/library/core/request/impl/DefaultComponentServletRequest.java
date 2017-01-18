package com.icfolson.aem.library.core.request.impl;

import com.day.cq.wcm.api.WCMMode;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.api.page.PageManagerDecorator;
import com.icfolson.aem.library.api.request.ComponentServletRequest;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.sling.api.resource.ValueMap.EMPTY;

public final class DefaultComponentServletRequest implements ComponentServletRequest {

    private static final Function<RequestParameter, String> REQUEST_PARAMETER_TO_STRING = RequestParameter:: getString;

    private static final Function<RequestParameter[], List<String>> REQUEST_PARAMETERS_TO_LIST = parameters -> Arrays
        .stream(parameters)
        .map(RequestParameter:: getString)
        .collect(Collectors.toList());

    private final SlingHttpServletRequest slingRequest;

    private final SlingHttpServletResponse slingResponse;

    private final ComponentNode componentNode;

    private final Node currentNode;

    private final PageDecorator currentPage;

    private final PageManagerDecorator pageManager;

    private final ValueMap pageProperties;

    private final ValueMap properties;

    private final Resource resource;

    private final ResourceResolver resourceResolver;

    public DefaultComponentServletRequest(final SlingHttpServletRequest slingRequest,
        final SlingHttpServletResponse slingResponse) {
        this.slingRequest = checkNotNull(slingRequest);
        this.slingResponse = checkNotNull(slingResponse);

        resource = slingRequest.getResource();
        resourceResolver = resource.getResourceResolver();
        properties = resource.getValueMap();
        currentNode = resource.adaptTo(Node.class);
        componentNode = resource.adaptTo(ComponentNode.class);
        pageManager = resourceResolver.adaptTo(PageManagerDecorator.class);
        currentPage = pageManager.getContainingPage(resource);
        pageProperties = currentPage != null ? currentPage.getProperties() : EMPTY;
    }

    @Override
    public ComponentNode getComponentNode() {
        return componentNode;
    }

    @Override
    public Node getCurrentNode() {
        return currentNode;
    }

    @Override
    public PageDecorator getCurrentPage() {
        return currentPage;
    }

    @Override
    public PageManagerDecorator getPageManager() {
        return pageManager;
    }

    @Override
    public ValueMap getPageProperties() {
        return pageProperties;
    }

    @Override
    public ValueMap getProperties() {
        return properties;
    }

    @Override
    public Optional<String> getRequestParameter(final String parameterName) {
        checkNotNull(parameterName);

        return Optional.fromNullable(slingRequest.getRequestParameter(parameterName))
            .transform(REQUEST_PARAMETER_TO_STRING);
    }

    @Override
    public Optional<List<String>> getRequestParameters(final String parameterName) {
        checkNotNull(parameterName);

        return Optional.fromNullable(slingRequest.getRequestParameters(parameterName))
            .transform(REQUEST_PARAMETERS_TO_LIST);
    }

    @Override
    public String getRequestParameter(final String parameterName, final String defaultValue) {
        checkNotNull(parameterName);
        checkNotNull(defaultValue);

        final RequestParameter parameter = slingRequest.getRequestParameter(parameterName);

        return parameter == null ? defaultValue : parameter.getString();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    @Override
    public List<String> getSelectors() {
        return ImmutableList.copyOf(slingRequest.getRequestPathInfo().getSelectors());
    }

    @Override
    public Session getSession() {
        return resourceResolver.adaptTo(Session.class);
    }

    @Override
    public SlingHttpServletRequest getSlingRequest() {
        return slingRequest;
    }

    @Override
    public SlingHttpServletResponse getSlingResponse() {
        return slingResponse;
    }

    @Override
    public WCMMode getWCMMode() {
        return WCMMode.fromRequest(slingRequest);
    }
}
