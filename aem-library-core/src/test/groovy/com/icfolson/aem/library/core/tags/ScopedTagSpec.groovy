package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.icfolson.aem.prosper.traits.JspTagTrait
import spock.lang.Unroll

import javax.servlet.jsp.PageContext

@Unroll
class ScopedTagSpec extends AemLibrarySpec implements JspTagTrait {

    static final def ATTRIBUTE_NAME = "scopedAttribute"

    static final def ATTRIBUTE_VALUE = "attributeValue"

    static class ScopedTag extends AbstractScopedTag {

        @Override
        int doEndTag(int scope) {
            pageContext.setAttribute ATTRIBUTE_NAME, ATTRIBUTE_VALUE, scope

            EVAL_PAGE
        }
    }

    def "set attribute in scope"() {
        setup:
        def proxy = init(ScopedTag, "/")
        def tag = proxy.tag as ScopedTag

        tag.scope = scope

        when:
        tag.doEndTag()

        then:
        proxy.pageContext.getAttribute(ATTRIBUTE_NAME, scopeValue as Integer) == ATTRIBUTE_VALUE

        where:
        scope         | scopeValue
        "page"        | PageContext.PAGE_SCOPE
        "request"     | PageContext.REQUEST_SCOPE
        "session"     | PageContext.SESSION_SCOPE
        "application" | PageContext.APPLICATION_SCOPE
    }

    def "invalid scope throws exception"() {
        setup:
        def tag = new ScopedTag()

        tag.scope = "invalid"

        when:
        tag.doEndTag()

        then:
        thrown(IllegalArgumentException)
    }
}
