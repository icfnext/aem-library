package com.icfolson.aem.library.api.page.enums;

import com.day.cq.wcm.api.NameConstants;

/**
 * Enumeration of page title types.
 */
public enum TitleType {

    /**
     * Default page title corresponding to "jcr:title" property.
     */
    TITLE(NameConstants.PN_TITLE),

    /**
     * Secondary page title corresponding to "pageTitle" property.
     */
    PAGE_TITLE(NameConstants.PN_PAGE_TITLE),

    /**
     * Navigation title corresponding to "navTitle" property.
     */
    NAVIGATION_TITLE(NameConstants.PN_NAV_TITLE);

    private final String propertyName;

    TitleType(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
