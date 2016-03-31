<%@include file="/libs/foundation/global.jsp"%>
<%@taglib prefix="aem-library" uri="http://www.icfolson.com/taglibs/aem-library"%>

<aem-library:defineObjects />

<c:if test="${isDebug}">
    <!-- resource path: ${resource.path} -->
    <!-- resource type: ${resource.resourceType} -->
    <!-- script: ${sling.script.scriptResource.path} -->
</c:if>