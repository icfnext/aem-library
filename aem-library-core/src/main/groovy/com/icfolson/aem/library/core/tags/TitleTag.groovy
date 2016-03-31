package com.icfolson.aem.library.core.tags

import com.day.cq.wcm.api.Page
import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_PAGE_NAME

/**
 * Render the title for the current page.
 */
@Slf4j("LOG")
final class TitleTag extends AbstractMetaTag {

    private static final def TAG_START = "<title>"

    private static final def TAG_END = "</title>"

    String suffix

    @Override
    int doEndTag() {
        def currentPage = pageContext.getAttribute(DEFAULT_CURRENT_PAGE_NAME) as Page
        def pageTitle = currentPage.title ?: currentPage.name

        def title = new StringBuilder()

        if (propertyName) {
            title.append(currentPage.properties.get(propertyName, pageTitle))
        } else {
            title.append(pageTitle)
        }

        if (suffix) {
            title.append(suffix)
        }

        def builder = new StringBuilder()

        builder.append(TAG_START)
        builder.append(xssApi.encodeForHTML(title.toString()))
        builder.append(TAG_END)

        try {
            pageContext.out.write(builder.toString())
        } catch (IOException e) {
            LOG.error("error writing title tag", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
