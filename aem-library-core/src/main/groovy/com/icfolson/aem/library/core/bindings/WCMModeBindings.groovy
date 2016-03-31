package com.icfolson.aem.library.core.bindings

import com.day.cq.wcm.api.WCMMode
import org.apache.sling.api.SlingHttpServletRequest

import javax.script.Bindings

/**
 * Additional bindings to add to the script context (e.g. for consumption in Sightly templates).
 */
final class WCMModeBindings implements Bindings {

    public static final String IS_ANALYTICS_MODE = "isAnalyticsMode"

    public static final String IS_AUTHOR = "isAuthor"

    public static final String IS_EDIT_MODE = "isEditMode"

    public static final String IS_DESIGN_MODE = "isDesignMode"

    public static final String IS_PREVIEW_MODE = "isPreviewMode"

    public static final String IS_READ_ONLY_MODE = "isReadOnlyMode"

    public static final String IS_PUBLISH = "isPublish"

    public static final String IS_DEBUG = "isDebug"

    private static final String PARAMETER_DEBUG = "debug"

    @Delegate
    private final Map<String, Object> map = [:]

    WCMModeBindings(SlingHttpServletRequest slingRequest) {
        def mode = WCMMode.fromRequest(slingRequest)

        map.put(IS_AUTHOR, mode != WCMMode.DISABLED)
        map.put(IS_PUBLISH, mode == WCMMode.DISABLED)
        map.put(IS_EDIT_MODE, mode == WCMMode.EDIT)
        map.put(IS_DESIGN_MODE, mode == WCMMode.DESIGN)
        map.put(IS_PREVIEW_MODE, mode == WCMMode.PREVIEW)
        map.put(IS_ANALYTICS_MODE, mode == WCMMode.ANALYTICS)
        map.put(IS_READ_ONLY_MODE, mode == WCMMode.READ_ONLY)
        map.put(IS_DEBUG, Boolean.valueOf(slingRequest.getParameter(PARAMETER_DEBUG)))
    }

    @Override
    def put(String key, value) {
        map.put(key, value)
    }
}
