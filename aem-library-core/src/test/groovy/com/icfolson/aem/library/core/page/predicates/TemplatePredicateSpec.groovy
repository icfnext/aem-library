package com.icfolson.aem.library.core.page.predicates

import com.icfolson.aem.library.core.specs.AemLibrarySpec

class TemplatePredicateSpec extends AemLibrarySpec {

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content"("cq:template": "homepage")
                child1 {
                    "jcr:content"("cq:template": "template")
                }
                child2()
            }
        }
    }

    def "page has no template property"() {
        setup:
        def page = getPageDecorator("/content/citytechinc/child2")
        def predicate = new TemplatePredicate("template")

        expect:
        !predicate.apply(page)
    }

    def "template matches page template"() {
        setup:
        def page = getPageDecorator("/content/citytechinc/child1")
        def predicate = new TemplatePredicate("template")
        def predicateForPage = new TemplatePredicate(page)

        expect:
        predicate.apply(page) && predicateForPage.apply(page)
    }

    def "template does not match page template"() {
        setup:
        def page = getPageDecorator("/content/citytechinc")
        def predicate = new TemplatePredicate("template")

        expect:
        !predicate.apply(page)
    }
}
