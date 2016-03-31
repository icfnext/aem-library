package com.icfolson.aem.library.core.tags

import com.day.cq.wcm.api.designer.Design
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.ResourceResolver

import javax.servlet.jsp.JspTagException

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_DESIGN_NAME
import static org.apache.sling.scripting.jsp.taglib.DefineObjectsTag.DEFAULT_RESOURCE_RESOLVER_NAME

@Slf4j("LOG")
final class FavIconTag extends AbstractMetaTag {

    private static final def TAG_START = "<link rel="

    private static final def RELS = ["icon", "shortcut icon"]

    @Override
    int doEndTag() {
        def currentDesign = pageContext.getAttribute(DEFAULT_CURRENT_DESIGN_NAME) as Design
        def resourceResolver = pageContext.getAttribute(DEFAULT_RESOURCE_RESOLVER_NAME) as ResourceResolver

        def favIconPath = "${currentDesign.path}/favicon.ico"
        def favIcon = !resourceResolver.getResource(favIconPath) ? null : favIconPath

        if (favIcon) {
            def html = new StringBuilder()

            RELS.each { rel ->
                html.append(TAG_START)
                html.append('"')
                html.append(rel)
                html.append("\" type=\"image/vnd.microsoft.icon\" href=\"")
                html.append(xssApi.getValidHref(favIcon))
                html.append(tagEnd)
                html.append('\n')
            }

            html.deleteCharAt(html.length() - 1)

            try {
                pageContext.out.write(html.toString())
            } catch (IOException e) {
                LOG.error("error writing favicon", e)

                throw new JspTagException(e)
            }
        } else {
            LOG.debug("favicon is null, skipping output")
        }

        EVAL_PAGE
    }
}
