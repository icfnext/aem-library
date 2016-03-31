package com.icfolson.aem.library.core.tags

import com.day.cq.wcm.api.WCMMode
import groovy.util.logging.Slf4j
import org.apache.sling.api.SlingHttpServletRequest

import javax.servlet.jsp.tagext.TagSupport

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_REQUEST_NAME

/**
 * Force "disabled" WCM mode in the tag body.
 */
@Slf4j("LOG")
final class DisableAuthorTag extends TagSupport {

    private static final def ATTR_PREVIOUS_WCMMODE = "previous-wcm-mode-"

    /**
     * Disables author if true, otherwise this tag does nothing.  Defaults to true.
     */
    String test

    @Override
    int doStartTag() {
        if (disableAuthor) {
            def slingRequest = pageContext.getAttribute(DEFAULT_REQUEST_NAME) as SlingHttpServletRequest

            def path = slingRequest.resource.path

            LOG.debug("disabling authoring for path = {}", path)

            slingRequest.setAttribute(ATTR_PREVIOUS_WCMMODE + path, WCMMode.fromRequest(slingRequest))

            WCMMode.DISABLED.toRequest(slingRequest)
        }

        EVAL_BODY_INCLUDE
    }

    @Override
    int doEndTag() {
        if (disableAuthor) {
            def slingRequest = pageContext.getAttribute(DEFAULT_REQUEST_NAME) as SlingHttpServletRequest

            def path = slingRequest.resource.path
            def mode = slingRequest.getAttribute(ATTR_PREVIOUS_WCMMODE + path) as WCMMode

            LOG.debug("restoring mode = {} for path = {}", mode.name(), path)

            mode.toRequest(slingRequest)
        }

        EVAL_PAGE
    }

    private boolean isDisableAuthor() {
        !this.test ? true : Boolean.valueOf(this.test)
    }
}
