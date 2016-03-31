package com.icfolson.aem.library.core.tags

import com.google.common.collect.Lists
import groovy.util.logging.Slf4j

import static javax.servlet.jsp.PageContext.REQUEST_SCOPE

@Slf4j("LOG")
final class IncludeDeferredClientLibTag extends AbstractDeferredClientLibTag {

    @Override
    int doEndTag() {
        def categories = Lists.newArrayList(requestCategories)

        categories.addAll(this.categories)

        LOG.debug("client libraries for request = {}", categories)

        pageContext.setAttribute(ATTR_CATEGORIES, categories.join(","), REQUEST_SCOPE)

        EVAL_PAGE
    }
}
