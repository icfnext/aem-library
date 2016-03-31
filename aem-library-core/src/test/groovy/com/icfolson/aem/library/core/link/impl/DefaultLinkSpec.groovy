package com.icfolson.aem.library.core.link.impl

import spock.lang.Specification

class DefaultLinkSpec extends Specification {

    def "empty link with null arguments"() {
        setup:
        def link = new DefaultLink(null, null, null, null, null, null, false, null, null, null)

        expect:
        link.empty
    }
}
