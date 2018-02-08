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
import javax.jcr.PropertyIterator
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

			String jcrContentPath

			jcrContentPath = path[0..(-1 * RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED_PATH.length())]
			if (jcrContentPath) {
				session.refresh(true)
				Node pageContentNode = session.getNode(jcrContentPath)

				Set<String> beforeValues = getDeepProperties(job.getProperty(RolloutInheritanceEventListener.EVENT_BEFORE_VALUE).split(','), pageContentNode)
				Set<String> afterValues = getDeepProperties(job.getProperty(RolloutInheritanceEventListener.EVENT_AFTER_VALUE).split(','), pageContentNode)

				// remove common values
				Set<String> commonValues = beforeValues.intersect(afterValues)
				Set<String> uniqueBeforeValues = beforeValues - commonValues
				Set<String> uniqueAfterValues = afterValues - commonValues

				uniqueAfterValues.each { afterValue ->
					addOrRemoveProp(pageContentNode, afterValue, true)
				}

				uniqueBeforeValues.each { beforeValue ->
					addOrRemoveProp(pageContentNode, beforeValue, false)
				}

				result = JobConsumer.JobResult.OK
			}
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

	private Set<String> getDeepProperties(String[] values, Node pageContentNode) throws RepositoryException {
		Set<String> deepProps = new HashSet<String>()
		if (values != null) {
			values.each { value ->
				if (!value.startsWith('/') && value.lastIndexOf('/') > 0) {
					deepProps.add(value)
				} else if (pageContentNode.hasNode(value)) {
					Node propertyNode = pageContentNode.getNode(value)
					propertyNode.recurse { Node childNode ->
						String childNodePath = childNode.path.substring(pageContentNode.path.length() + 1)
						PropertyIterator propertyIterator = childNode.properties
						while (propertyIterator.hasNext()) {
							javax.jcr.Property property = propertyIterator.nextProperty()
							String propertyName = property.name
							if (!propertyName.startsWith('jcr:') && !propertyName.startsWith('cq:')) {
								String deepPropertyPath = "${childNodePath}/${propertyName}"
								deepProps.add(deepPropertyPath)
							}
						}
					}
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

		LOG.debug("${(addToCancel ? 'Adding' : 'Removing')} property ${childPropName} for ${RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED} on child node ${childNodePath}")

		Node childNode
		if (pageContentNode.hasNode(childNodePath)) {
			childNode = pageContentNode.getNode(childNodePath)
		} else {
			childNode = JcrUtil.createPath(pageContentNode, childNodePath, false, 'nt:unstructured', 'nt:unstructured', session, true)
		}

		Value[] existingChildCancelInheritanceValues = getPropertyValues(childNode, RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED)

		List<String> updatedChildCancelInheritanceValues = new ArrayList<String>()
		existingChildCancelInheritanceValues.each { Value existingCancelInheritanceValue ->
			String existingCancelInheritanceString = existingCancelInheritanceValue.string
			if (existingCancelInheritanceString != childPropName) {
				updatedChildCancelInheritanceValues.add(existingCancelInheritanceString)
			}
		}

		if (addToCancel) {
			updatedChildCancelInheritanceValues.add(childPropName)
		}

		if (updatedChildCancelInheritanceValues.sort() != existingChildCancelInheritanceValues.sort()) {
			if (updatedChildCancelInheritanceValues.size() > 0) {
				childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, updatedChildCancelInheritanceValues.toArray(new String[updatedChildCancelInheritanceValues.size()]))
			} else {
				childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, (String[]) null)
			}
			session.save()
		}
	}

}
