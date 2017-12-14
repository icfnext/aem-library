## Component Framework

### Overview

The introduction of the HTL templating language has eliminated the need for custom JSP tags, scriptlets, and other unpleasantries when separating a component's view from it's supporting business logic.  The [Sling Models](https://sling.apache.org/documentation/bundles/models.html) framework offers a robust, POJO-based development pattern that the AEM Library augments to greatly simplify AEM component development.

For projects with JSP-based components, the AEM Library maintains a custom JSP tag to mimic HTL's "use" functionality for associating a Java/Groovy class with a component JSP.

### Abstract Component Class

Model classes may extend the `com.icfolson.aem.library.core.components.AbstractComponent` class to expose numerous convenience methods for retrieving/transforming property values, traversing the content repository, and generally reducing the amount of boilerplate code needed to perform common node- and property-based operations for a component.

The Java/Groovy model class for the component should expose getters for the values that required to render the component's view.

    package com.projectname.components.content

    import com.icfolson.aem.library.core.components.AbstractComponent
    import com.icfolson.aem.library.api.content.page.PageDecorator
    import org.apache.sling.models.annotations.Model
   
	@Model(adaptables = [Resource, SlingHttpServletRequest])
    class Navigation extends AbstractComponent {
    
        @Inject
        PageDecorator currentPage

        String getTitle() {
            get("jcr:title", "")
        }

        List<PageDecorator> getPages() {
            currentPage.getChildren(true)
        }
    } 

### Injectable Component Node

Alternatively, model classes may inject an instance of the `com.icfolson.aem.library.api.node.ComponentNode` class to provide the same functionality as the abstract class described above.

    import com.icfolson.aem.library.api.node.ComponentNode
    
    @Model(adaptables = [Resource, SlingHttpServletRequest])
    class Navigation {
    
        @Inject
        ComponentNode componentNode

        String getTitle() {
            componentNode.get("jcr:title", "")
        }
    }

See the `ComponentNode` [Javadoc](http://code.digitalatolson.com/aem-library/apidocs/com/icfolson/aem/library/api/node/ComponentNode.html) for details of the available methods.

### Sling Models Injectors

In addition to the AEM Library's component API, the framework also supplies a set of custom Sling Models injectors to support injection of common Sling and AEM objects for the current component.  See the [Injectors](/aem-library/injectors.html) page for additional information.

### HTL Integration

Sling Models-based components (i.e. POJOs with the `@org.apache.sling.models.annotations.Model` annotation) can be instantiated in HTL templates with a [data-sly-use](https://github.com/Adobe-Marketing-Cloud/sightly-spec/blob/master/SPECIFICATION.md#221-use) block statement.  Since the AEM Library components are just "decorated" Sling Models, nothing additional is required.

The HTL template for the preceding `Navigation` component would be implemented as follows:

    <sly data-sly-use.navigation="com.projectname.components.content.Navigation">
        <h1>${navigation.title}</h1>
    
        <ul data-sly-list.page="${navigation.pages}">
            <li><a href="${page.href}">${page.title}</a></li>
        </ul>
    </sly>

### Component Development Guidelines

* Component beans should be **read-only** since requests in publish mode are generally bound to an anonymous user without write access.  Repository write operations should be performed only in author mode (and replicated only when a page is activated by a content author).  Since component classes are executed in both author and publish modes, ideally one should consider alternative approaches to performing write operations in a component bean:
    * Delegate write operations to an OSGi service containing a service-appropriate Sling Resource Resolver.
    * Refactor the component to perform dialog-based content modifications by attaching a listener to the appropriate [dialog event](https://docs.adobe.com/docs/en/aem/6-1/ref/widgets-api/index.html?class=CQ.Dialog), e.g. 'beforesubmit'.
    * Register a [JCR event listener](http://www.day.com/maven/jsr170/javadocs/jcr-2.0/javax/jcr/observation/ObservationManager.html) to trigger event-based repository updates.
* Classes should remain stateless and contain no setters.  Since the lifecycle of a component/model is bound to a request, state should be maintained client-side using cookies, local storage, or HTML data attributes.