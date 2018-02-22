package com.icfolson.aem.library.core.services

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class OsgiConfigurationSpec extends Specification {

    @Shared
    OsgiConfiguration configuration

    def setupSpec() {
        def properties = [
            mapProperty        : ["1=one", "2=two", "3=three"],
            doubleProperty     : 1.1,
            longProperty       : 1L,
            booleanProperty    : true,
            integerProperty    : 1,
            stringProperty     : "value",
            stringArrayProperty: ["one", "two"] as String[]
        ]

        configuration = new OsgiConfiguration(properties)
    }

    def "to map"() {
        expect:
        configuration.toMap(propertyName) == result

        where:
        propertyName          | result
        "mapProperty"         | ["1": "one", "2": "two", "3": "three"]
        "nonExistentProperty" | [:]
    }

    def "to map with default value"() {
        expect:
        configuration.toMap(propertyName, ["a=1", "b=2"]) == result

        where:
        propertyName          | result
        "mapProperty"         | ["1": "one", "2": "two", "3": "three"]
        "nonExistentProperty" | ["a": "1", "b": "2"]
    }

    def "get as double"() {
        expect:
        configuration.getAsDouble(propertyName, 0.0 as Double) == result as Double

        where:
        propertyName          | result
        "doubleProperty"      | 1.1
        "nonExistentProperty" | 0.0
    }

    def "get as long"() {
        expect:
        configuration.getAsLong(propertyName, 0L) == result

        where:
        propertyName          | result
        "longProperty"        | 1L
        "nonExistentProperty" | 0L
    }

    def "get as boolean"() {
        expect:
        configuration.getAsBoolean(propertyName, false) == result

        where:
        propertyName          | result
        "booleanProperty"     | true
        "nonExistentProperty" | false
    }

    def "get as integer"() {
        expect:
        configuration.getAsInteger(propertyName, 0) == result

        where:
        propertyName          | result
        "integerProperty"     | 1
        "nonExistentProperty" | 0
    }

    def "get as string"() {
        expect:
        configuration.getAsString(propertyName, "defaultValue") == result

        where:
        propertyName          | result
        "stringProperty"      | "value"
        "nonExistentProperty" | "defaultValue"
    }

    def "get as list"() {
        expect:
        configuration.getAsList(propertyName) == result

        where:
        propertyName          | result
        "stringProperty"      | ["value"]
        "stringArrayProperty" | ["one", "two"]
        "nonExistentProperty" | []
    }

    def "get as list with default value"() {
        expect:
        configuration.getAsList(propertyName, ["defaultValue"]) == result

        where:
        propertyName          | result
        "stringProperty"      | ["value"]
        "nonExistentProperty" | ["defaultValue"]
    }
}
