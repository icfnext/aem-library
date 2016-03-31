## Adapters

Sling `Resource` and `ResourceResolver` instances are adaptable to AEM Library types as outlined below.

### Resource

Adapter | Details
:-------|:-----
PageDecorator | Only applies when the `Resource` path is a valid page path, returns null otherwise.
BasicNode | Applies to all `Resource` instances.
ComponentNode | Applies to all `Resource` instances.

### ResourceResolver

Adapter | Details
:-------|:-----
PageManagerDecorator | Applies to all `ResourceResolver` instances.