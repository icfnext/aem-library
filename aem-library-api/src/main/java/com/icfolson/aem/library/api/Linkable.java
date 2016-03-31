package com.icfolson.aem.library.api;

import com.icfolson.aem.library.api.link.Link;
import com.icfolson.aem.library.api.link.builders.LinkBuilder;

/**
 * Definition for "linkable" items, such as pages and components (i.e. path-addressable resources).
 */
public interface Linkable {

    /**
     * Get the URL for this item.
     *
     * @return href
     */
    String getHref();

    /**
     * Get the mapped URL for this item.
     *
     * @param mapped if true, the path will be routed through the resource resolver to determine the mapped path (e.g.
     * without leading "/content").
     * @return mapped href
     */
    String getHref(boolean mapped);

    /**
     * Get a link for this item.
     *
     * @return link
     */
    Link getLink();

    /**
     * Get a link for this item.
     *
     * @param mapped if true, the <code>Link</code> path will be routed through the resource resolver to determine the
     * mapped path (e.g. without leading "/content").
     * @return mapped link
     */
    Link getLink(boolean mapped);

    /**
     * Get a link builder for the current resource path.
     *
     * @return builder instance for this item
     */
    LinkBuilder getLinkBuilder();

    /**
     * Get a mapped link builder for the current resource path.
     *
     * @param mapped if true, the <code>LinkBuilder</code> for this resource will be routed through the resource
     * resolver to determine the mapped path (e.g. without leading "/content").
     * @return builder instance for this item containing the mapped link
     */
    LinkBuilder getLinkBuilder(boolean mapped);

}
