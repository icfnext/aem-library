package com.icfolson.aem.library.core.rollout.impl

import com.adobe.granite.offloading.api.OffloadingJobProperties
import com.day.cq.commons.jcr.JcrUtil
import groovy.util.logging.Slf4j
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Reference
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceResolverFactory
import org.apache.sling.event.jobs.Job
import org.apache.sling.event.jobs.consumer.JobConsumer

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.Value

@Component
@Service(value = JobConsumer.class)
@Property(name = JobConsumer.PROPERTY_TOPICS, value = RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED_JOB_TOPIC)
@Slf4j('LOG')
class RolloutInheritanceJobConsumer implements JobConsumer {

	@Reference
	private ResourceResolverFactory resourceResolverFactory

	private Session session

	@Override
	JobConsumer.JobResult process(Job job) {
		JobConsumer.JobResult result = JobConsumer.JobResult.FAILED

		String path = (String) job.getProperty(OffloadingJobProperties.INPUT_PAYLOAD.propertyName())
		ResourceResolver resourceResolver
		try {
			resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null)
			session = resourceResolver.adaptTo(Session)

			Set<String> beforeValues = getDeepProperties((Value[]) job.getProperty('beforeValue'))
			Set<String> afterValues = getDeepProperties((Value[]) job.getProperty('afterValue'))

			// remove duplicates
			afterValues.each { afterValue ->
				if (beforeValues.contains(afterValue)) {
					beforeValues.remove(afterValue)
					afterValues.remove(afterValue)
				}
			}

			String parentPath = path[0..(-1 * RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED_PATH.length())]
			session.refresh(true)
			Node pageContentNode = session.getNode(parentPath)

			afterValues.each { afterValue ->
				addOrRemoveProp(pageContentNode, afterValue, true)
			}

			beforeValues.each { beforeValue ->
				addOrRemoveProp(pageContentNode, beforeValue, false)
			}

			result = JobConsumer.JobResult.OK
		} catch (Throwable t) {
			LOG.error('Failed to rollout deep inheritance {}', path, t)
		} finally {
			if (session != null) {
				session.logout()
			}
			if (resourceResolver && resourceResolver.live) {
				resourceResolver.close()
			}
		}

		return result
	}

	private Set<String> getDeepProperties(Value[] values) throws RepositoryException {
		Set<String> deepProps = new HashSet<String>()
		if (values != null) {
			values.each { value ->
				String property = value.string
				if (!property.startsWith('/') && property.lastIndexOf('/') > 0) {
					deepProps.add(property)
				}
			}
		}
		return deepProps
	}

	private Value[] getPropertyValues(Node node, String propertyName) throws RepositoryException {
		Value[] values = []
		if (node.hasProperty(propertyName)) {
			javax.jcr.Property prop = node.getProperty(propertyName)

			if (prop.isMultiple()) {
				values = prop.values
			} else {
				values = [prop.value]
			}
		}

		return values
	}

	private void addOrRemoveProp(Node pageContentNode, String propertyValue, boolean addToCancel) throws RepositoryException {
		int propertyIndex = propertyValue.lastIndexOf('/')
		String childNodePath = propertyValue.substring(0, propertyIndex)
		String childPropName = propertyValue.substring(propertyIndex + 1)

		LOG.debug("{} property '{}' for {} on child node '{}'", (addToCancel ? "Adding" : "Removing"), childPropName, RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, childNodePath)

		// Create the child node if necessary
		Node childNode = JcrUtil.createPath(pageContentNode, childNodePath, false, 'nt:unstructured', 'nt:unstructured', session, false)

		Value[] existingChildCancelInheritanceValues = getPropertyValues(childNode, RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED)

		List<String> updatedChildCancelInheritanceValues = new ArrayList<String>()
		existingChildCancelInheritanceValues.each { Value existingCancelInheritanceValue ->
			String existingCancelInheritanceString = existingCancelInheritanceValue.string
			if (!existingCancelInheritanceString == childPropName) {
				updatedChildCancelInheritanceValues.add(existingCancelInheritanceString)
			}
		}

		if (addToCancel) {
			updatedChildCancelInheritanceValues.add(childPropName)
		}

		if (updatedChildCancelInheritanceValues.size() != existingChildCancelInheritanceValues.length) {
			if (updatedChildCancelInheritanceValues.size() > 0) {
				childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, updatedChildCancelInheritanceValues.toArray(new String[updatedChildCancelInheritanceValues.size()]))
			} else {
				childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, (String[]) null)
			}
			session.save()
		}
	}

}
