/*
 * ADOBE CONFIDENTIAL
 *
 * Copyright 2014 Adobe Systems Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 */
(function ($, ns, channel, window, undefined) {

    var ui = {};

    function capitalizeFirstLetter (text) {
        return text.charAt(0).toUpperCase() + text.slice(1);
    }

    function createEditorSelector (editors, editable) {
        var editorSelectorList,
            editorSelector;

        // build a list of choices
        editorSelectorList = $('<coral-buttonlist class="inlineeditor-selector-list"></coral-buttonlist>');
        editors.forEach(function (editor) {
            editorSelectorList.append(
                "<button is='coral-buttonlist-item' class='inlineeditor-selector-list-item' data-editor='"+ editor.type +"' data-targetid='" + editor.id + "'>" +
                Granite.I18n.get(capitalizeFirstLetter(editor.title)) +
                "</button>");
        });

        return editorSelectorList;
    }

    function setUpUI (editors, editable) {
        // create editor selector (little box on the editable)
        ui = createEditorSelector(editors, editable);

        // create popover
        var popover = new Coral.Popover().set({
            alignAt: Coral.Overlay.align.LEFT_BOTTOM,
            alignMy: Coral.Overlay.align.LEFT_TOP,
            content: {
                innerHTML: ''
            },
            target: $(".cq-editable-action")[0],
            open: true
        });
        ui.appendTo(popover.content);

        $(popover).appendTo(document.body);
    }

    function tearDownUI (editable) {
        $("#EditableToolbar").css("opacity", "");
        editable.overlay.dom.find(".inlineeditor-is-hover-subtarget").removeClass("inlineeditor-is-hover-subtarget");
        ui.closest("coral-popover").remove();
    }

    function bindEventsListener (editable) {
        // reacts on the next click
        $(".inlineeditor-selector-list-item").on("tap.editorselector click.editorselector", function (event) {
            var expectedTarget = $(event.target).closest("button[data-editor]"),
                editorType = expectedTarget.data("editor"),
                targetId = expectedTarget.data("targetid");

            if (editorType && targetId) {
                $("#EditableToolbar").css("opacity", "0");
                if(!editable.config.ipeConfigBackup){
                	editable.config.ipeConfigBackup = jQuery.extend({}, editable.config.ipeConfig);
                }
                editable.config.ipeConfig =  editable.config.ipeConfigBackup[targetId]	
                ns.editor.registry[editorType].setUp(editable, targetId);
            }

            tearDownUI(editable);
        });

        channel.on("keyup.editorselector", function (event) {
            if(event.keyCode === 13) {
                var targetId = editable.overlay.dom.find(".inlineeditor-is-hover-subtarget").data("asset-id"),
                    editorType = $(".inlineeditor-selector-list-item[data-targetid='" + targetId + "']").data("editor");

                if (editorType && targetId) {
                    $("#EditableToolbar").css("opacity", "0");
                    if(!editable.config.ipeConfigBackup){
                    	editable.config.ipeConfigBackup = jQuery.extend({}, editable.config.ipeConfig);
                    }
                    editable.config.ipeConfig =  editable.config.ipeConfigBackup[targetId]	
                    ns.editor.registry[editorType].setUp(editable, targetId);
                }

                tearDownUI(editable);
            }

        });

        channel.on('cq-interaction-focus.toolbar', function (event) {
            tearDownUI(editable);
        });

        $(".inlineeditor-selector-list-item").on("taphold.editorselector mouseenter.editorselector mouseleave.editorselector", function (event) {
            var expectedTarget = $(event.target).closest("button[data-editor]"),
                editorType = expectedTarget.data("editor"),
                targetId = expectedTarget.data("targetid");

            if(event.type === "taphold") {
                editable.overlay.dom.find(".inlineeditor-is-hover-subtarget").removeClass("inlineeditor-is-hover-subtarget");
            }

            if (editorType && targetId) {
                var dropTarget = editable.getDropTarget(targetId);
                if (dropTarget) {
                    dropTarget.overlay.toggleClass("inlineeditor-is-hover-subtarget");
                } else {
                    editable.overlay.dom.find("[data-asset-id='" + targetId + "']").toggleClass("inlineeditor-is-hover-subtarget");
                }
            }
        });
    }

    function unbindEventsListener () {
        $(".inlineeditor-selector-list-item").off("tap.editorselector click.editorselector");
        channel.off("keyup.editorselector");
        $(".inlineeditor-selector-list-item").off("taphold.editorselector mouseenter.editorselector mouseleave.editorselector");
    }

    /**
     * @class Granite.author.editor.HybridEditor
     * @param {Array.<InPlaceEditor>} editors List of the different in-place editors that will compose the HybridEditor
     * @classdesc The HybridEditor is a kind of "super" in-place editor that could be composed of different in-place editors.
     */
    ns.editor.HybridEditor = function (editors) {
        var self = this;
        if(editors !== null)
            self.editors = editors;

        // TODO: still here for compatibility reasons, but it's better
        // to launch the editor via startImageEditor() directly
        channel.on("inline-edit-start", function (e) {
            var editable = e.editable;
            self.startHybridEditor(editable);
        });
    };

    /**
     * Starts the hybrid in-place editor on the given Editable.
     * The Editable's corresponding component should be configured to support multiple childEditors (via editConfig.inplaceEditingConfig.childEditors)
     *
     * @function Granite.author.editor.HybridEditor#setUp
     *
     * @param {Granite.author.Editable} editable The editable to edit
     */
    ns.editor.HybridEditor.prototype.setUp = function (editable) {
        var editors = editable.config.editConfig.inplaceEditingConfig.childEditors;
        if(editors == null)
            editors = [];
        $.each(editors, function(index) {
            try {
                editors[index] = JSON.parse(this.toString());
            } catch (error) {
                //error parsing json
            }
        });
        this.editors = editors || this.editors;
        this.editors = $.grep(this.editors, function(editor) {
            return editor.type !== "image" || editable.dom.has(".cq-placeholder[class$=" + editor.id + "]").length === 0;
        });
        this.startHybridEditor(editable);
    };

    /**
     * Stops the hybrid in-place editor on the given Editable
     *
     * @function Granite.author.editor.HybridEditor#tearDown
     *
     * @param {Granite.author.Editable} editable The editable to edit
     */
    ns.editor.HybridEditor.prototype.tearDown = function (editable) {
        this.endHybridEditor(editable);
    };

    ns.editor.HybridEditor.prototype.startHybridEditor = function (editable) {
        // if the user is able to choose between multiple inplace editors
        if (this.editors.length > 1) {
            setUpUI(this.editors, editable);
        } else if(this.editors.length ===1) {
            ns.editor.registry[this.editors[0].type].setUp(editable, this.editors[0].id);
        }
        bindEventsListener(editable);
    };

    ns.editor.HybridEditor.prototype.endHybridEditor = function (editable) {
        tearDownUI(editable);
        unbindEventsListener();
    };

    ns.editor.register('hybrid', new ns.editor.HybridEditor());

}(jQuery, Granite.author, jQuery(document), this));
