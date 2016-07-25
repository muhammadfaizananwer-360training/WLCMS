function getUrlParameter(sParam)
{
    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++)
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam)
        {
            return sParameterName[1];
        }
    }
}

$(function() {

	APP.PLACEHOLDER_FIX();
	APP.EDIT_OR_VIEW_TOGGLE();

	/*if($('#varSyncClassId').val() > 0)
	{

	}
	else
	{
	var cType = getParameterByName('cType');
	if(cType != null && cType == WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE ){
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE_C});
		}
	if(cType != null && cType == WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE ){
		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_WEBINAR_SETUP_REQUIRED_INSTRUCTOR_SCEHDULE_W});
	  }

		$("#presenter_firstname").attr('disabled','disabled');
		$("#presenter_lastname").attr('disabled','disabled');
		$("#presenter_email").attr('disabled','disabled');
		$("#presenter_phonenumber").attr('disabled','disabled');
		$("#btnSave").attr('disabled','disabled');
		$("#full_name").attr('disabled','disabled');
		$("#email_address").attr('disabled','disabled');
		$("#phone_no").attr('disabled','disabled');
	}
*/

	if($('#synccoursetype').val() == "Classroom Course")
	{
		APP.LEFT_NAV.init("OPEN","nav_accordion_1c");
	}
	else if($('#synccoursetype').val() == "Webinar Course")
	{
		APP.LEFT_NAV.init("OPEN","nav_accordion_1w");
	}


	APP.BODY_COLLAPSES();
	APP.BOOTSTRAP_POPOVER("[data-toggle='popover']");


	$('#lnkInstructor a').addClass('active');
	$("#frm_instructor").trackChanges();
	$("#currentFormName").val("frm_instructor");

	var isValidPhone = function(){
	  var phone = $("#phoneNo").val();
	  return !(phone !== "" && !$.isNumeric(phone));
	};
	var isValidProviderPhone = function(){
		  var phone = $("#presenter_phonenumber").val();
		  var provider_phone = $("#phone_no").val();
		  return !(provider_phone !== "" && !$.isNumeric(provider_phone));
		};
     jQuery.validator.addMethod("isValidPhone", function() {

        return isValidPhone();}, "Please enter valid phone number.");
     jQuery.validator.addMethod("isValidProviderPhone", function() {

         return isValidProviderPhone();}, "Please enter valid phone number.");


    $("#frm_instructor").validate({
    	/*ignore: ':hidden:not(#enroll_date)',*/
    	// Specify the validation rules

    	ignore:[],
    	rules: {
    		presenter_firstname: { required: true },
    		presenter_lastname: { required: true},
    		presenter_email: { required: true, email: true },
        	presenter_phonenumber: { required: true, isValidPhone: true },
			full_name:{required: true},
			email_address:{required: true, email: true},
			phone_no:{ required: true, isValidProviderPhone: true }

        },
        // Specify the validation error Placement

      	 // Specify the validation error messages
        messages: {
        	presenter_firstname: {required : "Please enter first name."},
        	presenter_lastname: {required : "Please enter last name."},
        	presenter_email: {required : "Please enter email address.", email: "Presenter email address is of an invalid format."},
        	presenter_phonenumber: { required: "Please enter phone number.", isValidPhone: "Please enter valid phone number." }  ,
			full_name:{required : "Please enter full name."},
			email_address:{required : "Please enter email address.", email: "Email address is of an invalid format."},
			phone_no: { required: "Please enter phone number.", isValidProviderPhone: "Please enter valid phone number." }

        },



        submitHandler: function(form)
        {
        	var providerName = $("#frm_instructor").validate().element("#full_name");
        	var providerEmail = $("#frm_instructor").validate().element("#email_address");
        	var instructorName = $("#frm_instructor").validate().element("#presenter_firstname");
        	var instructorEmail = $("#frm_instructor").validate().element("#presenter_email");
        	var instructorLastName = $("#frm_instructor").validate().element("#presenter_lastname");
        	var instructorPhone = $("#frm_instructor").validate().element("#presenter_phonenumber");
        	var provider_phone_no = $("#frm_instructor").validate().element("#phone_no");


        	if (!providerName || !providerEmail || !provider_phone_no || !instructorName || !instructorEmail || !instructorLastName || !instructorPhone  ) {
        		TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
        		return;
        	}
			form.submit(function(){
                alert('Enter');
                $.ajax({
                    type: "POST",
                    url: "saveInstructor",
                    data: $(this).serialize(),
                    success: function(response) {
                        if(response.email=='Email exist'){
                            TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.EMAIL_INSTRUCTOR_FAILURE_MESSAGE, bFadeOut:true});
                            return;
                        }
                        alert('dfkdkn');
                    }
                });

            }) ;
        },

        invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
				TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut('.messages');
			elementFadeOut("#msgdiv");
		}

    });


    $("#frm_classinstructor_modal").validate({
        /*ignore: ':hidden:not(#enroll_date)',*/
        // Specify the validation rules

        rules: {
            firstName: { required: true },
            lastName: { required: true},
            email: { required: true, email: true },
            phoneNo: { required: true, isValidPhone: true }

        },
        // Specify the validation error Placement

        // Specify the validation error messages
        messages: {
            firstName: {required : "Please enter first name."},
            lastName: {required : "Please enter last name."},
            email: {required : "Please enter email address.", email: "Please enter valid email address."},
            phoneNo: { required: "Please enter phone number.", isValidPhone: "Please enter valid phone number." }


        }

    });


  });




