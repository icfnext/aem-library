package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class DescriptionTagSpec extends AemLibrarySpec implements JspMetaTagTrait {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content"("jcr:description": "JCR Description", "description": "Description")
            }
            ctmsp()
        }
    }

    def "description variations"() {
        setup:
        def proxy = init(DescriptionTag, "/content/citytechinc")
        def tag = proxy.tag as DescriptionTag

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
        ""               | ""               | '<meta name="description" content="JCR Description">'
        ""               | " | Chicago, IL" | '<meta name="description" content="JCR Description | Chicago, IL">'
        "description"    | ""               | '<meta name="description" content="Description">'
        "description"    | " | Chicago, IL" | '<meta name="description" content="Description | Chicago, IL">'
    }

    def "empty description"() {
        setup:
        def proxy = init(DescriptionTag, "/content/ctmsp")

        when:
        proxy.tag.doEndTag()

        then:
        proxy.output == '<meta name="description" content="">'
    }
}
