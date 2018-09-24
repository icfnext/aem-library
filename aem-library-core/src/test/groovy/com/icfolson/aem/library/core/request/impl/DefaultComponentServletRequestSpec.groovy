package com.icfolson.aem.library.core.request.impl

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Unroll

@Unroll
class DefaultComponentServletRequestSpec extends AemLibrarySpec {

    def setupSpec() {
        pageBuilder.content {
            home {
                "jcr:content" {
                    component()
                }
            }
        }
    }

    def "get selectors"() {
        setup:
        def slingRequest = requestBuilder.build {
            selectors = selectorsList
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.selectors == selectorsList

        where:
        selectorsList << [["a", "b"], ["a"], []]
    }

    def "get request parameter optional"() {
        setup:
        def slingRequest = requestBuilder.build {
            parameterMap = ["a": ["1", "2"], "b": [""]]
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.getRequestParameter(parameterName).present == isPresent

        where:
        parameterName | isPresent
        "a"           | true
        "b"           | true
        "c"           | false
    }

    def "get request parameter"() {
        setup:
        def slingRequest = requestBuilder.build {
            parameterMap = ["a": ["1", "2"], "b": [""]]
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.getRequestParameter(parameterName).get() == parameterValue

        where:
        parameterName | parameterValue
        "a"           | "1"
        "b"           | ""
    }

    def "get request parameter with default value"() {
        setup:
        def slingRequest = requestBuilder.build {
            parameterMap = ["a": ["1", "2"], "b": [""]]
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.getRequestParameter(parameterName, "default") == parameterValue

        where:
        parameterName | parameterValue
        "a"           | "1"
        "b"           | ""
        "c"           | "default"
    }

    def "get request parameters optional"() {
        setup:
        def slingRequest = requestBuilder.build {
            parameterMap = ["a": ["1", "2"], "b": ["1"]]
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.getRequestParameters(parameterName).present == isPresent

        where:
        parameterName | isPresent
        "a"           | true
        "b"           | true
        "c"           | false
    }

    def "get request parameters"() {
        setup:
        def slingRequest = requestBuilder.build {
            parameterMap = ["a": ["1", "2"], "b": ["1"]]
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.getRequestParameters(parameterName).get() == parameterValues

        where:
        parameterName | parameterValues
        "a"           | ["1", "2"]
        "b"           | ["1"]
    }

    def "getters return non-null values"() {
        setup:
        def slingRequest = requestBuilder.build {
            path = "/content/home/jcr:content/component"
        }
        def slingResponse = responseBuilder.build()
        def request = new DefaultComponentServletRequest(slingRequest, slingResponse)

        expect:
        request.componentNode
        request.currentNode
        request.currentPage
        request.pageManager
        request.properties
        request.resource
        request.resourceResolver
        request.session
        request.pageProperties
    }
}
