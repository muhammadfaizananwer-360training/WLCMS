/**
 *
 */
$(document).ready(function ()
		{
			//	BEGIN APP
			APP.PLACEHOLDER_FIX();

			APP.BODY_COLLAPSES();
			//	END APP

			$('button#searchAction').click(submitQuickSearch);

			$("#search_string").keypress(function(event) {
			    if (event.which == 13) {
			        event.preventDefault();
			        submitQuickSearch();
			    }
			});

		});

function submitQuickSearch(){

}