function gotoEdit (courseid)
{
	window.location = "editCourseOverview?id=" + courseid.value;
}

function elementFadeOut(id)
{
	setTimeout(function()
	{
      $(id).html('');
  },9000);
}


function instructor_list()
{

	APP.AJAX({
		url: 'classInstructors',
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


function table_minus_plus(cnd)
{
	if(cnd == "PLUS")
	{
        //$("#add-lesson-label").text('Edit');
        $("#addClassInstructorModal").modal('show');
        $("#addClassInstructorModal form").validate().resetForm();

	}
	else
	{
		confirmDeleteObject();
	}
}
function confirmDeleteObject (assetType, trg, event) {
    $trgModal = $("#confirmationModal");
    APP.CACHE = trg;
    //	BEGIN TITLE, MESSAGE AND BUTTONS
    var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
    var msg = '<p>This action will remove the instructor from this template. Do you want to continue?</p>';
    var btns = '<button type="button" class="btn blue" onclick="delete_instructors(); " data-dismiss="modal">YES</button>'+
        '<button type="button" class="btn btn-default" onclick="unCheckCheckBoxesWhenCancelonClick()" data-dismiss="modal">NO</button>';


    $trgModal.find(".modal-title").html(title);
    $trgModal.find(".modal-body").html(msg);
    $trgModal.find(".modal-footer").html(btns);

    $trgModal.modal('show');

}
function unCheckCheckBoxesWhenCancelonClick(){
    $('input[name="classinstructor_checkboxes"]:checked').removeAttr('checked');
    $('input[name="classinstructor_checkbox_parent"]:checked').removeAttr('checked');

}
function delete_instructors()
{
    $('input[name="classinstructor_checkboxes"]:checked').closest('tr').addClass("del");

    var ids = $('input[name="classinstructor_checkboxes"]:checked');//$( "[name='pages_title']" );
    if(ids.length<=0)
    {
        TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_ON_DELETE_SELECT,bFadeOut:true});
        return false;
    }
    var commaseparateids="";
    for(var i=0; i<ids.length; i++)
    {
        commaseparateids+=ids[i].value+","

    }


    commaseparateids = commaseparateids.substr(0,commaseparateids.length-1);
    var id ="1";
    APP.AJAX({
        url: 'deleteClassInstructions',
        type: "POST",
        cache: false,
        data:{ classInstructionIds: commaseparateids },
        async: false,
        success: function(response) {

            if(response=='Error in classroom'){
                TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.DELETE_INSTRUCTOR_FAILURE_MESSAGE_CLASSROOM, bFadeOut:true});
                return;
            }else if(response=='Error in course'){
                TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.DELETE_INSTRUCTOR_FAILURE_MESSAGE_INFORMATION, bFadeOut:true});
                return;
            }
            var table = $('#instructor_table').DataTable();
            table.rows('.del').remove().draw( false );
            TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});
                //reset_modal();

        },error: function(data){
            alert('Error');
        }

    });

}
function loadInstructor(id){
    reset_modal();
    var arr;
    /*$("#"+id).parents("td.sorting_1").siblings().each(function (i, e){
        //alert($(e).text());
        switch(i){
            case 0:
                $("#firstName").val($(e).text());
            case 1:
                $("#lastName").val($(e).text());
            case 2:
                $("#email").val($(e).text());
            case 3:
                $("#phoneNo").val($(e).text());
        }


    });*/

    APP.AJAX({
        url: 'loadClassInstructor',
        type: "GET",
        cache: false,
        data:{instructorId: id},
        async: false,
        success: function(response) {
            obj = eval(response);
            $("#firstName").val(obj.firstName);
            $("#lastName").val(obj.lastName);
            $("#email").val(obj.email);
            $("#phoneNo").val(obj.phoneNo);
            $("#classInstructorId").val(id);
        }
    });

    $("#add-lesson-label").text('Edit Instructor');
    $("#"+id).closest('tr').addClass("update");
}
function emailVerify(courseType) {
    if(courseType=="5") {
        return true;
    }
    else if($("#presenter_email").val()=="") {
        return true;
    }
    var isEmailNotExist=true;
    APP.AJAX({
        url: 'checkInstructorEmail',
        type: "POST",
        cache: false,
        data: $('#frm_instructor').serialize(),
        async: false,
        success: function (response) {
            if(response){
                isEmailNotExist=false;
                TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.EMAIL_INSTRUCTOR_FAILURE_MESSAGE, bFadeOut:true});
            }

            /*if (response.email == 'Email exist') {
                TopMessageBar.displayMessageTopBar({
                    vType: 2,
                    vMsg: WLCMS_LOCALIZED.EMAIL_INSTRUCTOR_FAILURE_MESSAGE,
                    bFadeOut: true
                });
                return;
            }*/


        }

    });
    return isEmailNotExist;
}


