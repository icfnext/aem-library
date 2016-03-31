package com.icfolson.aem.library.core.node.predicates

import spock.lang.Specification
import spock.lang.Unroll

import javax.jcr.Property
import javax.jcr.RepositoryException

@Unroll
class PropertyNamePredicateSpec extends Specification {

    def "predicate applied when property matches name"() {
        setup:
        def predicate = new PropertyNamePredicate("one")

        def property = Mock(Property) {
            getName() >> propertyName
        }

        expect:
        predicate.apply(property) == result

        where:
        propertyName | result
        "one"        | true
        "two"        | false
    }

    def "predicate not applied when property throws exception"() {
        setup:
        def predicate = new PropertyNamePredicate("propertyName")

        def property = Mock(Property) {
            getName() >> {
                throw new RepositoryException()
            }
        }

        expect:
        !predicate.apply(property)
    }
}
