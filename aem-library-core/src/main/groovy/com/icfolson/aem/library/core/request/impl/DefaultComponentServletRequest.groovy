package com.icfolson.aem.library.core.request.impl

import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.api.request.ComponentServletRequest
import com.day.cq.wcm.api.WCMMode
import com.google.common.base.Function
import com.google.common.base.Optional
import com.google.common.collect.ImmutableList
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.request.RequestParameter
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ValueMap

import javax.jcr.Node
import javax.jcr.Session

import static com.day.cq.wcm.api.WCMMode.fromRequest
import static com.google.common.base.Preconditions.checkNotNull
import static org.apache.sling.api.resource.ValueMap.EMPTY

final class DefaultComponentServletRequest implements ComponentServletRequest {

    private static final def REQUEST_PARAMETER_TO_STRING = new Function<RequestParameter, String>() {
        @Override
        String apply(RequestParameter parameter) {
            parameter.string
        }
    }

    private static final def REQUEST_PARAMETERS_TO_LIST = new Function<RequestParameter[], List<String>>() {
        @Override
        List<String> apply(RequestParameter[] parameters) {
            ImmutableList.copyOf(parameters*.string)
        }
    }

    final SlingHttpServletRequest slingRequest

    final SlingHttpServletResponse slingResponse

    final ComponentNode componentNode

    final Node currentNode

    final PageDecorator currentPage

    final PageManagerDecorator pageManager

    final ValueMap pageProperties

    final ValueMap properties

    final Resource resource

    final ResourceResolver resourceResolver

    DefaultComponentServletRequest(SlingHttpServletRequest slingRequest, SlingHttpServletResponse slingResponse) {
        this.slingRequest = checkNotNull(slingRequest)
        this.slingResponse = checkNotNull(slingResponse)

        resource = slingRequest.resource
        resourceResolver = resource.resourceResolver
        properties = resource.valueMap
        currentNode = resource.adaptTo(Node)
        componentNode = resource.adaptTo(ComponentNode)
        pageManager = resourceResolver.adaptTo(PageManagerDecorator)
        currentPage = pageManager.getContainingPage(resource)
        pageProperties = currentPage ? currentPage.properties : EMPTY
    }

    @Override
    Optional<String> getRequestParameter(String parameterName) {
        checkNotNull(parameterName)

        Optional.fromNullable(slingRequest.getRequestParameter(parameterName)).transform(REQUEST_PARAMETER_TO_STRING)
    }

    @Override
    String getRequestParameter(String parameterName, String defaultValue) {
        checkNotNull(parameterName)
        checkNotNull(defaultValue)

        def parameter = slingRequest.getRequestParameter(parameterName)

        parameter ? parameter.string : defaultValue
    }

    @Override
    Optional<List<String>> getRequestParameters(String parameterName) {
        checkNotNull(parameterName)

        Optional.fromNullable(slingRequest.getRequestParameters(parameterName)).transform(REQUEST_PARAMETERS_TO_LIST)
    }

    @Override
    List<String> getSelectors() {
        ImmutableList.copyOf(slingRequest.requestPathInfo.selectors)
    }

    @Override
    Session getSession() {
        resourceResolver.adaptTo(Session)
    }

    @Override
    WCMMode getWCMMode() {
        fromRequest(slingRequest)
    }
}
