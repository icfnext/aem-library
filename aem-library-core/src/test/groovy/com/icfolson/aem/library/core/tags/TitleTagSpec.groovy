package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class TitleTagSpec extends AemLibrarySpec implements JspMetaTagTrait {

    def setupSpec() {
        pageBuilder.content {
            citytechinc("CITYTECH, Inc.") {
                "jcr:content"(pageTitle: "CITYTECH")
            }
            ctmsp {
                "jcr:content"(pageTitle: "CTMSP")
            }
        }
    }

    def "title variations"() {
        setup:
        def proxy = init(TitleTag, "/content/citytechinc")
        def tag = proxy.tag as TitleTag

        tag.with {
            propertyName = testPropertyName
            suffix = testSuffix
        }

        when:
        tag.doEndTag()

        then:
        proxy.output == html

        where:
        testPropertyName | testSuffix       | html
        ""               | ""               | "<title>CITYTECH, Inc.</title>"
        ""               | " | Chicago, IL" | "<title>CITYTECH, Inc. | Chicago, IL</title>"
        "pageTitle"      | ""               | "<title>CITYTECH</title>"
        "pageTitle"      | " | Chicago, IL" | "<title>CITYTECH | Chicago, IL</title>"
        "navTitle"       | ""               | "<title>CITYTECH, Inc.</title>"
        "navTitle"       | " | Chicago, IL" | "<title>CITYTECH, Inc. | Chicago, IL</title>"
    }

    def "empty title defaults to page name"() {
        setup:
        def proxy = init(TitleTag, "/content/ctmsp")

        when:
        proxy.tag.doEndTag()

        then:
        proxy.output == "<title>ctmsp</title>"
    }
}
