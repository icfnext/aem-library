package com.icfolson.aem.library.core.components

import com.icfolson.aem.library.api.node.ComponentNode
import com.fasterxml.jackson.annotation.JsonAutoDetect

import javax.inject.Inject

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE

/**
 * Base class for AEM component classes instantiated by the {@link com.icfolson.aem.library.core.tags.ComponentTag}
 * or implemented with Sightly.
 */
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, isGetterVisibility = NONE)
abstract class AbstractComponent implements ComponentNode {

    /**
     * Component node delegate.
     */
    @Inject
    @Delegate
    ComponentNode delegate
}
