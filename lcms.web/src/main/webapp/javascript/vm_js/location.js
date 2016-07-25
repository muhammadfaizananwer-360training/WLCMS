function location_list()
{

	APP.AJAX({
				url: 'locationList?renderVmDecisionParam=1',
				type: "GET",
				cache: false,
				async: false,
				success: function(response) {
					var object = jQuery.parseJSON(response);
					oTb.fnClearTable();
					if(object.length > 0){
						oTb.fnAddData(object);
					}
		},error: function(data){
			alert('Error');
		}

   });
}

function confirmDeleteObject (assetType, trg, event) {
	$trgModal = $("#confirmationModal");
	APP.CACHE = trg;
	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
	var msg = '<p>This action will remove the location from this template. Do you want to continue?</p>';
	var btns = '<button type="button" class="btn blue" onclick="delete_locations(); " data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="unCheckCheckBoxesWhenCancelonClick()"  data-dismiss="modal">NO</button>';


	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');

}

function unCheckCheckBoxesWhenCancelonClick(){
	$('input[name="location_checkboxes"]:checked').removeAttr('checked');
    $('input[name="location_checkbox_parent"]:checked').removeAttr('checked');

}

	function delete_locations()
	{
		$('input[name="location_checkboxes"]:checked').closest('tr').addClass("del");

		var ids = $('input[name="location_checkboxes"]:checked');//$( "[name='pages_title']" );
		if(ids.length<=0)
		{
			TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_ON_DELETE_SELECT,bFadeOut:true});
			return false;
		}
		var commaseparateids="";;
		for(var i=0; i<ids.length; i++)
		{
			commaseparateids+=ids[i].value+","

		}


		commaseparateids = commaseparateids.substr(0,commaseparateids.length-1);;
		var id ="1";;
		APP.AJAX({
		url: 'deleteLocation',
		type: "POST",
		cache: false,
		data:{ locationIds: commaseparateids },
		async: false,
		success: function(response) {
			if(response)
			{
				var table = $('#location_table').DataTable();
				table.row('.del').remove().draw( false );


				TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
				reset_modal();
			}else
				{
					TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.DELETE_LOCATION_FAILURE_MESSAGE, bFadeOut:true});
				}
		},error: function(data){
			alert('Error');
		}

   });

	}

		function table_minus_plus(cnd)
		{
			if(cnd == "PLUS")
			{
				$("#addLocationModal").modal('show');

			}
			else
			{
				confirmDeleteObject();
							}
		}

		function confirm_remove_class_loc(confirm)
		{
			if(confirm)
			{
				$table = APP.CACHE[0];

				//	DELETE
				$table.children().each(function(index,element){

					if($(element).find('.checks').prop('checked') == true)
					{
						$(element).remove();
					}

				});

				$(APP.CACHE[1]).prop("checked",false);
			}
			APP.CACHE = "";
		}


	function cancelLocation() {

	$("label.error").hide();
	$(".error").removeClass("error");
	$('#msgdiv').html ('');

	return false;
}


