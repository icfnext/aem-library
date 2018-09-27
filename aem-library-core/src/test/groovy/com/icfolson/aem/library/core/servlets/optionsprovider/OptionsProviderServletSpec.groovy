package com.icfolson.aem.library.core.servlets.optionsprovider

import com.icfolson.aem.library.api.request.ComponentServletRequest
import com.google.common.base.Optional
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import groovy.json.JsonBuilder

class OptionsProviderServletSpec extends AemLibrarySpec {

    static final def MAP = ["one": "One", "two": "Two"]

    static final def LIST = MAP.collect { value, text -> [value: value, text: text] }

    static final def OPTIONS = Option.fromMap(MAP)

    class NoOptionsProviderServlet extends AbstractOptionsProviderServlet {

        @Override
        List<Option> getOptions(ComponentServletRequest request) {
            []
        }

        @Override
        Optional<String> getOptionsRoot(ComponentServletRequest request) {
            Optional.absent()
        }
    }

    class RootOptionsProviderServlet extends AbstractOptionsProviderServlet {

        @Override
        List<Option> getOptions(ComponentServletRequest request) {
            OPTIONS
        }

        @Override
        Optional<String> getOptionsRoot(ComponentServletRequest request) {
            Optional.of("root")
        }
    }

    class NoRootOptionsProviderServlet extends AbstractOptionsProviderServlet {

        @Override
        List<Option> getOptions(ComponentServletRequest request) {
            OPTIONS
        }

        @Override
        Optional<String> getOptionsRoot(ComponentServletRequest request) {
            Optional.absent()
        }
    }

    def "no options"() {
        def servlet = new NoOptionsProviderServlet()
        def request = requestBuilder.build()
        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.outputAsString == new JsonBuilder([]).toString()
    }

    def "options with root"() {
        def servlet = new RootOptionsProviderServlet()
        def request = requestBuilder.build()
        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.outputAsString == new JsonBuilder(["root": LIST]).toString()
    }

    def "options with no root"() {
        def servlet = new NoRootOptionsProviderServlet()
        def request = requestBuilder.build()
        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.outputAsString == new JsonBuilder(LIST).toString()
    }
}