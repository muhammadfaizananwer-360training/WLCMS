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
		//location.href=link;
		window.location.assign(link);
	}
}