package com.icfolson.aem.library.core.listeners

import com.day.cq.wcm.api.NameConstants
import com.icfolson.aem.library.core.specs.AemLibrarySpec
import spock.lang.Shared

import javax.jcr.Session
import javax.jcr.observation.Event
import javax.jcr.observation.EventIterator

class AbstractPageEventListenerSpec extends AemLibrarySpec {

    static final def EVENT_PATHS = [
        "/content/home",
        "/content/home/jcr:content",
        "/content/about/jcr:content"
    ]

    class TestPageEventListener extends AbstractPageEventListener {

        def paths = []

        TestPageEventListener(Session session) {
            super(session)
        }

        @Override
        void processPage(String path) {
            paths.add(path)
        }
    }

    @Shared listener

    def setupSpec() {
        pageBuilder.content {
            home((NameConstants.NN_TEMPLATE): "template")
            about()
        }

        listener = new TestPageEventListener(session)
    }

    def "process page"() {
        setup:
        def iterator = EVENT_PATHS.collect { path ->
            Mock(Event) {
                1 * getPath() >> path
            }
        }.iterator()

        def events = [
            hasNext: {
                iterator.hasNext()
            },
            nextEvent: {
                iterator.next()
            }
        ] as EventIterator

        when:
        listener.onEvent(events)

        then:
        listener.paths == ["/content/home"]
    }
}