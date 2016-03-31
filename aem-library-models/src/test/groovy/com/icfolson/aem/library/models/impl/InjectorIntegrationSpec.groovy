package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.models.specs.AemLibraryModelSpec
import com.day.cq.tagging.TagManager
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.models.annotations.Model

import javax.inject.Inject

class InjectorIntegrationSpec extends AemLibraryModelSpec {

    @Model(adaptables = SlingHttpServletRequest)
    static class InjectorIntegrationComponent {

        @Inject
        PageManagerDecorator pageManager

        @Inject
        TagManager tagManager
    }

    def "injected values from multiple injectors are correct types"() {
        setup:
        def request = requestBuilder.build()
        def model = request.adaptTo(InjectorIntegrationComponent)

        expect:
        model.pageManager instanceof PageManagerDecorator

        and:
        model.tagManager instanceof TagManager
    }
}
