package com.icfolson.aem.library.core.page.impl;

import com.icfolson.aem.library.api.page.PageDecorator;
import org.apache.sling.api.resource.Resource;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/*
public class PageDecoratorIterator  implements Iterator<PageDecorator> {

    private PageDecorator nextPage;

    private final Iterator<Resource> base;

    private final Predicate<PageDecorator> predicate;

    PageDecoratorIterator(final Iterator<Resource> base, final Predicate<PageDecorator> predicate) {
        this.base = base;
        this.predicate = predicate;

        seek();
    }

    @Override
    public boolean hasNext() {
        return nextPage != null;
    }

    @Override
    public PageDecorator next() {
        if (nextPage != null) {
            return seek();
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private PageDecorator seek() {
        PageDecorator prev = nextPage;

        nextPage = null;

        while (base.hasNext() && nextPage == null) {
            final Resource resource = base.next();

            nextPage = resource.adaptTo(PageDecorator.class);

            if (nextPage != null && predicate != null && !predicate.test(nextPage)) {
                nextPage = null;
            }
        }

        return prev;
    }
}

 */
