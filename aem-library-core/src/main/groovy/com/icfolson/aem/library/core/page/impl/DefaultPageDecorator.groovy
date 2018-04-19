package com.icfolson.aem.library.core.page.impl

import com.day.cq.commons.Filter
import com.day.cq.wcm.api.NameConstants
import com.day.cq.wcm.api.Page
import com.day.cq.wcm.commons.DeepResourceIterator
import com.google.common.base.Objects
import com.google.common.base.Optional
import com.google.common.base.Predicate
import com.google.common.base.Predicates
import com.icfolson.aem.library.api.link.ImageLink
import com.icfolson.aem.library.api.link.Link
import com.icfolson.aem.library.api.link.NavigationLink
import com.icfolson.aem.library.api.link.builders.LinkBuilder
import com.icfolson.aem.library.api.node.BasicNode
import com.icfolson.aem.library.api.node.ComponentNode
import com.icfolson.aem.library.api.page.PageDecorator
import com.icfolson.aem.library.api.page.PageManagerDecorator
import com.icfolson.aem.library.api.page.enums.TitleType
import com.icfolson.aem.library.core.link.builders.factory.LinkBuilderFactory
import com.icfolson.aem.library.core.node.predicates.ComponentNodePropertyExistsPredicate
import com.icfolson.aem.library.core.node.predicates.ComponentNodePropertyValuePredicate

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ValueMap

import static com.google.common.base.Preconditions.checkNotNull
import static com.icfolson.aem.library.core.node.impl.NodeFunctions.RESOURCE_TO_COMPONENT_NODE

final class DefaultPageDecorator implements PageDecorator {

	private static final Predicate<PageDecorator> ALL = Predicates.alwaysTrue()

	private static final Filter<Page> ALL_PAGES = new Filter<Page>() {
		@Override
		boolean includes(Page page) {
			true
		}
	}

	private static final Predicate<PageDecorator> DISPLAYABLE_ONLY = new Predicate<PageDecorator>() {
		@Override
		boolean apply(PageDecorator page) {
			page.contentResource && !page.hideInNav
		}
	}

	@Delegate
	private final Page delegate

	private final Optional<ComponentNode> componentNodeOptional

	DefaultPageDecorator(Page page) {
		this.delegate = page

		componentNodeOptional = Optional.fromNullable(page.contentResource).transform(RESOURCE_TO_COMPONENT_NODE)
	}

	@Override
	boolean equals(Object other) {
		new EqualsBuilder().append(path, (other as PageDecorator).path).equals
	}

	@Override
	int hashCode() {
		new HashCodeBuilder().append(path).hashCode()
	}

	@Override
	String toString() {
		Objects.toStringHelper(this)
				.add("path", path)
				.add("title", title)
				.toString()
	}

	@Override
	@SuppressWarnings("unchecked")
	<AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
		def result

		if (type == BasicNode || type == ComponentNode) {
			def resource = delegate.contentResource

			result = (AdapterType) resource?.adaptTo(ComponentNode)
		} else {
			result = delegate.adaptTo(type)
		}

