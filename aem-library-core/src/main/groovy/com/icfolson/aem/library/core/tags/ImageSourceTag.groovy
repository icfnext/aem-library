package com.icfolson.aem.library.core.tags

import groovy.util.logging.Slf4j

import javax.servlet.jsp.JspTagException

import static com.google.common.base.CharMatcher.DIGIT
import static com.google.common.base.Preconditions.checkArgument

/**
 * Render an image source path for the current component.
 */
@Slf4j("LOG")
final class ImageSourceTag extends AbstractComponentTag {

    String defaultValue = ""

    String name

    String width

    @Override
    int doEndTag() {
        checkArgument(DIGIT.matchesAllOf(width), "invalid width attribute = %s, must be numeric", width)

        def width = this.width as Integer

        def imageSource

        if (inherit) {
            if (this.name) {
                imageSource = componentNode.getImageSourceInherited(name, width)
            } else {
                imageSource = componentNode.getImageSourceInherited(width)
            }
        } else {
            if (this.name) {
                imageSource = componentNode.getImageSource(name, width)
            } else {
                imageSource = componentNode.getImageSource(width)
            }
        }

        try {
            pageContext.out.write(imageSource.or(defaultValue))
        } catch (IOException e) {
            LOG.error("error writing image source = $imageSource", e)

            throw new JspTagException(e)
        }

        EVAL_PAGE
    }
}
