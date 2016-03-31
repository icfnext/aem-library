package com.icfolson.aem.library.core.tags

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class PropertyTagSpec extends AemLibrarySpec implements JspMetaTagTrait {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component("jcr:title": "Component")
                }
                company() {
                    "jcr:content" {
                        component()
                    }
                }
            }
        }
    }

    def "test property value"() {
        setup:
        def proxy = init(PropertyTag, path)
        def tag = proxy.tag as PropertyTag

        tag.with {
            propertyName = "jcr:title"
            inherit = testInherit
            defaultValue = testDefaultValue
        }

        when:
        tag.doEndTag()

        then:
        proxy.output == propertyValue

        where:
        path                                                 | testInherit | testDefaultValue | propertyValue
        "/content/citytechinc/jcr:content/component"         | false       | ""               | "Component"
        "/content/citytechinc/jcr:content/component"         | false       | "Default"        | "Component"
        "/content/citytechinc/jcr:content/component"         | true        | ""               | "Component"
        "/content/citytechinc/company/jcr:content/component" | false       | ""               | ""
        "/content/citytechinc/company/jcr:content/component" | false       | "Default"        | "Default"
        "/content/citytechinc/company/jcr:content/component" | true        | ""               | "Component"
        "/content/citytechinc/company/jcr:content/component" | true        | "Default"        | "Component"
    }
}
