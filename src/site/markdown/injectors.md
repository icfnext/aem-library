## Sling Models Injectors

Bedrock provides several custom Sling Models injectors to facilitate injection of Bedrock-specific context objects.  Injector services are registered by default when the Bedrock Models bundle is installed, so `@Inject` annotated fields in model classes will be handled by the injectors below in addition to the default set of injectors provided by Sling. 

### Available Injectors

Title | Name | Service Ranking | Description 
:-----|:-----|:----------------|:-----------
Resource Resolver Adaptable Injector | adaptable | `Integer.MIN_VALUE` | Injects objects that can be adapted from `ResourceResolver`, e.g. `PageManagerDecorator`
Component Content Injector | component | `Integer.MAX_VALUE` | Injects objects derived from the current component context
Enum Injector | enum | 4000 | Injects an enum for the property value matching the annotated field name
Image Injector | images | 4000 |  Injects `com.day.cq.wcm.foundation.Image` from the current resource
Inherit Injector | inherit | 4000 | Injects a property that inherits from ancestor pages if it isn't found on the current resource
Link Injector | links | 4000 | Injects `com.icfolson.aem.library.api.link.Link` derived from the property value for the current field name
Model List Injector | model-list | 999 | Injects a list of models from adapted from the child of a named child resource
Reference Injector | references | 4000 | Injects a resource or object adapted from a resource based on the value of a property
Tag Injector | tags | 800 | Resolves a `com.day.cq.tagging.Tag` object (or list of tags) for the given property
ValueMap Injector | valuemap | 2500 |  Injects a property value with the given type from a `ValueMap`

### Injector-specific Annotations

Annotation | Supported Optional Elements | Injector
:----------|:----------------------------|:--------
`@ImageInject` | `injectionStrategy, isSelf, inherit, selectors` | images
`@InheritInject` | `injectionStrategy` | inherit
`@LinkInject` | `injectionStrategy, titleProperty, inherit` | links
`@ReferenceInject` | `injectionStrategy, inherit` | references
`@TagInject` | `injectionStrategy, inherit` | tags