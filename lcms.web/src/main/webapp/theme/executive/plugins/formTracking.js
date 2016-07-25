$.fn.extend({
	trackChanges: function() {
		$(":input",this).change(function() {
			if(this.name!='keywords'){
				$(this.form).data("changed", true);
			}
		});
	}
	,
	isChanged: function() {
		return this.data("changed");
	}
});

function navigateToURL(){
	var link = $("#navigatingLink").val();
	location.href=link;
}

/************************
 * Author: Mujeeb Tariq
 * When a user makes changes in a page and leaves changes unsaved and tries to move to another page,
 * a dialog box asks user whether he wants to save changes. If he presses yes, then this function triggers
 * and reverts back his old tab as active and user remains on same page.
 ************************/
function revertToSameURL(){
	//remove active class from any tab having active class
	$("li.list-group-item>a.btn-sub-group.active").removeClass("active");
	//Check current page location of user
	var url = window.location.toString();
	//Now make current tab of user 'active' based on his current page
	if(url.includes("editCourseOverview")){
		$("#nav_accordion_0").removeClass("open-sign");
		$("#nav_accordion_0").addClass("active");
	} else if(url.includes("coursestructure")){
		$("#nav_accordion_1").removeClass("open-sign");
		$("#nav_accordion_1").addClass("active");
	} else if(url.includes("courseSettings")){
		$("#nav_accordion_2").removeClass("open-sign");
		$("#nav_accordion_2").addClass("active");
	}
	//remaining 'Publishing' super tab
	else {
		$("#nav_accordion_3").removeClass("open-sign");
		$("#nav_accordion_3").addClass("active");
		//remove any active sub tab of Publishing
		$("ul.list-sub-group>li.list-group-item>a.active").removeClass("active");
		//Now make current sub-tab of user 'active'
		if(url.includes("availability")) {
			$("#lnkAvailability>a").addClass("active");
		} else if(url.includes("setMarketing")) {
			$("#lnkMarketing>a").addClass("active");
		} else if(url.includes("publishing")) {
			$("#lnkPublishing>a").addClass("active");
		}
	}
}

function checkForFormChanged(link){

	var formName = $("#currentFormName").val();
	$("#navigatingLink").val(link);

	if ( formName == "frm_marketing") {
		APP.CACHE = link;
		SaveMarketingOption ();
		return;
	}

	if ($("#" + formName).isChanged()){
        $('#confimrationModal').modal('show');
	} else{
		window.location.assign(link);
	}
}