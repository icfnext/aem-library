package com.icfolson.aem.library.core.utils

import com.icfolson.aem.library.core.specs.AemLibrarySpec
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.resource.Resource
import spock.lang.Unroll

@Unroll
class PathUtilsSpec extends AemLibrarySpec {

    def "is content"() {
        expect:
        PathUtils.isContent(path) == result

        where:
        path                      | result
        "http://www.google.com"   | false
        "notcontent"              | false
        "/content"                | false
        "/content/"               | false
        "/content/global"         | true
        "/content/global/en"      | true
        "/content/global/en/test" | true
    }

    def "is external"() {
        expect:
        PathUtils.isExternal(path) == result

        where:
        path                      | result
        "http://www.google.com"   | true
        "http://"                 | true
        "notcontent"              | true
        "/content/global"         | false
        "/content/global/en"      | false
        "/content/global/en/test" | false
    }

    def "is external strict"() {
        setup:
        nodeBuilder.etc {
            designs()
        }

        expect:
        PathUtils.isExternal(path, resourceResolver) == result

        where:
        path                    | result
        "http://www.google.com" | true
        "notcontent"            | true
        "/etc/foo"              | true
        "/etc/designs"          | false
    }

    def "get page path for string"() {
        expect:
        PathUtils.getPagePath(path) == result

        where:
        path                           | result
        ""                             | ""
        "/content/foo"                 | "/content/foo"
        "/content/foo/jcr:content"     | "/content/foo"
        "/content/foo/jcr:content/bar" | "/content/foo"
    }

    def "get page path for request"() {
        setup:
        def request = Mock(SlingHttpServletRequest)
        def resource = Mock(Resource)

        resource.path >> path
        request.resource >> resource

        expect:
        PathUtils.getPagePath(request) == result

        where:
        path                           | result
        ""                             | ""
        "/content/foo"                 | "/content/foo"
        "/content/foo/jcr:content"     | "/content/foo"
        "/content/foo/jcr:content/bar" | "/content/foo"
    }
}