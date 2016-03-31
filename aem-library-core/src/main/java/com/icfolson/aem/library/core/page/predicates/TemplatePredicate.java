package com.icfolson.aem.library.core.page.predicates;

import com.icfolson.aem.library.api.page.PageDecorator;
import com.google.common.base.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Predicate that filters on the value of the page's cq:template property.
 */
public final class TemplatePredicate implements Predicate<PageDecorator> {

    private final String templatePath;

    public TemplatePredicate(final PageDecorator page) {
        checkNotNull(page);

        templatePath = page.getTemplatePath();
    }

    public TemplatePredicate(final String templatePath) {
        checkNotNull(templatePath);

        this.templatePath = templatePath;
    }

    @Override
    public boolean apply(final PageDecorator page) {
        return templatePath.equals(page.getTemplatePath());
    }
}
