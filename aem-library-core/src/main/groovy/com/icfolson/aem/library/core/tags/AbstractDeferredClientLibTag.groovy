package com.icfolson.aem.library.core.tags

import com.google.common.base.Splitter

import javax.servlet.jsp.tagext.TagSupport

import static javax.servlet.jsp.PageContext.REQUEST_SCOPE

abstract class AbstractDeferredClientLibTag extends TagSupport {

    protected static final def ATTR_CATEGORIES = "deferredClientLibCategories"

    private static final def SPLITTER = Splitter.on(',').trimResults()

    String js

    protected List<String> getCategories() {
        SPLITTER.splitToList(js)
    }

    protected List<String> getRequestCategories() {
        def categories = pageContext.getAttribute(ATTR_CATEGORIES, REQUEST_SCOPE) as String

        !categories ? [] : SPLITTER.splitToList(categories)
    }
}
