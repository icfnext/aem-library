package com.icfolson.aem.library.models.impl

import com.day.cq.tagging.Tag
import com.icfolson.aem.library.models.annotations.TagInject
import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import org.apache.sling.api.resource.Resource
import org.apache.sling.models.annotations.DefaultInjectionStrategy
import org.apache.sling.models.annotations.Model

import javax.inject.Inject

import static com.day.cq.tagging.TagConstants.NT_TAG

class TagInjectorSpec extends AemLibraryModelSpec {

    @Model(adaptables = Resource, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    static class Component {

        @Inject
        Tag singleTag

        @Inject
        List<Tag> tagList

        @TagInject(inherit = true)
        Tag singleTagInherit

        @TagInject(inherit = true)
        List<Tag> tagListInherit
    }

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component(
                        singleTag: "beers:lager",
                        tagList: [
                            "beers:lager",
                            "beers:stout",
                            "beers:ale"
                        ],
                        singleTagInherit: "beers:porter",
                        tagListInherit: [
                            "beers:ale",
                            "beers:porter",
                            "beers:lager"
                        ]
                    )
                }
                page1 { "jcr:content" { component() } }
            }
        }

        nodeBuilder.etc {
            tags {
                beers(NT_TAG, "sling:resourceType": "cq/tagging/components/tag", title: "Beers") {
                    lager(NT_TAG, "sling:resourceType": "cq/tagging/components/tag", title: "Lager")
                    stout(NT_TAG, "sling:resourceType": "cq/tagging/components/tag", title: "Stout")
                    porter(NT_TAG, "sling:resourceType": "cq/tagging/components/tag", title: "Porter")
                    ale(NT_TAG, "sling:resourceType": "cq/tagging/components/tag", title: "Ale")
                }
            }
        }
    }

    def "all tags populated from root"() {
        setup:
        def resource = resourceResolver.resolve("/content/citytechinc/jcr:content/component")
        def component = resource.adaptTo(Component)

        expect:
        component.singleTag.path == "/etc/tags/beers/lager"
        component.singleTagInherit.path == "/etc/tags/beers/porter"
        component.tagList.size() == 3
        component.tagList[0].path == "/etc/tags/beers/lager"
        component.tagList[1].path == "/etc/tags/beers/stout"
        component.tagList[2].path == "/etc/tags/beers/ale"

        component.tagListInherit.size() == 3
        component.tagListInherit[0].path == "/etc/tags/beers/ale"
        component.tagListInherit[1].path == "/etc/tags/beers/porter"
        component.tagListInherit[2].path == "/etc/tags/beers/lager"
    }

    def "all inherited tags populated"() {
        setup:
        def resource = resourceResolver.resolve("/content/citytechinc/page1/jcr:content/component")
        def component = resource.adaptTo(Component)

        expect:
        component.singleTag == null
        component.singleTagInherit.path == "/etc/tags/beers/porter"
        component.tagList == null

        component.tagListInherit.size() == 3
        component.tagListInherit[0].path == "/etc/tags/beers/ale"
        component.tagListInherit[1].path == "/etc/tags/beers/porter"
        component.tagListInherit[2].path == "/etc/tags/beers/lager"
    }
}
