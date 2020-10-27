package com.icfolson.aem.library.core.rollout.impl

import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceResolverFactory
import org.apache.sling.event.jobs.JobManager
import org.apache.sling.settings.SlingSettingsService
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Deactivate
import org.osgi.service.component.annotations.Modified
import org.osgi.service.component.annotations.Reference
import org.osgi.service.metatype.annotations.Designate

import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.Value
import javax.jcr.observation.Event
import javax.jcr.observation.EventIterator
import javax.jcr.observation.EventListener
import javax.jcr.observation.ObservationManager

@Component(service = EventListener)
@Designate(ocd = RolloutInheritanceEventListenerConfiguration)
@Slf4j("LOG")
class RolloutInheritanceEventListener implements EventListener {

    static final String PROPERTY_INHERITANCE_CANCELLED = "cq:propertyInheritanceCancelled"
    static final String PROPERTY_INHERITANCE_CANCELLED_PATH = "/$PROPERTY_INHERITANCE_CANCELLED"

    static final String PROPERTY_INHERITANCE_CANCELLED_JOB_TOPIC = "com/icfolson/aem/library/core/services/job"

    static final String EVENT_BEFORE_VALUE = "beforeValue"
    static final String EVENT_AFTER_VALUE = "afterValue"
    static final String EVENT_INPUT_IDENTIFIER = "offloading.input.payload"

    static final Integer EVENT_TYPES = Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED
    static final String[] NODE_TYPE_NAMES = ["cq:PageContent"]

    @Reference
    private ResourceResolverFactory resourceResolverFactory

    @Reference
    private SlingSettingsService slingSettings

    @Reference
    private JobManager jobManager

    private ResourceResolver resourceResolver

    private Session session

    private ObservationManager observationManager

    @Override
    void onEvent(EventIterator eventIterator) {
        if (slingSettings.runModes.contains("author")) {
            while (eventIterator.hasNext()) {
                Event event = eventIterator.nextEvent()
                if (event.path.endsWith(PROPERTY_INHERITANCE_CANCELLED_PATH)) {
                    jobManager.addJob(PROPERTY_INHERITANCE_CANCELLED_JOB_TOPIC, createPayload(event))
                }
            }
        }
    }

    @Activate
    @Modified
    void activate(RolloutInheritanceEventListenerConfiguration configuration) {
        if (configuration.pathRoot()) {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(null)
            session = resourceResolver.adaptTo(Session)

            observationManager = session.workspace.observationManager
            observationManager.addEventListener(this, EVENT_TYPES, configuration.pathRoot(), true, null,
                NODE_TYPE_NAMES, true)
        }
    }

    @Deactivate
    void deactivate() {
        try {
            observationManager?.removeEventListener(this)
        } catch (RepositoryException re) {
            LOG.error("Error deactivating RolloutInheritanceEventListener", re)
        } finally {
            resourceResolver?.close()
        }
    }

    private Map<String, Object> createPayload(Event event) {
        def payload = [:]

        payload.put(EVENT_INPUT_IDENTIFIER, event.path)
        payload.put(EVENT_BEFORE_VALUE, ((Value[]) event.info.get(EVENT_BEFORE_VALUE))?.join(",") ?: "")
        payload.put(EVENT_AFTER_VALUE, ((Value[]) event.info.get(EVENT_AFTER_VALUE))?.join(",") ?: "")

        payload
    }
}
