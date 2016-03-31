package com.icfolson.aem.library.core.adapter

import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.node.impl.DefaultBasicNode
import com.icfolson.aem.library.core.page.impl.DefaultPageDecorator
import com.icfolson.aem.library.core.page.impl.DefaultPageManagerDecorator
import com.day.cq.wcm.api.Page
import com.icfolson.aem.library.core.node.impl.DefaultComponentNode
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.Properties
import org.apache.felix.scr.annotations.Property
import org.apache.felix.scr.annotations.Service
import org.apache.sling.api.adapter.AdapterFactory
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.osgi.framework.Constants

@Component
@Service(AdapterFactory)
@Properties([
    @Property(name = Constants.SERVICE_DESCRIPTION, value = "AEM Library Adapter Factory")
])
final class AemLibraryAdapterFactory implements AdapterFactory {

    @Property(name = AdapterFactory.ADAPTABLE_CLASSES)
    public static final String[] ADAPTABLE_CLASSES = [
        "org.apache.sling.api.resource.Resource",
        "org.apache.sling.api.resource.ResourceResolver"
    ]

    @Property(name = AdapterFactory.ADAPTER_CLASSES)
    public static final String[] ADAPTER_CLASSES = [
        "com.icfolson.aem.library.api.page.PageManagerDecorator",
        "com.icfolson.aem.library.api.page.PageDecorator",
        "com.icfolson.aem.library.api.node.ComponentNode",
        "com.icfolson.aem.library.api.node.BasicNode"
    ]

    @Override
    <AdapterType> AdapterType getAdapter(Object adaptable, Class<AdapterType> type) {
        def result

        if (adaptable instanceof ResourceResolver) {
            result = getResourceResolverAdapter(adaptable, type)
        } else if (adaptable instanceof Resource) {
            result = getResourceAdapter(adaptable, type)
        } else {
            result = null
        }

        result
    }

    private static <AdapterType> AdapterType getResourceResolverAdapter(ResourceResolver resourceResolver,
        Class<AdapterType> type) {
        def result = null

        if (type == PageManagerDecorator) {
            result = new DefaultPageManagerDecorator(resourceResolver) as AdapterType
        }

        result
    }

    private static <AdapterType> AdapterType getResourceAdapter(Resource resource, Class<AdapterType> type) {
        def result = null

        if (type == PageDecorator) {
            def page = resource.adaptTo(Page)

            if (page) {
                result = new DefaultPageDecorator(page) as AdapterType
            }
        } else if (type == ComponentNode) {
            result = new DefaultComponentNode(resource) as AdapterType
        } else if (type == BasicNode) {
            result = new DefaultBasicNode(resource) as AdapterType
        }

        result
    }
}
