package com.icfolson.aem.library.core.servlets

import com.icfolson.aem.library.api.request.ComponentServletRequest
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import groovy.json.JsonBuilder

import javax.servlet.ServletException

class AbstractComponentServletSpec extends AemLibrarySpec {

    static final def MAP = [one: 1, two: 2]

    def setupSpec() {
        pageBuilder.content {
            citytechinc {
                "jcr:content" {
                    component("jcr:title": "Testing Component")
                }
            }
        }
    }

    def "process get"() {
        setup:
        def slingRequest = requestBuilder.build()
        def slingResponse = responseBuilder.build()

        def servlet = new AbstractComponentServlet() {
            @Override
            protected void processGet(ComponentServletRequest request) throws ServletException, IOException {
                new JsonBuilder(MAP).writeTo(request.slingResponse.writer)
            }
        }

        when:
        servlet.doGet(slingRequest, slingResponse)

        then:
        slingResponse.contentAsString == new JsonBuilder(MAP).toString()
    }
}
