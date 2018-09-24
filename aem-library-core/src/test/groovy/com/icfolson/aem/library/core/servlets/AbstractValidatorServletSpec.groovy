package com.icfolson.aem.library.core.servlets

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import groovy.json.JsonBuilder
import org.apache.sling.api.SlingHttpServletRequest

class AbstractValidatorServletSpec extends AemLibrarySpec {

    class ValidatorServlet extends AbstractValidatorServlet {

        @Override
        protected boolean isValid(SlingHttpServletRequest request, String path, String value) {
            false
        }
    }

    def setupSpec() {
        pageBuilder.content {
            home()
        }
    }

    def "exception thrown when value parameter is null"() {
        setup:
        def servlet = new ValidatorServlet()

        def request = requestBuilder.build {
            path = "/content/home"
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        thrown(NullPointerException)
    }

    def "validate method called with correct arguments"() {
        setup:
        def servlet = Spy(ValidatorServlet) {
            1 * isValid(_, "/content/home", "foo")
        }

        def request = requestBuilder.build {
            path = "/content/home"
            parameterMap = ["value": "foo"]
        }

        def response = responseBuilder.build()

        when:
        servlet.doGet(request, response)

        then:
        response.outputAsString == new JsonBuilder([valid: false]).toString()
    }
}
