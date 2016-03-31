package com.icfolson.aem.library.api.page;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.google.common.base.Predicate;
import org.apache.sling.api.resource.Resource;

import javax.jcr.query.Query;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * Decorates the CQ <code>PageManager</code> interface with additional methods for finding pages using queries, tags,
 * and template paths.
 */
public interface PageManagerDecorator extends PageManager {

    /**
     * Copies the given page to the new destination and automatically saves the modifications
     *
     * @param page the page to copy
     * @param destination the destination
     * @param beforeName the name of the next page. if <code>null</code> the page is ordered at the end.
     * @param shallow if <code>true</code> a non-recursive copy is performed.
     * @param resolveConflict if <code>true</code> resolves name conflict if destination already exists.
     * @return the copied page
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator copy(Page page, String destination, String beforeName, boolean shallow, boolean resolveConflict)
        throws WCMException;

    /**
     * Copies the given page to the new destination
     *
     * @param page the page to copy
     * @param destination the destination
     * @param beforeName the name of the next page. if <code>null</code> the page is ordered at the end.
     * @param shallow if <code>true</code> a non-recursive copy is performed.
     * @param resolveConflict if <code>true</code> resolves name conflict if destination already exists.
     * @param autoSave if <code>true</code> saves the modifications.
     * @return the copied page
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator copy(Page page, String destination, String beforeName, boolean shallow, boolean resolveConflict,
        boolean autoSave) throws WCMException;

    /**
     * Creates a new page at the given path using the provided template as content template. If a no pageName is given
     * but a title, then the title is used as hint for the name of the new page.
     *
     * @param parentPath the path of the parent page
     * @param pageName the name of the new page
     * @param template the template for the new page
     * @param title the title of the new page
     * @return the page that was created
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator create(String parentPath, String pageName, String template, String title) throws WCMException;

    /**
     * Creates a new page at the given path using the provided template as content template. If a no pageName is given
     * but a title, then the title is used as hint for the name of the new page.
     *
     * @param parentPath the path of the parent page
     * @param pageName the name of the new page
     * @param template the template for the new page
     * @param title the title of the new page
     * @param autoSave if <code>true</code> saves the modifications.
     * @return the page that was created
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator create(String parentPath, String pageName, String template, String title, boolean autoSave)
        throws WCMException;

    /**
     * Find all descendant pages of the given path containing the specified tags.
     *
     * @param rootPath root path
     * @param tagIds set of tag IDs
     * @param matchOne if true, 'OR' the specified tag IDs, 'AND' otherwise
     * @return pages containing specified tags
     */
    List<PageDecorator> findPages(String rootPath, Collection<String> tagIds, boolean matchOne);

    /**
     * Find all descendant pages of the given path matching the template path.
     *
     * @param rootPath root path
     * @param templatePath template path
     * @return pages matching specified template
     */
    List<PageDecorator> findPages(String rootPath, String templatePath);

    /**
     * Find all descendant pages of the given path that match the predicate.
     *
     * @param rootPath root path
     * @param predicate predicate to determine if a page should be included in the result list
     * @return pages matching filter criteria
     */
    List<PageDecorator> findPages(String rootPath, Predicate<PageDecorator> predicate);

    /**
     * Returns the page that contains this resource. If the resource is a page the resource is returned. Otherwise it
     * walks up the parent resources until a page is found.
     *
     * @param resource resource to find the page for
     * @return page or <code>null</code> if not found.
     */
    @Override
    PageDecorator getContainingPage(Resource resource);

    /**
     * Returns the page that contains the resource at the given path. If the path addresses a page, that page is
     * returned. Otherwise it walks up the parent resources until a page is found.
     *
     * @param path path to find the page for
     * @return page or <code>null</code> if not found.
     */
    @Override
    PageDecorator getContainingPage(String path);

    /**
     * Decorate the given page.
     *
     * @param page non-null CQ page
     * @return decorated page
     */
    PageDecorator getPage(Page page);

    /**
     * Convenience method that returns the page at the given path. If the resource at that path does not exist or is not
     * adaptable to Page, <code>null</code> is returned.
     *
     * @param path path of the page
     * @return page or <code>null</code>
     */
    @Override
    PageDecorator getPage(String path);

    /**
     * Moves the given page to the new destination. If source and destination are equals the page is just ordered.
     *
     * @param page the page to move
     * @param destination the path of the new destination
     * @param beforeName the name of the next page. if <code>null</code> the page is ordered at the end.
     * @param shallow if <code>true</code> only the page content is moved
     * @param resolveConflict if <code>true</code> resolves name conflict if destination already exists.
     * @param adjustRefs list of paths to pages that refer to the moved one. those references will be adjusted.
     * @return the new page at the new location
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator move(Page page, String destination, String beforeName, boolean shallow, boolean resolveConflict,
        String[] adjustRefs) throws WCMException;

    /**
     * Restore a revision of some page.
     *
     * @param path path to the page or to the parent page
     * @param revisionId revision id to restore
     * @return the page that was restored
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator restore(String path, String revisionId) throws WCMException;

    /**
     * Restore a tree. Restores this page and its subtree to the versions of the given date.
     *
     * @param path path to page
     * @param date calendar date to restore to
     * @return the page that was restored
     * @throws WCMException if an error during this operation occurs.
     */
    @Override
    PageDecorator restoreTree(String path, Calendar date) throws WCMException;

    /**
     * Search for pages using a query.
     *
     * @param query JCR query
     * @return list of pages for the query result
     */
    List<PageDecorator> search(Query query);

    /**
     * Search for pages using a query with the given result limit.
     *
     * @param query JCR query
     * @param limit result limit
     * @return list of pages for the query result
     */
    List<PageDecorator> search(Query query, int limit);
}
