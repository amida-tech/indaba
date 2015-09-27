CKEDITOR.editorConfig = function( config )
{
    config.toolbar = 'MyToolbar';

    config.toolbar_MyToolbar =
    [
        ['Source','-','PasteText','PasteFromWord','-','Print', 'SpellChecker', 'Scayt'],
        ['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'],
        ['Bold','Italic','Underline','Strike','NumberedList','BulletedList','-','Link','Unlink','Anchor'],
        ['Format','BGColor','Maximize']
    ];
    config.language = $.i18n.message('common.language');
};