function addLocation(come_from_classroom) {

	if(come_from_classroom!="")
	{
		add_location_from_classroom();
		return;

	}
	if($("#locationid").val()!="")
	{
			updateLocation();
			return;
	}


	nameValid = $("#addMyLocationform").validate().element("#locationname");
	stateValid = $("#addMyLocationform").validate().element("#state");
	countryValid = $("#addMyLocationform").validate().element("#country");
	phoneValid = $("#addMyLocationform").validate().element("#locationphone");
	addressValid = $("#addMyLocationform").validate().element("#locationaddress");
	cityValid = $("#addMyLocationform").validate().element("#locationcity");
	zipValid = $("#addMyLocationform").validate().element("#locationzip");



	if( nameValid== false || stateValid == false || countryValid == false || phoneValid == false || addressValid == false || cityValid == false || zipValid == false){

		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		$('#submitLocation').removeAttr("data-dismiss");
		return false;

	}

	APP.AJAX({
		url: 'addLocation',
		type: "POST",
		cache: false,
		data:{locationName: $("#locationname").val(), city: $("#locationcity").val(),
			zip: $("#locationzip").val(),country: $("#country").val(),
			state: $("#state").val(),phone: $("#locationphone").val(),
			desc: $("#locationdesc").val(),address: $("textarea[name='locationaddress']").val()},
		async: false,
		success: function(response) {
				var object = [ "<input id='"+response.id+"' type='checkbox' onclick=\"APP.CHECKBOX_WITH_BTN(this,false,'loc-delete-btn')\" class='checks' value='"+response.id+"' name='location_checkboxes'>", "<a class='anchor' href='javascript:;' data-toggle='modal' data-target='#addLocationModal' onclick='loadLocation("+response.id+")'>"+response.locationname+"</a>", ""+response.city+"",""+response.state+""];
				oTb.fnAddData(object);
				$('#submitLocation').attr('data-dismiss', 'modal');

				TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

		}
   });




}
function reset_modal()
{
	nameValid = $("#locationname").val("");
	stateValid = $("#state").val("");
	countryValid = $("#country").val("");
	phoneValid = $("#locationphone").val("");
	addressValid = $("#locationaddress").val("");
	cityValid = $("#locationcity").val("");
	zipValid = $("#locationzip").val("");
	descValid = $("#locationdesc").val("");
	id=  $("#locationid").val("");
	cancelLocation();
}



function loadLocation(id)
{
	cancelLocation();
	jQuery.validator.messages.required = "";



	jQuery("selector").validate();
	$("#"+id).closest('tr').addClass("update");
	var bSearch = false;
	var asset_object;
	APP.AJAX({
		url: 'loadLocation',
		type: "GET",
		data:{locationId: id},
		async: true,
		success: function(response) {
			bSearch = true;
			asset_object = eval(response);
			d = asset_object;
			$("#locationname").val(d.locationname);
			$("#state").val(d.state);
			$("#locationcity").val(d.city);
			$("#country").val(d.country);
			$("#locationphone").val(d.phone);
			$("#locationzip").val(d.zip);
			$("#locationid").val(d.id);
			$("textarea[name='locationaddress']").val(d.streetAddress);
			$("textarea[name='locationdesc']").val(d.description);

			COUNTRIES.PRINT_STATE('state', $("#country")[0].selectedIndex);
			$('select[name^="state"] option[value="'+d.state+'"]').prop('selected',true);

		}



   });


}





function updateLocation()
{

	nameValid = $("#addMyLocationform").validate().element("#locationname");
	stateValid = $("#addMyLocationform").validate().element("#state");
	countryValid = $("#addMyLocationform").validate().element("#country");
	phoneValid = $("#addMyLocationform").validate().element("#locationphone");
	addressValid = $("#addMyLocationform").validate().element("#locationaddress");
	cityValid = $("#addMyLocationform").validate().element("#locationcity");
	zipValid = $("#addMyLocationform").validate().element("#locationzip");



	if( nameValid== false || stateValid == false || countryValid == false || phoneValid == false || addressValid == false || cityValid == false || zipValid == false){

		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
		$('#submitLocation').removeAttr("data-dismiss");
		return false;

	}

	APP.AJAX({
		url: 'updateLocation',
		type: "POST",
		cache: false,
		data:{locationName: $("#locationname").val(), city: $("#locationcity").val(),
			zip: $("#locationzip").val(),country: $("#country").val(),
			state: $("#state").val(),phone: $("#locationphone").val(),
			desc: $("#locationdesc").val(),address: $("textarea[name='locationaddress']").val(),loc_id: $("#locationid").val()},
		async: false,
		success: function(response) {
			$('#submitLocation').attr('data-dismiss', 'modal');
			var object = [ "<input id='"+response.id+"' type='checkbox' onclick=\"APP.CHECKBOX_WITH_BTN(this,false,'loc-delete-btn')\" class='checks' value='"+response.id+"' name='location_checkboxes'>", "<a class='anchor' href='javascript:;' data-toggle='modal' data-target='#addLocationModal' onclick='loadLocation("+response.id+")'>"+response.locationname+"</a>", ""+response.city+"",""+response.state+""];
			var table = $('#location_table').DataTable();
			table.row('.update').data(object).draw( false );
			$("#"+response.id).closest('tr').removeClass('update');
			TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});


		}
   });


}