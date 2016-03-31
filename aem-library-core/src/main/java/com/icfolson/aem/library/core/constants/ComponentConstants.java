package com.icfolson.aem.library.core.constants;

/**
 * Component and resource type constants.
 */
public final class ComponentConstants {

    /**
     * Relative path to content components.
     */
    public static final String COMPONENT_PATH_CONTENT = "/content";

    /**
     * Default page/component image name.
     */
    public static final String DEFAULT_IMAGE_NAME = "image";

    /**
     * Component group that is hidden from the Sidekick.
     */
    public static final String GROUP_HIDDEN = ".hidden";

    /**
     * Conventional node/resource name for a main paragraph system.
     */
    public static final String NODE_NAME_PAR = "par";

    /**
     * Base resource type for all components.
     */
    public static final String RESOURCE_TYPE_PARBASE = "foundation/components/parbase";

    /**
     * Sling resource type for paragraph systems.
     */
    public static final String RESOURCE_TYPE_PARSYS = "foundation/components/parsys";

    /**
     * Property name in component descriptor containing annotated class name.
     */
    public static final String PROPERTY_CLASS_NAME = "className";

    /**
     * Edit bar property. Displays the static text value, e.g. to show "Breadcrumb" in the edit bar for a
     * component, you would use EDIT_BAR_TEXT + "Breadcrumb".
     */
    public static final String EDIT_BAR_TEXT = "text: ";

    /**
     * Edit bar property. Adds a spacer to the edit bar.
     */
    public static final String EDIT_BAR_SPACER = "-";

    /**
     * Edit bar property. Adds an edit button to the edit bar that, when clicked, will open up the edit dialog for the
     * component.
     */
    public static final String EDIT_BAR_EDIT = "edit";

    /**
     * Edit bar property. Adds a delete button to the edit bar that, when clicked, will delete the component.
     */
    public static final String EDIT_BAR_DELETE = "delete";

    /**
     * Edit bar property. Adds an insert button to the edit bar that, when clicked, will allow a new component to be
     * inserted above the current one.
     */
    public static final String EDIT_BAR_INSERT = "insert";

    /**
     * Edit bar property. Adds an annotate button to the edit bar that, when clicked, allows the component to be annotated.
     */
    public static final String EDIT_BAR_ANNOTATE = "annotate";

    /**
     * Edit bar property. Adds a copy/cut button to the edit bar that, when clicked, allows the component to be moved.
     */
    public static final String EDIT_BAR_COPY_MOVE = "copymove";

    /**
     * Page refresh listener value.
     */
    public static final String REFRESH_PAGE = "REFRESH_PAGE";

    /**
     * Self refresh listener value.
     */
    public static final String REFRESH_SELF = "REFRESH_SELF";

    /**
     * Parent refresh listener value.
     */
    public static final String REFRESH_PARENT = "REFRESH_PARENT";

    /**
     * Self moved refresh listener value.
     */
    public static final String REFRESH_SELFMOVED = "REFRESH_SELFMOVED";

    /**
     * Inserted refresh listener value.
     */
    public static final String REFRESH_INSERTED = "REFRESH_INSERTED";

    /**
     * Name of event to be fired after successfully inserting a new component instance.
     */
    public static final String EVENT_AFTER_INSERT = "afterinsert";

    /**
     * Name of event to be fired before moving.
     */
    public static final String EVENT_BEFORE_MOVE = "beforemove";

    /**
     * Name of event to be fired after successfully moving.
     */
    public static final String EVENT_AFTER_MOVE = "aftermove";

    /**
     * Name of event to be fired before copying.
     */
    public static final String EVENT_BEFORE_COPY = "beforecopy";

    /**
     * Name of event to be fired after successfully copying.
     */
    public static final String EVENT_AFTER_COPY = "aftercopy";

    /**
     * Name of event to be fired before editing a component instance.
     */
    public static final String EVENT_BEFORE_EDIT = "beforeedit";

    /**
     * Name of event to be fired after successfully editing a component instance.
     */
    public static final String EVENT_AFTER_EDIT = "afteredit";

    /**
     * Name of event to be fired before deleting a component instance.
     */
    public static final String EVENT_BEFORE_DELETE = "beforedelete";

    /**
     * Name of event to be fired after successfully deleting a component instance.
     */
    public static final String EVENT_AFTER_DELETE = "afterdelete";

    /**
     * Name of event to be fired before inserting a child component instance.
     */
    public static final String EVENT_BEFORE_CHILD_INSERT = "beforechildinsert";

    /**
     * Name of event to be fired after successfully insert a child component instance.
     */
    public static final String EVENT_AFTER_CHILD_INSERT = "afterchildinsert";

    /**
     * Name of event to be fired when rendering component.
     */
    public static final String EVENT_RENDER = "render";

    /**
     * Name of event to be fired when component is ready (rendered and events attached).
     */
    public static final String EVENT_READY = "ready";

    /**
     * Name of event to be fired when updating a component list.
     */
    public static final String EVENT_UPDATE_COMPONENTLIST = "updatecomponentlist";

    private ComponentConstants() {

    }
}
