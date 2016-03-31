package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import com.day.cq.wcm.api.designer.Design

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_CURRENT_DESIGN_NAME

class FavIconTagSpec extends AemLibrarySpec implements JspMetaTagTrait {

    static final def FAVICON = { favIcon ->
        """<link rel="icon" type="image/vnd.microsoft.icon" href="$favIcon">
<link rel="shortcut icon" type="image/vnd.microsoft.icon" href="$favIcon">"""
    }

    def setupSpec() {
        nodeBuilder.etc {
            designs {
                citytechinc {
                    "favicon.ico"()
                }
            }
        }
    }

    def "no favicon, no output"() {
        setup:
        def proxy = init(FavIconTag, "/", [(DEFAULT_CURRENT_DESIGN_NAME): Mock(Design) {
            getPath() >> ""
        }])

        when:
        proxy.tag.doEndTag()

        then:
        !proxy.output
    }

    def "valid favicon, HTML output"() {
        setup:
        def attributes = [(DEFAULT_CURRENT_DESIGN_NAME): Mock(Design) {
            getPath() >> "/etc/designs/citytechinc"
        }]

        def proxy = init(FavIconTag, "/", attributes)

        when:
        proxy.tag.doEndTag()

        then:
        proxy.output == FAVICON("/etc/designs/citytechinc/favicon.ico") as String
    }
}