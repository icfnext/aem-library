package com.icfolson.aem.library.core.tags

import com.day.cq.wcm.foundation.Image
import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

/**
 * Draw an HTML image tag for the current component.
 */
@Slf4j("LOG")
final class ImageTag extends AbstractComponentTag {

    String alt

    String name

    String title

    @Override
    int doEndTag() {
        def resource = componentNode.resource
        def image = name ? new Image(resource, name) : new Image(resource)

        if (alt) {
            image.alt = alt
        }

        if (title) {
            image.title = title
        }

        if (image.hasContent()) {
            try {
                image.draw(pageContext.out)
            } catch (IOException e) {
                LOG.error("error writing image tag for name = $name", e)

                throw new JspTagException(e)
            }
        }

        EVAL_PAGE
    }
}
