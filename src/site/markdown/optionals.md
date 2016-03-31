## Optionals

Many of the component and page decorator methods return an `Optional` wrapper for nullable values.  See the [Google Guava user guide](https://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained#Optional) for a more thorough explanation, but here are a few examples of `Optional` usage in the AEM context:

    String href = getAsHref("path").or("#");
    String redirectHref = getAsHref("redirectPath").or(currentPage.getHref());

    Optional<PageDecorator> pageOptional = getAsPage("pagePath");

    if (pageOptional.isPresent()) {
        PageDecorator page = pageOptional.get();
        String title = page.getNavigationTitleOptional().or(page.getTitle());

        ...
    }

The [Javadoc](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/base/Optional.html) has the complete list of available methods.