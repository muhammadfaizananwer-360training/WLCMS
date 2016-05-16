// JavaScript Document

// ********************************************	Add Row
var trCount = 1;
var isFirstTime = true;

function addRow(tableID){
	var table = $("#"+tableID+" tr");
	var L = table.length-1;
	
	if(trCount < L){
		trCount = L;
	}else{
		trCount++;
	}
	
	var getRow = table.eq(1);
	var newRow = $(getRow).last().clone();
	newRow.removeAttr("style");
	newRow.find('td').children().each(function(i){
	
	switch($(this).attr("name")){
		case "option":
				$(this).removeAttr("checked");
				$(this).attr("id",  "option" + trCount);
			break;
		case "optionAudio":
			$(this).attr("id",  "optionAudio" + trCount);
			break;
		case "optionAudioSpan":
			$(this).attr("id",  "optionAudio" + trCount + "Span");
			break;
		case "optionAudioVer":
			$(this).attr("id",  "optionAudio" + trCount + "Ver");
			break;
		case "optionAudioName":
			$(this).attr("id",  trCount);
			break;
		default:
			//	Set ID for every td objects
			$(this).attr("id",$(this).attr("name") + trCount);
	}
	
	if($(this).attr("name") != "optionStatus"){
		
		try{
			$(this).val("");
		}catch(e){
		
		}
		
		try{
			$(this).html("");
		}catch(e){}
	
		}
	});

	//	Append new row with the table
	var getDataSetsDiv = "#"+tableID+" tbody";
	$(getDataSetsDiv).append(newRow);
	
	FormModified();
}

// ********************************************	Delete Row
function deleteRow(tableID) {

	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		var canDelete = false;
		
		if(isFirstTime){
			isFirstTime = false;
			trCount = rowCount - 2;
		}
		
		for(var i=0; i<rowCount; i++) {
			
			var row = table.rows[i];
			var chkbox = row.cells[0].childNodes[0];
			
			if(chkbox != null && chkbox.checked == true && chkbox.getAttribute("id") != "checkAll") {
				if(!canDelete){
					//	Just Ask in first time
					if(confirm("Are you sure you want to delete selected item(s)?")){
						canDelete = true;
						
						//^^^^^^^^^^^^^^^^^^^^^^^^^^^^ LCMS-10365
						document.getElementById("checkAll").checked = false;
						//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
						
						FormModified();
					}else{
						//	Terminate this function if cancel
						return;
					}
				}
				
				if(canDelete){
					table.deleteRow(i);
					rowCount--;
					i--;
				}
			}
		}
	}catch(e) {
		alert(e);
	}
}

// ********************************************	Radio button fucntionality on check box
var currentCheckItemId = "";

function radioFn(chk){
	if(chk.checked){
		try{
			document.getElementById(currentCheckItemId).checked = false;
		}catch(e){
			
		}
		currentCheckItemId = chk.id;
	}else{
		currentCheckItemId = "";
	}
}

// ********************************************	Check All
function checkAll(chk){
	$(chk).parent().parent().parent().find('.gridSNoCell').each(function(index,element){
		//alert(index + " " + (index > 0));
		if( index > 0){
			if(chk.checked){
				$(this).find('input').attr('checked','checked');
			}else{
				$(this).find('input').removeAttr('checked');
			}
		}
	});
}