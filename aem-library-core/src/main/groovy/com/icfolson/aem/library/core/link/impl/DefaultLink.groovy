package com.icfolson.aem.library.core.link.impl

import com.icfolson.aem.library.api.link.Link
import groovy.transform.Immutable

@Immutable
class DefaultLink implements Link {

	String path

	String extension

	String suffix

	String href

	List<String> selectors

	String queryString

	boolean external

	String target

	String title

	Map<String, String> properties

	@Override
	boolean isEmpty() {
		!href
	}
}