		result
	}

	@Override
	ValueMap asMap() {
		getInternal({ componentNode -> componentNode.asMap() }, ValueMap.EMPTY)
	}

	@Override
	<T> T get(String propertyName, T defaultValue) {
		getInternal({ componentNode -> componentNode.get(propertyName, defaultValue) }, defaultValue)
	}

	@Override
	<T> Optional<T> get(String propertyName, Class<T> type) {
		getInternal({ componentNode -> componentNode.get(propertyName, type) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHref(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsHref(propertyName) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHref(String propertyName, boolean strict) {
		getInternal({ componentNode -> componentNode.getAsHref(propertyName, strict) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHref(String propertyName, boolean strict, boolean mapped) {
		getInternal({ componentNode -> componentNode.getAsHref(propertyName, strict, mapped) }, Optional.absent())
	}

	@Override
	Optional<Link> getAsLink(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsLink(propertyName) }, Optional.absent())
	}

	@Override
	Optional<Link> getAsLink(String propertyName, boolean strict) {
		getInternal({ componentNode -> componentNode.getAsLink(propertyName, strict) }, Optional.absent())
	}

	@Override
	Optional<Link> getAsLink(String propertyName, boolean strict, boolean mapped) {
		getInternal({ componentNode -> componentNode.getAsLink(propertyName, strict, mapped) }, Optional.absent())
	}

	@Override
	<T> List<T> getAsList(String propertyName, Class<T> type) {
		getInternal({ componentNode -> componentNode.getAsList(propertyName, type) }, Collections.emptyList())
	}

	@Override
	Optional<PageDecorator> getAsPage(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsPage(propertyName) }, Optional.absent())
	}

	@Override
	<AdapterType> Optional<AdapterType> getAsType(String propertyName, Class<AdapterType> type) {
		getInternal({ componentNode -> componentNode.getAsType(propertyName, type) }, Optional.absent())
	}

	@Override
	Optional<String> getImageReference(boolean isSelf) {
		getInternal({ componentNode -> componentNode.getImageReference(isSelf) }, Optional.absent())
	}

	@Override
	Optional<String> getImageReference() {
		getInternal({ componentNode -> componentNode.imageReference }, Optional.absent())
	}

	@Override
	Optional<String> getImageReference(String name) {
		getInternal({ componentNode -> componentNode.getImageReference(name) }, Optional.absent())
	}

	@Override
	Optional<String> getImageRendition(String renditionName) {
		getInternal({ componentNode -> componentNode.getImageRendition(renditionName) }, Optional.absent())
	}

	@Override
	Optional<String> getImageRendition(String name, String renditionName) {
		getInternal({ componentNode -> componentNode.getImageRendition(name, renditionName) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSource() {
		getInternal({ componentNode -> componentNode.imageSource }, Optional.absent())
	}

	@Override
	Optional<String> getImageSource(int width) {
		getInternal({ componentNode -> componentNode.getImageSource(width) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSource(String name) {
		getInternal({ componentNode -> componentNode.getImageSource(name) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSource(String name, int width) {
		getInternal({ componentNode -> componentNode.getImageSource(name, width) }, Optional.absent())
	}

	@Override
	<T> T getInherited(String propertyName, T defaultValue) {
		getInternal({ componentNode -> componentNode.getInherited(propertyName, defaultValue) }, defaultValue)
	}

	@Override
	<T> Optional<T> getInherited(String propertyName, Class<T> type) {
		getInternal({ componentNode -> componentNode.getInherited(propertyName, type) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHrefInherited(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHrefInherited(String propertyName, boolean strict) {
		getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName, strict) }, Optional.absent())
	}

	@Override
	Optional<String> getAsHrefInherited(String propertyName, boolean strict, boolean mapped) {
		getInternal({ componentNode -> componentNode.getAsHrefInherited(propertyName, strict, mapped) },
		Optional.absent())
	}

	@Override
	Optional<Link> getAsLinkInherited(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName) }, Optional.absent())
	}

	@Override
	Optional<Link> getAsLinkInherited(String propertyName, boolean strict) {
		getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName, strict) }, Optional.absent())
	}

	@Override
	Optional<Link> getAsLinkInherited(String propertyName, boolean strict, boolean mapped) {
		getInternal({ componentNode -> componentNode.getAsLinkInherited(propertyName, strict, mapped) },
		Optional.absent())
	}

	@Override
	<T> List<T> getAsListInherited(String propertyName, Class<T> type) {
		getInternal({ componentNode -> componentNode.getAsListInherited(propertyName, type) }, Collections.emptyList())
	}

	@Override
	Optional<PageDecorator> getAsPageInherited(String propertyName) {
		getInternal({ componentNode -> componentNode.getAsPageInherited(propertyName) }, Optional.absent())
	}

	@Override
	<AdapterType> Optional<AdapterType> getAsTypeInherited(String propertyName, Class<AdapterType> type) {
		getInternal({ componentNode -> componentNode.getAsTypeInherited(propertyName, type) }, Optional.absent())
	}

	@Override
	Optional<String> getImageReferenceInherited(boolean isSelf) {
		getInternal({ componentNode -> componentNode.getImageReferenceInherited(isSelf) }, Optional.absent())
	}

	@Override
	Optional<String> getImageReferenceInherited() {
		getInternal({ componentNode -> componentNode.imageReferenceInherited }, Optional.absent())
	}

	@Override
	Optional<String> getImageReferenceInherited(String name) {
		getInternal({ componentNode -> componentNode.getImageReferenceInherited(name) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSourceInherited() {
		getInternal({ componentNode -> componentNode.imageSourceInherited }, Optional.absent())
	}

	@Override
	Optional<String> getImageSourceInherited(int width) {
		getInternal({ componentNode -> componentNode.getImageSourceInherited(width) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSourceInherited(String name) {
		getInternal({ componentNode -> componentNode.getImageSourceInherited(name) }, Optional.absent())
	}

	@Override
	Optional<String> getImageSourceInherited(String name, int width) {
		getInternal({ componentNode -> componentNode.getImageSourceInherited(name, width) }, Optional.absent())
	}

	@Override
	Optional<PageDecorator> findAncestor(Predicate<PageDecorator> predicate) {
		findAncestor(predicate, false)
	}

	@Override
	Optional<PageDecorator> findAncestor(Predicate<PageDecorator> predicate, boolean excludeCurrentResource) {
		PageDecorator page = excludeCurrentResource ? parent : this
		PageDecorator ancestorPage = null

		while (page) {
			if (predicate.apply(page)) {
				ancestorPage = page
				break
			} else {
				page = page.parent
			}
		}

		Optional.fromNullable(ancestorPage)
	}

	@Override
	Optional<PageDecorator> findAncestorWithProperty(String propertyName) {
		findAncestorForPredicate(new ComponentNodePropertyExistsPredicate(propertyName), false)
	}

	@Override
	Optional<PageDecorator> findAncestorWithProperty(String propertyName, boolean excludeCurrentResource) {
		findAncestorForPredicate(new ComponentNodePropertyExistsPredicate(propertyName), excludeCurrentResource)
	}

	@Override
	<V> Optional<PageDecorator> findAncestorWithPropertyValue(String propertyName, V propertyValue) {
		findAncestorForPredicate(new ComponentNodePropertyValuePredicate<V>(propertyName, propertyValue), false)
	}

	@Override
	<V> Optional<PageDecorator> findAncestorWithPropertyValue(String propertyName, V propertyValue,
			boolean excludeCurrentResource) {
		findAncestorForPredicate(new ComponentNodePropertyValuePredicate<V>(propertyName, propertyValue),
				excludeCurrentResource)
	}

	@Override
	List<PageDecorator> findDescendants(Predicate<PageDecorator> predicate) {
		def pages = []
		def pageManager = this.pageManager

		delegate.listChildren(ALL_PAGES, true).each { child ->
			PageDecorator page = pageManager.getPage(child)

			if (predicate.apply(page)) {
				pages.add(page)
			}
		}

		pages
	}

	@Override
	List<PageDecorator> getChildren() {
		filterChildren(ALL, false)
	}

	@Override
	List<PageDecorator> getChildren(boolean displayableOnly) {
		displayableOnly ? filterChildren(DISPLAYABLE_ONLY, false) : filterChildren(ALL, false)
	}

	@Override
	List<PageDecorator> getChildren(Predicate<PageDecorator> predicate) {
		filterChildren(checkNotNull(predicate), false)
	}

	@Override
	Iterator<PageDecorator> listChildPages() {
		listChildPages(Predicates.alwaysTrue())
	}

	@Override
	Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate) {
		listChildPages(predicate, false)
	}

	@Override
	Iterator<PageDecorator> listChildPages(Predicate<PageDecorator> predicate, boolean deep) {
		def resource = delegate.adaptTo(Resource)
		def iterator = deep ? new DeepResourceIterator(resource) : resource.listChildren()

		new PageDecoratorIterator(iterator, predicate)
	}

	@Override
	Optional<PageDecorator> getChild(String name){
		if(hasChild(name)){
			return Optional.of(delegate.resource.getChild(name).adaptTo(PageDecorator))
		}
		return Optional.absent()
	}

	@Override
	Optional<ComponentNode> getComponentNode() {
		componentNodeOptional
	}

	@Override
	Optional<ComponentNode> getComponentNode(String relativePath) {
		componentNodeOptional.present ? componentNodeOptional.get().getComponentNode(relativePath) : Optional.absent()
	}

	@Override
	String getHref() {
		getHref(false)
	}

	@Override
	String getHref(boolean mapped) {
		getLink(mapped).href
	}

	@Override
	ImageLink getImageLink(String imageSource) {
		LinkBuilderFactory.forPage(this).setImageSource(checkNotNull(imageSource)).buildImageLink()
	}

	@Override
	boolean isHasImage() {
		getInternal({ ComponentNode componentNode -> componentNode.hasImage }, false)
	}

	@Override
	boolean isHasImage(String name) {
		getInternal({ componentNode -> componentNode.isHasImage(name) }, false)
	}

	@Override
	Link getLink() {
		getLink(false)
	}

	@Override
	Link getLink(TitleType titleType) {
		getLinkBuilder(titleType, false).build()
	}

	@Override
	Link getLink(boolean mapped) {
		getLinkBuilder(mapped).build()
	}

	@Override
	Link getLink(TitleType titleType, boolean mapped) {
		getLinkBuilder(titleType, mapped).build()
	}

	@Override
	LinkBuilder getLinkBuilder() {
		getLinkBuilder(false)
	}

	@Override
	LinkBuilder getLinkBuilder(TitleType titleType) {
		getLinkBuilder(titleType, false)
	}

	@Override
	LinkBuilder getLinkBuilder(boolean mapped) {
		LinkBuilderFactory.forPage(this, mapped, TitleType.TITLE)
	}

	@Override
	LinkBuilder getLinkBuilder(TitleType titleType, boolean mapped) {
		LinkBuilderFactory.forPage(this, mapped, titleType)
	}

	@Override
	NavigationLink getNavigationLink() {
		getNavigationLink(false, false)
	}

	@Override
	NavigationLink getNavigationLink(boolean isActive) {
		getNavigationLink(isActive, false)
	}

	@Override
	NavigationLink getNavigationLink(boolean isActive, boolean mapped) {
		LinkBuilderFactory.forPage(this, mapped, TitleType.NAVIGATION_TITLE).setActive(isActive).buildNavigationLink()
	}

	// overrides

	@Override
	PageDecorator getAbsoluteParent(int level) {
		pageManager.getPage(delegate.getAbsoluteParent(level))
	}

	@Override
	PageManagerDecorator getPageManager() {
		delegate.adaptTo(Resource).resourceResolver.adaptTo(PageManagerDecorator)
	}

	@Override
	PageDecorator getParent() {
		pageManager.getPage(delegate.parent)
	}

	@Override
	PageDecorator getParent(int level) {
		pageManager.getPage(delegate.getParent(level))
	}

	@Override
	String getTemplatePath() {
		properties.get(NameConstants.NN_TEMPLATE, "")
	}

	@Override
	Optional<String> getTitle(TitleType titleType) {
		get(titleType.propertyName, String)
	}

	@Override
	String getTitle() {
		properties.get(NameConstants.PN_TITLE, "")
	}

	// internals

	private <T> T getInternal(Closure<T> closure, T defaultValue) {
		componentNodeOptional.present ? closure.call(componentNodeOptional.get()) : defaultValue
	}

	private Optional<PageDecorator> findAncestorForPredicate(Predicate<ComponentNode> predicate,
			boolean excludeCurrentResource) {
		PageDecorator page = excludeCurrentResource ? parent : this
		PageDecorator ancestorPage = null

		while (page) {
			Optional<ComponentNode> optionalComponentNode = page.componentNode

			if (optionalComponentNode.present && predicate.apply(optionalComponentNode.get())) {
				ancestorPage = page
				break
			} else {
				page = page.parent
			}
		}

		Optional.fromNullable(ancestorPage)
	}

	private List<PageDecorator> filterChildren(Predicate<PageDecorator> predicate, boolean deep) {
		def pages = []
		def pageManager = this.pageManager

		delegate.listChildren(ALL_PAGES, deep).each { child ->
			def page = pageManager.getPage(child)

			if (page && predicate.apply(page)) {
				pages.add(page)
			}
		}

		pages
	}
}