function addClassInstructor(){
    firstNameValid = $("#frm_classinstructor_modal").validate().element("#firstName");
    lastNameValid = $("#frm_classinstructor_modal").validate().element("#lastName");
    emailValid = $("#frm_classinstructor_modal").validate().element("#email");
    phoneValid = $("#frm_classinstructor_modal").validate().element("#phoneNo");



    if( firstNameValid== false || lastNameValid == false || emailValid == false || phoneValid == false ){

        TopMessageBar.displayMessageTopBar({vType:2, vMsg: WLCMS_LOCALIZED.VALIDATION_NOT_SO_FAST});
        $('#submitClassInstuctor').removeAttr("data-dismiss");
        return false;

    }
    APP.AJAX({
        url: 'addClasInstructor',
        type: "POST",
        cache: false,
        data:{firstName: $("#firstName").val(), lastName: $("#lastName").val(),
            email: $("#email").val(),phoneNo: $("#phoneNo").val(),classInstructorId: $("#classInstructorId").val()},
        async: false,
        success: function(response) {
            if(response.email=='Email exist'){
                TopMessageBar.displayMessageTopBar({vType:2, vMsg:WLCMS_LOCALIZED.EMAIL_INSTRUCTOR_FAILURE_MESSAGE, bFadeOut:true});
                return;
            }
            var object = [ "<input id='"+response.id+"' type='checkbox' onclick=\"APP.CHECKBOX_WITH_BTN(this,false,'instructor-delete-btn')\" class='checks' value='"+response.id+"' name='classinstructor_checkboxes'>", "<a class='anchor' href='javascript:;' data-toggle='modal' data-target='#addClassInstructorModal' onclick='loadInstructor("+response.id+")'>"+response.firstName+"</a>", ""+response.lastName+"",""+response.email+"",""+response.phoneNo+""];
            if($("#classInstructorId").val()=='') {
                oTb.fnAddData(object);
                $('#submitClassInstuctor').attr('data-dismiss', 'modal');
            }else {
                var table = $('#instructor_table').DataTable();
                table.row('.update').data(object).draw(false);
                $("#" + response.id).closest('tr').removeClass('update');
                $('#submitClassInstuctor').attr('data-dismiss', 'modal');
            }

            TopMessageBar.displayMessageTopBar({vType:1, vMsg:WLCMS_LOCALIZED.SAVE_MESSAGE, bFadeOut:true});

        }
    });
}

function reset_modal()
{
    nameValid = $("#firstName").val("");
	stateValid = $("#lastName").val("");
	countryValid = $("#email").val("");
	phoneValid = $("#phoneNo").val("");
	$("label.error").hide();
    $(".error").removeClass("error");
    $('#msgdiv').html ('');
    $("#add-lesson-label").text('Add Instructor');
    $("#classInstructorId").val("");

}
