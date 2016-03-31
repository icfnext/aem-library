package com.icfolson.aem.library.core.tags

import javax.servlet.jsp.PageContext
import javax.servlet.jsp.tagext.TagSupport

import static com.google.common.base.Preconditions.checkArgument

/**
 * Base class for scoped tag handlers containing a "scope" attribute corresponding to a <code>PageContext</code> scope
 * constant.
 */
abstract class AbstractScopedTag extends TagSupport {

    private static final Map<String, Integer> SCOPES = [
        "page": PageContext.PAGE_SCOPE,
        "request": PageContext.REQUEST_SCOPE,
        "session": PageContext.SESSION_SCOPE,
        "application": PageContext.APPLICATION_SCOPE
    ]

    /**
     * Scope of instance in page context.  Defaults to "page".
     */
    String scope

    /**
     * @param scope scope value
     * @return tag result
     */
    abstract int doEndTag(int scope)

    @Override
    final int doEndTag() {
        checkArgument(!scope || SCOPES[scope], "scope attribute is invalid = %s, must be one of %s", scope,
            SCOPES.keySet())

        def result

        if (scope) {
            result = doEndTag(SCOPES[scope])
        } else {
            // default
            result = doEndTag(PageContext.PAGE_SCOPE)
        }

        result
    }
}
