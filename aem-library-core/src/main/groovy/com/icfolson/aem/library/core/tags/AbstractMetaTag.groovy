package com.icfolson.aem.library.core.tags

import com.adobe.granite.xss.XSSAPI
import com.day.cq.widget.Doctype

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_XSSAPI_NAME

@SuppressWarnings("deprecation")
abstract class AbstractMetaTag extends AbstractComponentTag {

    protected final String getTagEnd() {
        def builder = new StringBuilder()

        builder.append('"')
        builder.append(Doctype.isXHTML(pageContext.request) ? "/" : "")
        builder.append('>')

        builder.toString()
    }

    protected final XSSAPI getXssApi() {
        pageContext.getAttribute(DEFAULT_XSSAPI_NAME) as XSSAPI
    }
}
