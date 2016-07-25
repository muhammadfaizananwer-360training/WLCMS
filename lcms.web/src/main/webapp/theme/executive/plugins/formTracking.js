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
	if(contains(url, "editCourseOverview")){
		revertToActive("#nav_accordion_0");
	} else if(contains(url, "coursestructure")){
		revertToActive("#nav_accordion_1");
	} else if(contains(url, "courseSettings")){
		revertToActive("#nav_accordion_2");
	} else if(contains(url, "editClassroomWebinarCourse")){
		if(contains(url, "cType=5")) {
			//cType=5 is classroom
			revertToActive("#nav_accordion_0_cr");
		} else {
			//cType=6 is webinar
			revertToActive("#nav_accordion_0_wb");
		}
	} else if(contains(url, "instructor")){
		revertToActive("#nav_instructor");
	} else if(contains(url, "classroom-classes")){
		revertToActive("#nav_classroomsetup");
	} else if(contains(url, "locationList")){
		revertToActive("#nav_locationList");
	} else if(contains(url, "schedule")){
		revertToActive("#lnkSchedule");
	} else if(contains(url, "webinarSetup")){
		revertToActive("#lnkWebinarSetup");
	}
	//remaining 'Publishing' super tab
	else {
		revertToSuperTab(url);
	}
}

function contains(url, page){
	return url.indexOf(page) !== -1;
}

function revertToActive(selector){
	$(selector).removeClass("open-sign").addClass("active");
}

function revertToSuperTab(url){
	revertToActive("#nav_accordion_3");
	//expand Publishing super tab
	$("#nav_accordion_3").addClass("close-sign");
	$("ul.list-sub-group").attr("style", "display: block;");
	//remove any active sub tab of Publishing
	$("ul.list-sub-group>li.list-group-item>a.active").removeClass("active");
	//Now make current sub-tab of user 'active'
	if(contains(url, "availability")) {
		$("#lnkAvailability>a").addClass("active");
	} else if(contains(url, "setMarketing")) {
		$("#lnkMarketing>a").addClass("active");
	} else if(contains(url, "webinar_publishing")) {
		$("#lnkWebinarPublishing>a").addClass("active");
	} else if(contains(url, "publishing")) {
		$("#lnkPublishing>a").addClass("active");
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