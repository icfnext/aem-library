package com.icfolson.aem.library.core.rollout.impl

import com.adobe.granite.offloading.api.OffloadingJobProperties
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Activate
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Deactivate
import org.apache.felix.scr.annotations.Modified
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceResolverFactory
import org.apache.sling.event.jobs.JobManager
import org.apache.sling.settings.SlingSettingsService

import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.Value
import javax.jcr.observation.Event
import javax.jcr.observation.EventIterator
import javax.jcr.observation.EventListener
import javax.jcr.observation.ObservationManager

@Component(metatype = true, immediate = true)
@Service
@Slf4j('LOG')
class RolloutInheritanceEventListener implements EventListener {

	static final String PROPERTY_INHERITANCE_CANCELLED = 'cq:propertyInheritanceCancelled'
	static final String PROPERTY_INHERITANCE_CANCELLED_PATH = "/$PROPERTY_INHERITANCE_CANCELLED"

	static final String PROPERTY_INHERITANCE_CANCELLED_JOB_TOPIC = 'com/icfolson/aem/library/core/services/job'

	static final String EVENT_BEFORE_VALUE = 'beforeValue'
	static final String EVENT_AFTER_VALUE = 'afterValue'

	@Reference
	private ResourceResolverFactory resourceResolverFactory

	@Reference
	private SlingSettingsService slingSettings

	@Reference
	private JobManager jobManager

	@Property(label = 'Path root')
	private static final String PATH_ROOT = 'pathRoot'

	private String pathRoot

	private ResourceResolver resourceResolver

	private Session session

	private ObservationManager observationManager

	@Override
	void onEvent(EventIterator eventIterator) {
		if (slingSettings.runModes.contains('author')) {
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
	void activate(final Map<String, Object> properties) {
		pathRoot = properties.get(PATH_ROOT)
		if (pathRoot) {
			resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null)
			session = resourceResolver.adaptTo(Session)
			observationManager = session.workspace.observationManager

			int eventTypes = Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED | Event.PROPERTY_REMOVED
			String[] nodeTypeNames = ['cq:PageContent']
			observationManager.addEventListener(this, eventTypes, pathRoot, true, null, nodeTypeNames, true)
		}
	}

	@Deactivate
	void deactivate() {
		try {
			if (observationManager != null) {
				observationManager.removeEventListener(this)
			}
		} catch (RepositoryException re) {
			LOG.error('Error deactivating RolloutInheritanceEventListener', re)
		} finally {
			if (session != null) {
				session.logout()
			}
		}
	}

	private Map<String, Object> createPayload(Event event) {
		final Map<String, Object> payload = new HashMap<String, Object>()
		payload.put(OffloadingJobProperties.INPUT_PAYLOAD.propertyName(), event.path)
		payload.put(EVENT_BEFORE_VALUE, ((Value[]) event.info.get(EVENT_BEFORE_VALUE)).join(','))
		payload.put(EVENT_AFTER_VALUE, ((Value[]) event.info.get(EVENT_AFTER_VALUE)).join(','))
		payload
	}

}
