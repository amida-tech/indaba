CKEDITOR.editorConfig = function( config )
{
    config.toolbar = $.i18n.message('common.js.ckedit.toolbar');

    config.toolbar_MyToolbar =
    [
        [$.i18n.message('common.js.ckedit.toolbar.maximize')]
    ];
};