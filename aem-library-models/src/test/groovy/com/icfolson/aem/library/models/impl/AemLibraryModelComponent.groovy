package com.icfolson.aem.library.models.impl

import com.icfolson.aem.library.core.components.AbstractComponent
import org.apache.sling.api.resource.Resource
import org.apache.sling.models.annotations.Model

@Model(adaptables = Resource)
class AemLibraryModelComponent extends AbstractComponent {

    String getTitle() {
        get("jcr:title", "")
    }
}
