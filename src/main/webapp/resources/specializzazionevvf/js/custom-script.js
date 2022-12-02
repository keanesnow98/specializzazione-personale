/**
 * Is this component wrapped in a float label?
 *
 * @param {JQuery | undefined | null} jq An element to check if wrapped in float label. 
 * @return {boolean} true this this JQ has a float label parent
 */
PrimeFaces.utils.hasFloatLabel = function(jq) {
    if (!jq || !jq.parent()) {
        return false;
    }
    return jq.parent().hasClass('ui-float-label');
};

/**
 * Handles floating label CSS if wrapped in a floating label.
 * @private
 * @param {JQuery | undefined} element the to add the CSS classes to
 * @param {JQuery | undefined} input the input to check if filled
 * @param {boolean | undefined} hasFloatLabel true if this is wrapped in a floating label
 */
PrimeFaces.utils.updateFloatLabel = function(element, input, hasFloatLabel) {
    if (!element || !input || !hasFloatLabel) {
        return;
    }
    if (input.val() !== '' || element.find('.ui-chips-token').length !== 0) {
        element.addClass('ui-inputwrapper-filled');
    }
    else {
        element.removeClass('ui-inputwrapper-filled');
    }
};