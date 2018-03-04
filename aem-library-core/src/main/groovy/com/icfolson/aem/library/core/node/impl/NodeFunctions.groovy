package com.icfolson.aem.library.core.node.impl

import com.google.common.base.Function
import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.node.ComponentNode
import org.apache.sling.api.resource.Resource

final class NodeFunctions {

    public static final Function<Resource, BasicNode> RESOURCE_TO_BASIC_NODE = new Function<Resource, BasicNode>() {
        @Override
        BasicNode apply(Resource resource) {
            new DefaultBasicNode(resource)
        }
    }

    public static final Function<Resource, ComponentNode> RESOURCE_TO_COMPONENT_NODE = new Function<Resource, ComponentNode>() {
        @Override
        ComponentNode apply(Resource resource) {
            new DefaultComponentNode(resource)
        }
    }

    private NodeFunctions() {

    }
}
