package com.icfolson.aem.library.core.specs

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.citytechinc.aem.prosper.specs.ProsperSpec
import com.icfolson.aem.library.core.adapter.AemLibraryAdapterFactory

/**
 * Spock specification for testing AEM Library-based components and services.
 */
abstract class AemLibrarySpec extends ProsperSpec {

    def setupSpec() {
        slingContext.registerAdapterFactory(new AemLibraryAdapterFactory(), AemLibraryAdapterFactory.ADAPTABLE_CLASSES,
            AemLibraryAdapterFactory.ADAPTER_CLASSES)
    }

    ComponentNode getComponentNode(String path) {
        resourceResolver.getResource(path).adaptTo(ComponentNode)
    }

    @Override
    PageDecorator getPage(String path) {
        pageManager.getPage(path)
    }

    @Override
    PageManagerDecorator getPageManager() {
        resourceResolver.adaptTo(PageManagerDecorator)
    }
}
