package com.icfolson.aem.library.core.tags

import groovy.util.logging.Slf4j

/**
 * Instantiates a component class and sets it in JSP page context.
 */
@Slf4j("LOG")
final class ComponentTag extends AbstractComponentInstanceTag {

    /**
     * Component class to instantiate.
     */
    String className

    /**
     * Name to set in pageContext for the component class.
     */
    String name

    @Override
    int doEndTag(int scope) {
        LOG.debug("class name = {}, attribute name = {}, scope = {}", className, name, scope)

        pageContext.setAttribute(name, getInstance(className), scope)

        EVAL_PAGE
    }
}
