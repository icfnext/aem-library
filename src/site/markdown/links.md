## Links

The `Link` object encapsulates the properties of an HTML link, including the decomposition of the URL "parts" according to Sling (i.e. path, selectors, extension).  A `Link` or collection of Links can be returned from a component bean to represent a navigation structure, for example.  The `LinkBuilder` class provides numerous methods to build an immutable `Link`, `ImageLink`, or `NavigationLink` instance.  The builder itself is acquired through one of several static factory methods that accept a `Page`, `Resource`, or JCR path value.

### Image Link

A `Link` with the addition of an image source property.

### Navigation Link

A `Link` containing a list of child links.  A recursively-built list of navigation links can be used to represent a hierarchical navigation structure.