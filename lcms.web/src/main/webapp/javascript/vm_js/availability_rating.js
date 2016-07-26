    var now=new Date();
    var datePicker_1;
    var datePicker_2;
    var singleTone_1 = false;
    var singleTone_2 = false;
    var checks = [false,[false,false]];

$(document).ready(function(){

	reviewsMarkedReady = [];
	$('#CourseRating_table').hide();
	enableDisablePublishButn(false);
	$('#PublishAll').hide();
	$('#resultTitle').hide();

	APP.PLACEHOLDER_FIX();
	APP.DATE_PICKER("#datePicker_1");
	APP.BODY_COLLAPSES();

	$("#datePicker_1 > input").val("");
	$("#datePicker_2 > input").val("");
	$("#searchBtn").attr("disabled","disabled");

	$("#datePicker_1").on('changeDate', function(ev){

		if(ev.viewMode === "days")
		{
			datePicker_1 = new Date(ev.date);
			checks[0] = datePicker_1.setHours(0,0,0,0) <= now.setHours(0,0,0,0);
			if(datePicker_2 !== null && datePicker_2 !== undefined){
				checks[1][0] = datePicker_2.setHours(0,0,0,0) >= datePicker_1.setHours(0,0,0,0);
			}

			if(!singleTone_1)
			{
				singleTone_1 = true;
				APP.DATE_PICKER("#datePicker_2");
				$("#datePicker_2 > span > button").removeClass("disabled");
			}
			$(this).datepicker('hide');
		}
	});

	$("#datePicker_2").on('changeDate', function(ev){

		if(ev.viewMode === "days")
		{
			datePicker_2 = new Date(ev.date);

			checks[1][0] = datePicker_2.setHours(0,0,0,0) >= datePicker_1.setHours(0,0,0,0);
			checks[1][1] = datePicker_2.setHours(0,0,0,0) > now.setHours(0,0,0,0);

			if(!singleTone_2)
			{
				singleTone_2 = true;
				var $sBtn = $("#searchBtn");
				$sBtn.removeAttr("disabled");
				$sBtn.removeClass("default");
				$sBtn.addClass("blue");
			}
			$(this).datepicker('hide');
		}
	});

});

$("#searchBtn").click(function()
		{
			var $d1 = $("#datePicker_1");
			var $d2 = $("#datePicker_2");
			$d1.parent().find("label.error").remove();
			$d1.find("input.form-control").removeClass("error");
			$d2.parent().find("label.error").remove();
			$d2.find("input.form-control").removeClass("error");

			if(!checks[0])
			{
				$d1.find("input.form-control").addClass("error");
				$d1.parent().append("<label class='error'><small>This date cannot be later than today's date.</small></label>");
				return;
			}

			if(!checks[1][0])
			{
				$d2.find("input.form-control").addClass("error");
				$d2.parent().append("<label class='error'><small>The 'To' date cannot be earlier than the 'From' date.</small></label>");
				return;
			}

			if(checks[1][1])
			{
				$d2.find("input.form-control").addClass("error");
				$d2.parent().append("<label class='error'><small>The 'To' date cannot be later than today's date.</small></label>");
				return;
			}
			serachRating();
		});


