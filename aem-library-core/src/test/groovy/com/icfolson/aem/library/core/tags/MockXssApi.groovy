package com.icfolson.aem.library.core.tags

import com.adobe.granite.xss.XSSAPI
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.resource.ResourceResolver

class MockXssApi implements XSSAPI {

    @Override
    Integer getValidInteger(String source, int i) {
        i
    }

    @Override
    Long getValidLong(String source, long l) {
        l
    }

    @Override
    String getValidDimension(String source, String s1) {
        source
    }

    @Override
    String getValidHref(String source) {
        source
    }

    @Override
    String getValidHref(String source, boolean b) {
        source
    }

    @Override
    String getValidJSToken(String source, String s1) {
        source
    }

    @Override
    String getValidCSSColor(String source, String s1) {
        source
    }

    @Override
    String encodeForHTML(String source) {
        source
    }

    @Override
    String encodeForHTMLAttr(String source) {
        source
    }

    @Override
    String encodeForXML(String source) {
        source
    }

    @Override
    String encodeForXMLAttr(String source) {
        source
    }

    @Override
    String encodeForJSString(String source) {
        source
    }

    @Override
    String filterHTML(String source) {
        source
    }

    @Override
    XSSAPI getRequestSpecificAPI(SlingHttpServletRequest slingHttpServletRequest) {
        this
    }

    @Override
    XSSAPI getResourceResolverSpecificAPI(ResourceResolver resourceResolver) {
        this
    }
}
