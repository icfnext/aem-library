package com.icfolson.aem.library.core.bindings

import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.scripting.api.BindingsValuesProvider
import org.osgi.service.component.annotations.Component

import javax.script.Bindings

import static org.apache.sling.api.scripting.SlingBindings.REQUEST

@Component(service = BindingsValuesProvider, immediate = true, property = [
    "javax.script.name=sightly",
    "javax.script.name=jsp",
    "service.ranking:Integer=9999"
])
final class WCMModeBindingValuesProvider implements BindingsValuesProvider {

    @Override
    void addBindings(Bindings bindings) {
        def slingRequest = bindings.get(REQUEST) as SlingHttpServletRequest

        bindings.putAll(new WCMModeBindings(slingRequest))
    }
}
