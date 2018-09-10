package com.icfolson.aem.library.core.link.builders.factory;

import com.day.cq.wcm.api.Page;
import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;
import com.icfolson.aem.library.api.page.enums.TitleType;
import com.icfolson.aem.library.core.constants.PropertyConstants;
import com.icfolson.aem.library.core.link.builders.impl.DefaultLinkBuilder;
import org.apache.sling.api.resource.Resource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory for acquiring <code>LinkBuilder</code> instances.
 */
public final class LinkBuilderFactory {

    /**
     * Get a builder instance for an existing <code>Link</code>.  The path, extension, title, and target are copied from
     * the link argument.
     *
     * @param link existing link
     * @return builder
     */
    public static LinkBuilder forLink(final Link link) {
        checkNotNull(link);

        return new DefaultLinkBuilder(link.getPath(), null)
            .setExtension(link.getExtension())
            .setTitle(link.getTitle())
            .setTarget(link.getTarget());
    }

    /**
     * Get a builder instance for a page.  If the page contains a redirect, the builder will contain the redirect target
     * rather than the page path.
     *
     * @param page page
     * @return builder containing the path of the given page
     */
    public static LinkBuilder forPage(final Page page) {
        return forPage(page, false, TitleType.TITLE);
    }

    /**
     * Get a builder instance for a page using the specified title type on the returned builder.
     *
     * @param page page
     * @param titleType type of page title to set on the builder
     * @return builder containing the path and title of the given page
     */
    public static LinkBuilder forPage(final Page page, final TitleType titleType) {
        return forPage(page, false, titleType);
    }

    /**
     * Get a builder instance for a page.  If the page contains a redirect, the builder will contain the redirect target
     * rather than the page path.
     *
     * @param page page
     * @param mapped if true, link path will be mapped through resource resolver
     * @return builder containing the mapped path of the given page
     */
    public static LinkBuilder forPage(final Page page, final boolean mapped) {
        return forPage(page, mapped, TitleType.TITLE);
    }

    /**
     * Get a builder instance for a page using the specified title type on the returned builder.
     *
     * @param page page
     * @param mapped if true, link path will be mapped through resource resolver
     * @param titleType type of page title to set on the builder
     * @return builder containing the path and title of the given page
     */
    public static LinkBuilder forPage(final Page page, final boolean mapped, final TitleType titleType) {
        final String title = checkNotNull(page).getProperties().get(titleType.getPropertyName(), page.getTitle());

        final String redirect = page.getProperties().get(PropertyConstants.REDIRECT_TARGET, "");
        final String path = redirect.isEmpty() ? page.getPath() : redirect;

        final Resource resource = page.getContentResource();

        return new DefaultLinkBuilder(path, (mapped && resource != null) ? resource.getResourceResolver() : null)
            .setTitle(title);
    }

    /**
     * Get a builder instance for a path.
     *
     * @param path content or external path
     * @return builder containing the given path
     */
    public static LinkBuilder forPath(final String path) {
        return new DefaultLinkBuilder(checkNotNull(path), null);
    }

    /**
     * Get a builder instance for a resource.
     *
     * @param resource resource
     * @return builder containing the path of the given resource
     */
    public static LinkBuilder forResource(final Resource resource) {
        return forResource(resource, false);
    }

    /**
     * Get a builder instance for a resource using the mapped path on the returned builder.
     *
     * @param resource resource
     * @param mapped if true, link path will be mapped through resource resolver
     * @return builder containing the mapped path of the given resource
     */
    public static LinkBuilder forResource(final Resource resource, final boolean mapped) {
        checkNotNull(resource);

        return new DefaultLinkBuilder(resource.getPath(), mapped ? resource.getResourceResolver() : null);
    }

    private LinkBuilderFactory() {

    }
}
