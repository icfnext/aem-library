package com.icfolson.aem.library.core.tags

import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

/**
 * Render an href attribute value for the given property name.
 */
@Slf4j("LOG")
final class HrefTag extends AbstractComponentTag {

    /**
     * Default value if property does not exist.
     */
    String defaultValue = ""

    @Override
    int doEndTag() {
        def optionalHref

        if (inherit) {
            optionalHref = componentNode.getAsHrefInherited(propertyName)
        } else {
            optionalHref = componentNode.getAsHref(propertyName)
        }

        def href = optionalHref.or(defaultValue)

        try {
            pageContext.out.write(href)
        } catch (IOException e) {
            LOG.error("error writing href = $href", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
