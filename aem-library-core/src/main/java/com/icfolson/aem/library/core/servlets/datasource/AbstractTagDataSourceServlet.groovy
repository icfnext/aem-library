package com.icfolson.aem.library.core.servlets.datasource

import com.day.cq.commons.Filter
import com.day.cq.tagging.Tag
import com.day.cq.tagging.TagManager
import com.icfolson.aem.library.api.request.ComponentServletRequest
import com.icfolson.aem.library.core.servlets.optionsprovider.Option
import org.apache.commons.lang3.StringUtils

/**
 * Extends the AbstractOptionsDataSourceServlet and solely focuses on building options from tags within the repository.
 * A basic extension is to provide just the namespace of the tags and the servlet will build a list of options
 * from all direct descendants of that namespace tag. Extending classes may optionally provide a more granular tag path
 * and a custom filter.
 */
abstract class AbstractTagDataSourceServlet extends AbstractOptionsDataSourceServlet {

    public static final Filter<Tag> TAG_FILTER_INCLUDE_ALL = new Filter<Tag>() {
        @Override
        boolean includes(Tag tag) {
            true
        }
    }

    @Override
    protected List<Option> getOptions(final ComponentServletRequest request) {
        def tagManager = request.resourceResolver.adaptTo(TagManager)

        def containerTagPath = tagManager.resolve(namespace + containerTag)

        def options = []

        if (containerTagPath) {
            options = containerTagPath.listChildren(tagFilter).collect { tag ->
                new Option(tag.tagID, tag.title)
            }
        }

        options
    }

    /**
     * The string value of the tag namespace with a colon at the end, e.g. "colors:". A namespace tag is any tag that is
     * a direct child of the taxonomy root node (/etc/tags).
     *
     * @return the node name of the namespace tag.
     */
    protected abstract String getNamespace()

    /**
     * Override this method to provide the path of the parent tag containing all child tags to be returned by the
     * servlet. Defaults to an empty string if not overridden, resulting in all child tags directly under the namespace
     * tag being returned.
     *
     * @return the path of the containing tag.
     */
    protected String getContainerTag() {
        StringUtils.EMPTY
    }

    /**
     * Override this method to provide a filter for the list of tags returned by the servlet. Defaults to an all
     * inclusive filter if not overridden.
     *
     * @return the tag filter to use when building the list of tags returned by the servlet.
     */
    protected Filter<Tag> getTagFilter() {
        TAG_FILTER_INCLUDE_ALL
    }
}
