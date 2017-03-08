package com.icfolson.aem.library.core.tags

import com.adobe.granite.ui.clientlibs.HtmlLibraryManager
import groovy.util.logging.Slf4j
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.scripting.SlingScriptHelper

import javax.servlet.jsp.JspTagException

import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_REQUEST_NAME
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_SLING_NAME

@Slf4j("LOG")
final class DeferredClientLibTag extends AbstractDeferredClientLibTag {

    @Override
    int doEndTag() {
        def slingRequest = pageContext.getAttribute(DEFAULT_REQUEST_NAME) as SlingHttpServletRequest
        def sling = pageContext.getAttribute(DEFAULT_SLING_NAME) as SlingScriptHelper
        def htmlLibraryManager = sling.getService(HtmlLibraryManager)

        def uniqueCategories = new LinkedHashSet<String>(requestCategories)

        LOG.debug("writing deferred client libraries = {}", uniqueCategories)

        try {
            htmlLibraryManager.writeJsInclude(slingRequest, pageContext.out, uniqueCategories as String[])
        } catch (IOException e) {
            LOG.error("error writing deferred client libraries = $uniqueCategories", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
