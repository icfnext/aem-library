package com.icfolson.aem.library.core.page.impl;

import com.day.cq.commons.RangeIterator;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.base.Predicate;
import com.google.common.base.Stopwatch;
import com.icfolson.aem.library.api.page.PageDecorator;
import com.icfolson.aem.library.api.page.PageManagerDecorator;
import com.icfolson.aem.library.core.page.predicates.TemplatePredicate;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.RowIterator;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class DefaultPageManagerDecorator implements PageManagerDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPageManagerDecorator.class);

    private final ResourceResolver resourceResolver;

    private final PageManager pageManager;

    DefaultPageManagerDecorator(ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;

        pageManager = resourceResolver.adaptTo(PageManager.class);
    }

    @Override
    public PageManager getPageManager() {
        return pageManager;
    }

    @Override
    public List<PageDecorator> findPages(final String rootPath, final Collection<String> tagIds, final boolean matchOne) {
        checkNotNull(rootPath);
        checkNotNull(tagIds);

        LOG.debug("path = {}, tag IDs = {}", rootPath, tagIds);

        final Stopwatch stopwatch = Stopwatch.createStarted();

        final RangeIterator<Resource> iterator = resourceResolver.adaptTo(TagManager.class).find(rootPath,
                tagIds.toArray(new String[0]), matchOne);

        final List<PageDecorator> pages = new ArrayList<>();

        while (iterator.hasNext()) {
            final Resource resource = iterator.next();

            if (JcrConstants.JCR_CONTENT.equals(resource.getName())) {
                final PageDecorator page = getPage(resource.getParent().getPath());

                if (page != null) {
                    pages.add(page);
                }
            }
        }

        LOG.debug("found {} result(s) in {}ms", pages.size(), stopwatch.elapsed(MILLISECONDS));

        return pages;
    }

    @Override
    public List<PageDecorator> findPages(final String rootPath, final String template) {
        return findPages(rootPath, new TemplatePredicate(template));
    }

    @Override
    public List<PageDecorator> findPages(final String rootPath, final Predicate<PageDecorator> predicate) {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        final List<PageDecorator> pages = Optional.ofNullable(getPage(checkNotNull(rootPath)))
                .map(page -> page.findDescendants(predicate))
                .orElse(Collections.emptyList());

        stopwatch.stop();

        LOG.debug("found {} result(s) in {}ms", pages.size(), stopwatch.elapsed(MILLISECONDS));

        return pages;
    }

    @Override
    public List<PageDecorator> search(final Query query) {
        return search(query, -1);
    }

    @Override
    public List<PageDecorator> search(final Query query, final int limit) {
        checkNotNull(query);

        LOG.debug("query statement : {}", query.getStatement());

        final Stopwatch stopwatch = Stopwatch.createStarted();

        final List<PageDecorator> pages = new ArrayList<>();

        int count = 0;

        try {
            final Set<String> paths = new HashSet<>();

            final RowIterator rows = query.execute().getRows();

            while (rows.hasNext()) {
                final String path = rows.nextRow().getPath();

                if (limit == -1 || count < limit) {
                    LOG.debug("result path : {}", path);

                    final PageDecorator page = getContainingPage(path);

                    if (page != null) {
                        // ensure no duplicate pages are added
                        if (!paths.contains(page.getPath())) {
                            paths.add(page.getPath());
                            pages.add(page);
                            count++;
                        }
                    } else {
                        LOG.error("result is null for path : {}", path);
                    }
                }
            }

            stopwatch.stop();

            LOG.debug("found {} result(s) in {}ms", pages.size(), stopwatch.elapsed(MILLISECONDS));
        } catch (RepositoryException re) {
            LOG.error("error finding pages for query : {}", query.getStatement(), re);
        }

        return pages;
    }

    @Override
    public PageDecorator getContainingPage(final Resource resource) {
        return getPage(pageManager.getContainingPage(resource));
    }

    @Override
    public PageDecorator getContainingPage(final String path) {
        return getPage(pageManager.getContainingPage(path));
    }

    @Override
    public PageDecorator getPage(final Page page) {
        return Optional.ofNullable(page)
                .map(p -> p.adaptTo(PageDecorator.class))
                .orElse(null);
    }

    @Override
    public PageDecorator getPage(final String path) {
        return getPage(pageManager.getPage(path));
    }
}

