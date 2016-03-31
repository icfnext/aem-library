package com.icfolson.aem.library.core.page.impl

import com.icfolson.aem.library.api.page.PageDecorator
import com.day.cq.tagging.TagConstants
import com.google.common.base.Predicate
import com.icfolson.aem.library.core.specs.AemLibrarySpec

import javax.jcr.query.Query

class DefaultPageManagerDecoratorSpec extends AemLibrarySpec {

    def setupSpec() {
        nodeBuilder.etc {
            tags {
                tag1("cq:Tag")
                tag2("cq:Tag")
                tag3("cq:Tag")
            }
        }

        pageBuilder.content {
            citytechinc("CITYTECH, Inc.") {
                "jcr:content"(otherPagePath: "/content/ales/esb") {
                    component {
                        one("sling:resourceType": "one", "cq:tags": "/etc/tags/tag3")
                        two("sling:resourceType": "two")
                    }
                }
                child {
                    "jcr:content"(hideInNav: true, "cq:template": "template", "cq:tags": "/etc/tags/tag1")
                }
            }
            other {
                "jcr:content"("cq:template": "template", "cq:tags": ["/etc/tags/tag1", "/etc/tags/tag2"])
            }
            hierarchy {
                one {
                    child1()
                    child2()
                    child3()
                }
                two()
                three()
            }
        }

        def taggableNodePaths = ["/content/citytechinc/child/jcr:content", "/content/other/jcr:content",
            "/content/citytechinc/jcr:content/component/one"]

        taggableNodePaths.each { path ->
            session.getNode(path).addMixin(TagConstants.NT_TAGGABLE)
        }

        session.save()
    }

    def "find pages for predicate"() {
        setup:
        def predicate = new Predicate<PageDecorator>() {
            @Override
            boolean apply(PageDecorator pageDecorator) {
                pageDecorator.name.startsWith("child")
            }
        }

        expect:
        pageManager.findPages("/content/hierarchy", predicate).size() == 3
    }

    def "find pages for tag IDs"() {
        expect:
        pageManager.findPages("/content", ["/etc/tags/tag1"], true).size() == 2
    }

    def "find pages for tag IDs matching all"() {
        expect:
        pageManager.findPages("/content", ["/etc/tags/tag1", "/etc/tags/tag2"], false).size() == 1
    }

    def "tagged non-page node is excluded from search results"() {
        expect:
        !pageManager.findPages("/content", ["/etc/tags/tag3"], true)
    }

    def "search"() {
        setup:
        def statement = "/jcr:root/content//element(*, cq:Page) order by @jcr:score descending"
        def query = session.workspace.queryManager.createQuery(statement, Query.XPATH)

        expect:
        pageManager.search(query).size() == 10
    }

    def "search with limit"() {
        setup:
        def statement = "/jcr:root/content//element(*, cq:Page) order by @jcr:score descending"
        def query = session.workspace.queryManager.createQuery(statement, Query.XPATH)

        expect:
        pageManager.search(query, 1).size() == 1
    }

    def "find pages for template"() {
        expect:
        pageManager.findPages("/content", "template").size() == 2
    }

    def "find pages for non-existing template"() {
        expect:
        !pageManager.findPages("/content", "ghost")
    }

    def "find pages for template with invalid starting path"() {
        expect:
        !pageManager.findPages("/etc", "template")
    }
}
