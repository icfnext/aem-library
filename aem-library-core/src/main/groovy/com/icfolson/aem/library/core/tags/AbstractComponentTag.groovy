package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.api.node.ComponentNode
import org.apache.sling.api.resource.Resource

import javax.servlet.jsp.tagext.TagSupport

import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_RESOURCE_NAME

abstract class AbstractComponentTag extends TagSupport {

    /**
     * Property name for tag.
     */
    String propertyName

    /**
     * Should property value be inherited? Defaults to false.
     */
    String inherit

    protected ComponentNode getComponentNode() {
        def resource = pageContext.getAttribute(DEFAULT_RESOURCE_NAME) as Resource

        resource.adaptTo(ComponentNode)
    }

    protected boolean isInherit() {
        Boolean.valueOf(inherit)
    }
}