function serachRating(){

	var date1 = $('[name="datePicker_1"]').val();
	var date2 =$('[name="datePicker_2"]').val();
	var NPSRating = $('#NPSRating').val();
	var hideNullReview = $('#chkHideNull').prop('checked')?1:0;
	var rowNo = 1;
	reviewsMarkedReady = [];

	 showProgressLoader("<div id='loader-label'>Loading...</div>");
	 $('#CourseRating_table').show();
	 $('#resultTitle').show();

	 var oTable = $("#CourseRating_table").dataTable({
	        "bServerSide": false,
	        "sAjaxSource": "/lcms/getRatingCourseSearch?" + 'datePicker_1='+date1 +'&datePicker_2='+date2+'&NPSRating='+$("#NPSRating").val()+'&chkHideNull='+hideNullReview,
	        "bProcessing": false,
	        "bJQueryUI": false,
	        "ordering": true,
	        "emptyTable": "No search result(s) found",
	        "zeroRecords": "No search result(s) found",
	        "bDeferRender": true,
            "aLengthMenu": [
                [5, 20, 30, -1],
                [5, 20, 30, "All"]
            ],
            "searching": false,
            "bDestroy": true,
            "iDisplayLength": 20,
            "sPaginationType": "bootstrap",
            "oLanguage": {
                "sLengthMenu": "Show _MENU_",
                "oPaginate": {
                    "sPrevious": "Prev",
                    "sNext": "Next"
                }
            },

              aoColumnDefs: [
          { bSortable: false, aTargets: [ 3 ] }],
    "bAutoWidth": false, // Disable the auto width calculation
	        "bLengthChange": false,
	        "aoColumns": [
		                      {"mData": "rowNo", "sWidth": "4px"
		                      },
		                      {"mData": "submission_date", "sWidth": "80px",
		                       "mRender": function (data, type, full) {

		                    	   var detailMethod =  ["GetRatingDetail('"];
		                    	   detailMethod.push(full.NpsRating);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.userReviewText);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.rating_course);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingShoppingExp);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingLearningTech);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingCS);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingCourseSecondary);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingShoppingExpSecondary);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingLearningTectSecondary);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.RatingCSSecondary);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.PublishStatus);
		                    	   detailMethod.push("','");
		                    	   detailMethod.push(full.CourseName);
		                    	   detailMethod.push("','" + full.CourseBusinessKey + "','");
		                    	   detailMethod.push(full.RatingCourseSecondary_Q);
		                    	   detailMethod.push("','" + full.RatingShoppingExpSecondary_Q + "','");
		                    	   detailMethod.push(full.RatingLearningTectSecondary_Q);
		                    	   detailMethod.push("','" + full.RatingCSSecondary_Q + "','");
		                    	   detailMethod.push(full.CourseId);
		                    	   detailMethod.push("','" + full.EnrollmentId + "','");
		                    	   detailMethod.push(full.ratingId + "')");

		                    		 return '<a class="anchor" href="javascript:;" onclick="' + detailMethod.join('') + '" data-toggle="modal"  data-backdrop="static" data-keyboard="false" data-target="#furtherDetailsModal">' + data + '</a>';
		                    	  }
		                      },
		                      { "mData": "publish_status", "sWidth": "80px",
		                    	"mRender": function (data, type, full) {
		                    		  var rewview_row = "review_" + full.ratingId;
		                    		  var returnTxt = '';
		                    		  if(data === '' || data === 'undefined'){
		                    			  returnTxt = '<span id=' + rewview_row + ' class="">NEW</span>';
		                    		  }else if(data === 'READY'){
		                    			  returnTxt = '<span id=' + rewview_row + ' class="yellow-text">READY</span>';
		                    			  addReviewsId("" + full.ratingId);
		                    		  }else if(data === 'DENIED'){
		                    			  returnTxt = '<span id=' + rewview_row + ' class="red-text">DENIED</span>';
		                    		  }else{
		                    			  returnTxt = '<span id=' + rewview_row + ' class="green-text">PUBLISHED</span>';
		                    		  }
		                    		  return returnTxt;
		                          }
		                      },
							  {"sWidth": "70%" , "mRender": function (data, type, full) {return '';}}
	                     ],
		      "initComplete": function( settings, json ) {
		    	  if(json.iTotalRecords > 0){
		    		  $('#PublishAll').show();
		    	  }else{
		    		  $('#PublishAll').hide();
		    	  }
		    	  hideProgressLoader();
		    	  if(reviewsMarkedReady.length > 0){
		    			enableDisablePublishButn(true);
		    	   }else{
		    		   enableDisablePublishButn(false);
		    	   }
		      },
			  "fnServerData": function ( sSource, aoData, fnCallback, oSettings ) {
					  oSettings.jqXHR = APP.AJAX( {
						"dataType": 'json',
						"type": "GET",
						"url": "getRatingCourseSearch?" + 'datePicker_1='+date1 +'&datePicker_2='+date2+'&NPSRating='+$("#NPSRating").val()+'&chkHideNull='+hideNullReview,
						"data": aoData,
						"success": fnCallback
					  } );
				}
	    });
}
$(function() {

	var nowTemp = new Date();
	var future = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

	$.validator.addMethod('futuredate', function(value, ele) {
		var dsplit = value.split("-");
		var valueDateobj = new Date( dsplit[2],dsplit[0]-1,dsplit[1],0, 0, 0, 0);
		var validDate = false;
		if ( valueDateobj > future ) {
			validDate = true;
		}

		return validDate;
	}, 'Course expiration must be a future date.');

	$.validator.addMethod('Todate_Less_FromDate', function(value, ele) {
		console.log('value:'+value);
		var dateFrom = value.split("-");
		var dateTo = $('#datePicker_2').find('input').val().split("-");
		if(dateFrom==="" || dateTo==="")
			{
				return false;
			}

		var valueDateFrom = new Date( dateFrom[2],dateFrom[0]-1,dateFrom[1],0, 0, 0, 0);
		var valueDateTo = new Date( dateTo[2],dateTo[0]-1,dateTo[1],0, 0, 0, 0);
		var validDate = false;
		if ( dateTo > dateFrom ) {
			validDate = true;
		}

		return validDate;
	}, 'From Date must be less than To Date.');

	$("#frm_availability").validate({
		rules: {
			datePicker_1:{
				required : true
			}
			,
			datePicker_2:{
				required : true
			}
			,
			NPSRating :{
				required : false
			}
		},
		messages: {
			NPSRating: {
			      required: "Please select an option from the list"
			     },
	     datePicker_1:{
				required : "Please select From Date"
			}
			,
			datePicker_2:{
				required : "Please select To Date"
			}
		},
		errorPlacement: function(error, element) {
			error.insertAfter(element);
		},
		submitHandler: function(form){
			console.log('submitting form');
			form.submit();
		},
		invalidHandler: function(event, validator) {
			var errors = validator.numberOfInvalids();
			if (errors) {
			$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Not so fast! Required fields are missing. Complete the fields, then click <strong>Save</strong>.</div>');
			} else {
				$("#msgdiv").html('');
			}
			elementFadeOut('.messages');
			elementFadeOut("#msgdiv");
			enableDisablePublishButn(false);
		}
	});
});

