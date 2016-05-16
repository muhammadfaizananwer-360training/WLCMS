//	Plugin Source Code
CKEDITOR.plugins.add( 'abbr', {
    icons: 'abbr',
    init: function( editor ) {
        // Plugin logic goes here...
		
		//	Creating an Editor Command
		editor.addCommand( 'abbrDialog', new CKEDITOR.dialogCommand( 'abbrDialog' ) );

		//	Creating the Toolbar Button
		editor.ui.addButton( 'Abbr', {
			label: 'Insert hyperlink to support materials',
			command: 'abbrDialog',
			toolbar: 'others'
		});
		
		CKEDITOR.dialog.add( 'abbrDialog', this.path + 'dialogs/abbr.js' );
    }
});