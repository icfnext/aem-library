package com.icfolson.aem.library.core.tags

import com.icfolson.aem.prosper.tag.JspTagProxy
import com.icfolson.aem.prosper.traits.JspTagTrait

import javax.servlet.jsp.tagext.TagSupport

import static com.day.cq.wcm.tags.DefineObjectsTag.DEFAULT_XSSAPI_NAME

trait JspMetaTagTrait extends JspTagTrait {

    @Override
    JspTagProxy init(Class<TagSupport> tagClass, String path) {
        init(tagClass, path, [:])
    }

    @Override
    JspTagProxy init(Class<TagSupport> tagClass, String path, Map<String, Object> additionalPageContextAttributes) {
        additionalPageContextAttributes[DEFAULT_XSSAPI_NAME] = new MockXssApi()

        super.init(tagClass, path, additionalPageContextAttributes)
    }
}