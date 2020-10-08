package com.icfolson.aem.library.core.rollout.impl

import com.day.cq.commons.jcr.JcrUtil
import groovy.util.logging.Slf4j
import org.apache.sling.api.resource.ResourceResolverFactory
import org.apache.sling.event.jobs.Job
import org.apache.sling.event.jobs.consumer.JobConsumer
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference

import javax.jcr.Node
import javax.jcr.RepositoryException
import javax.jcr.Session
import javax.jcr.Value
import javax.jcr.util.TraversingItemVisitor

import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED
import static com.icfolson.aem.library.core.rollout.impl.RolloutInheritanceEventListener.EVENT_AFTER_VALUE
import static com.icfolson.aem.library.core.rollout.impl.RolloutInheritanceEventListener.EVENT_BEFORE_VALUE
import static com.icfolson.aem.library.core.rollout.impl.RolloutInheritanceEventListener.EVENT_INPUT_IDENTIFIER

@Component(service = JobConsumer, property = [
    "job.topics=com/icfolson/aem/library/core/services/job"
])
@Slf4j("LOG")
class RolloutInheritanceJobConsumer implements JobConsumer {

    @Reference
    private ResourceResolverFactory resourceResolverFactory

    @Override
    JobResult process(Job job) {
        def result = JobResult.FAILED

        def path = (String) job.getProperty(EVENT_INPUT_IDENTIFIER)

        def resourceResolver = null
        def session = null

        try {
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(null)
            session = resourceResolver.adaptTo(Session)

            def jcrContentPath = path[0..(-1 * RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED_PATH.length())]

            if (jcrContentPath) {
                session.refresh(true)

                def pageContentNode = session.getNode(jcrContentPath)

                def beforeValues = getDeepProperties(getPropertyValues(job, EVENT_BEFORE_VALUE), pageContentNode)
                def afterValues = getDeepProperties(getPropertyValues(job, EVENT_AFTER_VALUE), pageContentNode)

                // remove common values
                Set<String> commonValues = beforeValues.intersect(afterValues as Iterable)
                Set<String> uniqueBeforeValues = beforeValues - commonValues
                Set<String> uniqueAfterValues = afterValues - commonValues

                uniqueAfterValues.each { afterValue ->
                    addOrRemoveProp(session, pageContentNode, afterValue, true)
                }

                uniqueBeforeValues.each { beforeValue ->
                    addOrRemoveProp(session, pageContentNode, beforeValue, false)
                }

                result = JobResult.OK
            }
        } catch (Throwable t) {
            LOG.error("Failed to rollout deep inheritance ${path}", t)
        } finally {
            session?.logout()

            if (resourceResolver && resourceResolver.live) {
                resourceResolver.close()
            }
        }

        result
    }

    private Set<String> getDeepProperties(String[] values, Node pageContentNode) throws RepositoryException {
        def deepProps = new HashSet<String>()

        if (values) {
            values.each { value ->
                if (!value.startsWith("/") && value.lastIndexOf("/") > 0) {
                    deepProps.add(value)
                } else if (value && pageContentNode.hasNode(value)) {
                    def propertyNode = pageContentNode.getNode(value)

                    def visitor = new TraversingItemVisitor.Default(false) {
                        @Override
                        protected void entering(Node node, int level) throws RepositoryException {
                            def childNodePath = node.path.substring(pageContentNode.path.length() + 1)
                            def propertyIterator = node.properties

                            while (propertyIterator.hasNext()) {
                                def property = propertyIterator.nextProperty()
                                def propertyName = property.name

                                if (!propertyName.startsWith("jcr:") && !propertyName.startsWith("cq:")) {
                                    deepProps.add("${childNodePath}/${propertyName}")
                                }
                            }
                        }
                    }

                    visitor.visit(propertyNode)
                }
            }
        }

        deepProps
    }

    private String[] getPropertyValues(Job job, String propertyName) {
        (job.getProperty(propertyName) as String).split(",")
    }

    private Value[] getPropertyValues(Node node, String propertyName) throws RepositoryException {
        Value[] values = []

        if (node.hasProperty(propertyName)) {
            def prop = node.getProperty(propertyName)

            if (prop.multiple) {
                values = prop.values
            } else {
                values = [prop.value]
            }
        }

        values
    }

    private void addOrRemoveProp(Session session, Node pageContentNode, String propertyValue, boolean addToCancel)
        throws RepositoryException {
        int propertyIndex = propertyValue.lastIndexOf("/")
        String childNodePath = propertyValue.substring(0, propertyIndex)
        String childPropName = propertyValue.substring(propertyIndex + 1)

        LOG.debug("${(addToCancel ? "Adding" : "Removing")} property ${childPropName} " +
            "for ${RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED} on child node ${childNodePath}")

        Node childNode

        if (pageContentNode.hasNode(childNodePath)) {
            childNode = pageContentNode.getNode(childNodePath)
        } else {
            childNode = JcrUtil.createPath(pageContentNode, childNodePath, false, NT_UNSTRUCTURED, NT_UNSTRUCTURED,
                session, true)
        }

        def existingChildCancelInheritanceValues = getPropertyValues(childNode,
            RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED)

        List<String> updatedChildCancelInheritanceValues = []

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
                childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED,
                    updatedChildCancelInheritanceValues.toArray(new String[updatedChildCancelInheritanceValues.size()]))
            } else {
                childNode.setProperty(RolloutInheritanceEventListener.PROPERTY_INHERITANCE_CANCELLED, (String[]) null)
            }

            session.save()
        }
    }
}
