package com.icfolson.aem.library.core.servlets.optionsprovider

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class OptionSpec extends Specification {

    def "sort options"() {
        setup:
        def options = Option.fromMap(map)

        Collections.sort(options, comparator)

        expect:
        options*.value == ["one", "two"]

        where:
        map                          | comparator
        ["two": "Two", "one": "One"] | Option.ALPHA
        ["two": "Two", "one": "one"] | Option.ALPHA_IGNORE_CASE
    }
}
