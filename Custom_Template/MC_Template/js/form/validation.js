// JavaScript Document
var blankMsg = "<div class='blankErr'><img src='UI/formUI/validationSign.png' alt'' />&nbsp;&nbsp; The indicated fields must be completed.</div>";
var rowMsg = "<div class='optErr'><img src='UI/formUI/validationSign.png' alt'' />&nbsp;&nbsp; At least one option choice is required.</div>";
var errPanelArr = new Array();

function rowValidation(getRows,minRow,panel){
	var msgDiv = panel.find('.PanelText .validationMsg');
	var msgRowDiv = msgDiv.find('.optErr');
	
	if(!Boolean(getRows > minRow)){

		//	----------------------------
		//	Register error panel
		//	----------------------------
		registerErrPanel (panel.attr("id"));
		
		
		if(msgRowDiv.html() == null){
			msgDiv.html(rowMsg);
			panel.css("border","1px solid #F00");
		}

	}
	return Boolean(getRows > minRow);
}

function blankValidation(trg,value,panel,isUpdate){
	var msgDiv = panel.find('.PanelText .validationMsg');
	var msgBlankDiv = msgDiv.find('.blankErr');
	
	//	----------------------------
	//	Validation for Empty Fields	
	//	----------------------------
	if(value == ""){

		//	----------------------------
		//	Register error panel
		//	----------------------------
		registerErrPanel (panel.attr("id"));
		
		if(msgBlankDiv.html() == null){
			msgDiv.html(blankMsg);
			panel.css("border","1px solid #F00");
		}
		
		trg.css("border","1px solid #F00");
	}else{
		//	----------------------------
		//	Release error panel
		//	----------------------------
		if(isErrFreePanel (panel.attr("id"), isUpdate)){
			panel.removeAttr("style");
			msgDiv.html("");
		}
		trg.removeAttr("style");
	};
}

function registerErrPanel (panelId){
	//	Checking err panel ID, is already registered or not?
	var getElementID = findIn2dArray(errPanelArr, panelId);
	
	if(getElementID == -1){
		//	Register for new
		//alert("Register "+panelId);
		errPanelArr.push([panelId,1]);
	}else{
		//	Count the number of errors
		var countErr = Number(errPanelArr[getElementID][1]);
		countErr++;
		errPanelArr[getElementID][1] = countErr;
		//alert(countErr);
	}
}

function findIn2dArray(arr_2d, val){
    var indexArr = $.map(arr_2d, function(arr, i) {
            if($.inArray(val, arr) != -1) {
                return 1;
            }

            return -1;
    });

	if(!Array.indexOf){
		Array.prototype.indexOf = function(obj){
			for(var i=0; i<this.length; i++){
				if(this[i]==obj){
					return i;
				}
			}
			return -1;
		}
	}

    return indexArr.indexOf(1);
}

function isErrFreePanel (panelId,isUpdate){
	//	Checking err panel ID, is already registered or not?
	var getElementID = findIn2dArray(errPanelArr, panelId);
	if(getElementID != -1){
		//	Count the number of errors
		var countErr = Number(errPanelArr[getElementID][1]);
		
		if(isUpdate){
			countErr--;
			errPanelArr[getElementID][1] = countErr;
			if(!Boolean(countErr)){
				//	Eliminate
				errPanelArr = element_Eliminate (errPanelArr,[panelId,0]);
			}
		}
	}
	return !Boolean(countErr);
}

function element_Eliminate (Arr, E) {
	var tempArr = new Array();
	var Zloop = 0;

	for (var Z=0; Z<Arr.length;Z++) {
		if(String(Arr[Z]) != String(E)){
			tempArr[Zloop] = Arr[Z];
			Zloop++;
		}
	}
	return tempArr;
}