package com.icfolson.aem.library.core.tags

import com.day.cq.wcm.api.Page
import com.day.cq.wcm.commons.WCMUtils
import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_PAGE_NAME

/**
 * Render the keywords for the current page.
 */
@Slf4j("LOG")
final class KeywordsTag extends AbstractMetaTag {

    private static final def TAG_START = "<meta name=\"keywords\" content=\""

    @Override
    int doEndTag() {
        def currentPage = pageContext.getAttribute(DEFAULT_CURRENT_PAGE_NAME) as Page

        def builder = new StringBuilder()

        builder.append(TAG_START)
        builder.append(xssApi.encodeForHTMLAttr(WCMUtils.getKeywords(currentPage, false)))
        builder.append(tagEnd)

        try {
            pageContext.out.write(builder.toString())
        } catch (IOException e) {
            LOG.error("error writing keywords tag for page = ${currentPage.path}", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
