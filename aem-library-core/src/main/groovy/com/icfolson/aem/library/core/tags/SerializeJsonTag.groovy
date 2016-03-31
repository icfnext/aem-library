package com.icfolson.aem.library.core.tags

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

import static com.google.common.base.Preconditions.checkArgument

/**
 * Serializes a component class or object instance as JSON.  The class to be serialized should be annotated using
 * Jackson annotations (e.g. <code>JsonGetter</code>, <code>JsonProperty</code>) to indicate which fields or methods
 * should be serialized (this is not necessary for all types, e.g. basic POJOs and collections).
 */
@Slf4j("LOG")
final class SerializeJsonTag extends AbstractComponentInstanceTag {

    private static final def MAPPER = new ObjectMapper()

    /**
     * Component class to instantiate and serialize.
     */
    String className

    /**
     * Name of existing component or object in page context.  <code>className</code> attribute is checked first.
     */
    String instanceName

    /**
     * Optional name to set in pageContext for the component instance.  This only applies when the
     * <code>className</code> attribute is used.
     */
    String name

    @Override
    int doEndTag(int scope) {
        checkArgument(className || instanceName, "className or instanceName attribute is required")

        try {
            def object

            if (className) {
                LOG.debug("serializing JSON for class name = {}", className)

                object = getInstance(className)

                if (name) {
                    pageContext.setAttribute(name, object, scope)
                }
            } else {
                LOG.debug("serializing JSON for instance name = {}", instanceName)

                object = pageContext.getAttribute(instanceName, scope)
            }

            pageContext.out.write(MAPPER.writeValueAsString(object))
        } catch (IOException e) {
            LOG.error("error serializing JSON", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
