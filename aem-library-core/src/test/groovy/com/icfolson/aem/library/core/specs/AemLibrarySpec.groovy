package com.icfolson.aem.library.core.specs

import com.day.cq.wcm.api.Page
import com.day.cq.wcm.api.PageManager
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.core.adapter.AemLibraryAdapterFactory
import com.icfolson.aem.prosper.specs.ProsperSpec

/**
 * Spock specification for testing AEM Library-based components and services.
 */
abstract class AemLibrarySpec extends ProsperSpec {

    def setupSpec() {
        slingContext.registerAdapterFactory(new AemLibraryAdapterFactory())
    }

    ComponentNode getComponentNode(String path) {
        resourceResolver.getResource(path).adaptTo(ComponentNode)
    }

    @Override
    Page getPage(String path) {
        pageManager.getPage(path)
    }

    @Override
    PageManager getPageManager() {
        resourceResolver.adaptTo(PageManagerDecorator)
    }
}
