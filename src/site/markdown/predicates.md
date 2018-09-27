## Predicates

Predicates are typically used to filter collections; in the AEM Library, a [Predicate](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Predicate.html) can be used in `ComponentNode` and `PageDecorator` instances to filter lists of descendant nodes or pages using various criteria.

The library contains a few basic `Predicate` implementations, but predicates can also be implemented as anonymous inner classes or as `static final` instances (if they do not depend on instance variables, similar to a `Comparator`).

Using the [Google Guava](https://github.com/google/guava) API, [Predicates](https://github.com/google/guava/wiki/FunctionalExplained#predicates) can easily be chained to build complex criteria to achieve more granular filtering behavior.

    Predicate<ComponentNode> propertyExistsPredicate = new ComponentNodePropertyExistsPredicate("batman");
    Predicate<ComponentNode> resourceTypePredicate = new ComponentNodeResourceTypePredicate("components/lo-pan");

    Predicate<ComponentNode> predicate = Predicates.or(propertyExistsPredicate, resourceTypeExists);

    List<ComponentNode> filteredComponentNodes = getComponentNodes(predicate);