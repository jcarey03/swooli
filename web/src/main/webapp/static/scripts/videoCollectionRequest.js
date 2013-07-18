$(function() {

    highlightFormInputErrors($('#videoCollectionForm .error').prev('input'));

    $('#descriptionWidgetContainer textarea').autosize({append: "\n"});
    var plugin = $('#descriptionWidgetContainer').medes({
        "serviceUrl" : "media.json",
        "descriptionCssClass" : "innerShadow",
        "mediaUrlCssClass" : "innerShadow",
        "wpsCssClass" : "innerShadow",
        "defaultDescription" : "Describe your video collection..."
    });

    // submit button handler
//    $('#createAccountButton').click(function() {
//        $('#accountForm').submit();
//    });


});
