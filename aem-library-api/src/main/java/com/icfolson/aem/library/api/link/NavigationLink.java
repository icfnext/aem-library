package com.icfolson.aem.library.api.link;

import java.util.List;

/**
 * A navigation link contains all of the attributes of a <code>Link</code> with the addition of an active state
 * attribute and a list of child links for representing navigation hierarchies.
 */
public interface NavigationLink extends Link {

    /**
     * List of child links. Useful for building navigation hierarchies.
     *
     * @return list of child links for this link or empty list if none exist
     */
    List<NavigationLink> getChildren();

    /**
     * @return true if link is active
     */
    boolean isActive();
}
