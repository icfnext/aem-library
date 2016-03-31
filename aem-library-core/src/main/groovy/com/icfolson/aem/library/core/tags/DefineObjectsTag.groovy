package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.api.components.annotations.AutoInstantiate
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.bindings.WCMModeBindings
import com.day.cq.wcm.api.components.Component
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.scripting.SlingScriptHelper

import javax.servlet.jsp.JspTagException

import static com.icfolson.aem.library.core.constants.ComponentConstants.PROPERTY_CLASS_NAME
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_COMPONENT_NAME
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_PAGE_NAME
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_PAGE_MANAGER_NAME
import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_SLING_NAME
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_REQUEST_NAME

/**
 * Add to and/or override attributes set in pageContext for use in JSPs.
 */
@Slf4j("LOG")
final class DefineObjectsTag extends AbstractComponentInstanceTag {

    private static final def ATTR_COMPONENT_INSTANCE_NAME = "componentInstanceName"

    @Override
    int doEndTag(int scope) {
        def slingRequest = pageContext.getAttribute(DEFAULT_REQUEST_NAME) as SlingHttpServletRequest

        setBindings(slingRequest)
        setOverrides(slingRequest)

        if (LOG.debugEnabled) {
            def resource = slingRequest.resource
            def sling = pageContext.getAttribute(DEFAULT_SLING_NAME) as SlingScriptHelper

            LOG.debug("instantiated component request for resource path = {} with type = {} and script = {}",
                resource.path, resource.resourceType, sling.script.scriptResource.path)
        }

        instantiateComponentClass()

        EVAL_PAGE
    }

    /**
     * Set AEM Library bindings (WCMMode flags, etc.) in the current page context.
     *
     * @param slingRequest current Sling request
     */
    private void setBindings(SlingHttpServletRequest slingRequest) {
        // add mode attributes to page context
        def bindings = new WCMModeBindings(slingRequest)

        bindings.each { key, value ->
            pageContext.setAttribute(key, value)
        }
    }

    /**
     * Override page manager and current page with decorated instances.
     *
     * @param slingRequest current Sling request
     */
    private void setOverrides(SlingHttpServletRequest slingRequest) {
        def pageManager = slingRequest.resourceResolver.adaptTo(PageManagerDecorator)
        def currentPage = pageManager.getContainingPage(slingRequest.resource)

        pageContext.setAttribute(DEFAULT_PAGE_MANAGER_NAME, pageManager)
        pageContext.setAttribute(DEFAULT_CURRENT_PAGE_NAME, currentPage)
    }

    /**
     * Instantiate the component class associated with the current component request, if necessary.
     */
    private void instantiateComponentClass() {
        def component = pageContext.getAttribute(DEFAULT_COMPONENT_NAME) as Component

        if (component) {
            def className = component.properties.get(PROPERTY_CLASS_NAME, "")
            def clazz

            try {
                clazz = !className ? null : Class.forName(className)
            } catch (ClassNotFoundException e) {
                LOG.error("class not found for name = $className", e)

                throw new JspTagException(e)
            }

            if (clazz) {
                setComponentInstance(clazz, className)
            } else {
                LOG.debug("class not found for component = {}, not instantiating component class",
                    component.resourceType)
            }
        } else {
            LOG.debug("component is null, not instantiating component class")
        }
    }

    private void setComponentInstance(Class<?> clazz, String className) {
        if (clazz.isAnnotationPresent(AutoInstantiate)) {
            def autoInstantiate = clazz.getAnnotation(AutoInstantiate)
            def instanceName = autoInstantiate.instanceName() ?: StringUtils.uncapitalize(clazz.simpleName)
            def instance = getInstance(clazz)

            LOG.debug("class name = {}, instance name = {}, setting component in page context", className,
                instanceName)

            pageContext.setAttribute(instanceName, instance)
            pageContext.setAttribute(ATTR_COMPONENT_INSTANCE_NAME, instanceName)
        } else {
            LOG.debug("annotation not present for class name = {}, not instantiating component class", className)
        }
    }
}
