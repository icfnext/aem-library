package com.icfolson.aem.library.api.page;

import com.day.cq.commons.Filter;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.Template;
import com.day.cq.wcm.api.WCMException;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.icfolson.aem.library.api.*;
import com.icfolson.aem.library.api.link.ImageLink;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.NavigationLink;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;
import com.icfolson.aem.library.api.node.ComponentNode;
import com.icfolson.aem.library.api.page.enums.TitleType;
import org.apache.sling.api.adapter.Adaptable;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Decorates the CQ <code>Page</code> interface with additional convenience
 * methods for traversing the content hierarchy and getters for AEM Library
 * classes.
 */
public interface PageDecorator extends Accessible, Inheritable, Linkable, Traversable<PageDecorator>, Adaptable,
        Labelable, Replicable {
    /**
     * Get the underlying WCM page.
     *
     * @return page
     */
    Page getPage();

    /**
     * Get the child pages of the current page.
     *
     * @return all child pages of current page or empty list if none exist
     */
    List<PageDecorator> getChildren();

    /**
     * Get the child pages of the current page, excluding children that are not
     * "displayable" (i.e. hidden in nav).
     *
     * @param displayableOnly if true, only pages that are not hidden in
     * navigation will be returned
     * @return child pages of current page or empty list if none exist
     */
    List<PageDecorator> getChildren(boolean displayableOnly);

    /**
     * Get the child pages of the current page filtered using the given
     * predicate.
     *
     * @param predicate predicate to filter pages
     * @return filtered list of child pages or empty list if none exist
     */
    List<PageDecorator> getChildren(Predicate<PageDecorator> predicate);

    /**
     * List children of the current page.
     *
     * @return iterator of child pages
     */
    Iterator<PageDecorator> listChildPages();

    /**
     * List child pages of the current page filtered using the given predicate.
     *
     * @param predicate predicate to filter pages
     * @return filtered iterator of child pages
     */
    Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate);

    /**
     * List child pages of the current page filtered using the given predicate.
     *
     * @param predicate predicate to filter pages
     * @param deep if true, recursively iterate over all descendant pages
     * @return filtered iterator of child pages
     */
    Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate, boolean deep);

    /**
     * Get the child page of the current page by name.
     *
     * @param name name of the child
     * @return page or absent <code>Optional</code>
     */
    Optional<PageDecorator> getChild(String name);

    /**
     * Get the component node for the "jcr:content" node for this page. If the
     * page does not have a content node, an "absent" Optional is returned.
     *
     * @return optional component node for page content
     */
    Optional<ComponentNode> getComponentNode();

    /**
     * Get the component node for the node at the given path relative to the
     * "jcr:content" node for this page. If the node does not exist, an "absent"
     * Optional is returned.
     *
     * @param relativePath relative path to resource
     * @return optional component node for resource relative to page content
     */
    Optional<ComponentNode> getComponentNode(String relativePath);

    /**
     * Get a link with a specified title type for this item.
     *
     * @param titleType type of title to set on link
     * @return link
     */
    Link getLink(TitleType titleType);

    /**
     * Get a link with a specified title type for this item.
     *
     * @param titleType type of title to set on link
     * @param mapped if true, the <code>Link</code> path will be routed through
     * the resource resolver to determine the mapped path (e.g.
     * without leading "/content").
     * @return link
     */
    Link getLink(TitleType titleType, boolean mapped);

    /**
     * Get a link builder for the current resource path.
     *
     * @param titleType type of title to set on builder
     * @return builder instance for this item
     */
    LinkBuilder getLinkBuilder(TitleType titleType);

    /**
     * Get a link builder for the current resource path.
     *
     * @param titleType type of title to set on builder
     * @param mapped if true, the <code>Link</code> path will be routed through
     * the resource resolver to determine the mapped path (e.g.
     * without leading "/content").
     * @return builder instance for this item
     */
    LinkBuilder getLinkBuilder(TitleType titleType, boolean mapped);

    /**
     * Get a navigation link for this page. The returned link will use the
     * navigation title as the link title, defaulting to the JCR title if it
     * does not exist.
     *
     * @return navigation link
     */
    NavigationLink getNavigationLink();

    /**
     * Get a navigation link for this page containing an active state. The
     * returned link will use the navigation title as the link title, defaulting
     * to the JCR title if it does not exist.
     *
     * @param isActive active state to be set on returned link
     * @return navigation link
     */
    NavigationLink getNavigationLink(boolean isActive);

    /**
     * Get a navigation link for this page containing an active state. The
     * returned link will use the navigation title as the link title, defaulting
     * to the JCR title if it does not exist.
     *
     * @param isActive active state to be set on returned link
     * @param mapped if true, the <code>NavigationLink</code> path will be
     * routed through the resource resolver to determine the mapped
     * path (e.g. without leading "/content").
     * @return navigation link
     */
    NavigationLink getNavigationLink(boolean isActive, boolean mapped);

    /**
     * Get a link for this page with an attached image source.
     *
     * @param imageSource image source to set on the returned image link
     * @return image link with the provided image source
     */
    ImageLink getImageLink(String imageSource);

    boolean isHasImage();

    boolean isHasImage(String name);

    /**
     * Get the template path for this page. This method is preferred over
     * getTemplate().getPath(), which is dependent on access to /apps and will
     * therefore fail in publish mode.
     *
     * @return value of cq:template property or empty string if none exists
     */
    String getTemplatePath();

    /**
     * Get the title with the given type for this page. If the title value is
     * empty or non-existent, an absent <code>Optional</code> is returned.
     *
     * @param titleType type of title to retrieve
     * @return title value or absent <code>Optional</code>
     */
    Optional<String> getTitle(TitleType titleType);

    // overrides for returning decorated types

    /**
     * Returns the absolute parent page. If no page exists at that level,
     * <code>null</code> is returned.
     * <p>
     * Example (this path == /content/geometrixx/en/products)
     * <p>
     * <pre>
     * | level | returned                        |
     * |     0 | /content                        |
     * |     1 | /content/geometrixx             |
     * |     2 | /content/geometrixx/en          |
     * |     3 | /content/geometrixx/en/products |
     * |     4 | null                            |
     * </pre>
     *
     * @param level hierarchy level of the parent page to retrieve
     * @return the respective parent page or <code>null</code>
     */

    PageDecorator getAbsoluteParent(int level);

    /**
     * Convenience method that returns the manager of this page.
     *
     * @return the page manager
     */

    PageManagerDecorator getPageManager();

    /**
     * Returns the parent page if it's resource adapts to page.
     *
     * @return the parent page or <code>null</code>
     */

    PageDecorator getParent();

    /**
     * Returns the relative parent page. If no page exists at that level,
     * <code>null</code> is returned.
     * <p>
     * Example (this path == /content/geometrixx/en/products)
     * <p>
     * <pre>
     * | level | returned                        |
     * |     0 | /content/geometrixx/en/products |
     * |     1 | /content/geometrixx/en          |
     * |     2 | /content/geometrixx             |
     * |     3 | /content                        |
     * |     4 | null                            |
     * </pre>
     *
     * @param level hierarchy level of the parent page to retrieve
     * @return the respective parent page or <code>null</code>
     */

    PageDecorator getParent(int level);

    Optional<String> getImageSource();

    Optional<String> getImageSource(int width);

    Optional<String> getImageSource(String name);

    Optional<String> getImageSource(String name, int width);

    /*  Pass through methods from com.day.cq.wcm.api.Page for backwards compatibility */
    String getPath();

    Resource getContentResource();

    Resource getContentResource(String var1);

    Iterator<Page> listChildren();

    Iterator<Page> listChildren(Filter<Page> var1);

    Iterator<Page> listChildren(Filter<Page> var1, boolean var2);

    boolean hasChild(String var1);

    int getDepth();

    ValueMap getProperties();

    ValueMap getProperties(String var1);

    String getName();

    String getTitle();

    String getPageTitle();

    String getNavigationTitle();

    boolean isHideInNav();

    boolean hasContent();

    boolean isValid();

    long timeUntilValid();

    Calendar getOnTime();

    Calendar getOffTime();

    Calendar getDeleted();

    String getDeletedBy();

    String getLastModifiedBy();

    Calendar getLastModified();

    String getVanityUrl();

    Tag[] getTags();

    boolean isLocked();

    String getLockOwner();

    boolean canUnlock();

    Template getTemplate();

    Locale getLanguage(boolean var1);

    Locale getLanguage();

    void lock() throws WCMException;

    void unlock() throws WCMException;
}