$('.btn-sub-group').click(function()
 {
	$('#msgdiv').html('<div class=\'alert alert-danger alert-dismissible fade in\' >	<button type=\'button\' class=\'close\' data-dismiss=\'alert\'><span aria-hidden=\'true\'>&#215;</span><span class=\'sr-only\'>Close</span></button>Hold On! Please save the overview information before navigating to other areas of your new course.</div>');
	elementFadeOut("#msgdiv");
  });

function validateForm () {
	$("#frm_availability").valid ();
}

function elementFadeOut(id){
	setTimeout(function(){$(id).html('');},3000);
}
function showProgressLoader(message){
	$("#loader-overlay").html(message);
	$("#bg-loader").show();
	$("#loader-overlay").show();
}
function hideProgressLoader(){
	$("#loader-overlay").fadeOut();
	$("#bg-loader").fadeOut();
}
function addReviewsId(id){
	var idx = $.inArray(id, reviewsMarkedReady);
	if (idx === -1) {
		reviewsMarkedReady.push(id);
	}
	if(reviewsMarkedReady.length > 0){
		enableDisablePublishButn(true);
	}
}
function removeReviewsId(id){
	var idx = $.inArray(id, reviewsMarkedReady);
	if (idx > -1) {
		reviewsMarkedReady.splice(idx, 1);
	}
	if(reviewsMarkedReady.length < 1){
		enableDisablePublishButn(false);
	}
}
function showConfirmationModal(){

	$trgModal = $("#confirmationModal");

	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';

	var msg = '<p>All reviews with READY status will be made visible on the storefront. Do you want to publish?</p>';

	var btns = '<button type="button" class="btn blue" onclick="PublishAllCourse()" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="" data-dismiss="modal">NO</button>';

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');
}
function enableDisablePublishButn(elem){

	if(elem === true){
		$("#PublishAll").attr("disabled", false);
	}else{
		$("#PublishAll").attr("disabled", true);
	}
}