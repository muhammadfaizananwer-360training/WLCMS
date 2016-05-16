// JavaScript Document


// ********************************************	Colapse Panel
function makeColapsePanel(){
	$(".FormWrapper .Panel").each(function(i){
		//	Set ID
		$(this).attr("id","Panel_"+i);
		
		var getHTML = $(this).find(".PanelHeader").html();
		$(this).find(".PanelHeader").html("<div onclick='toggleBtn(this);' class='PanelToggleBtn'><div class='PanelHeaderToggleImage'><img src='UI/formUI/arrow2.png' alt=''/></div><div class='PanelHeaderText'>"+getHTML+"</div></div>");
	});
	
	document.getElementById("loader").style.display = "none";
}

function toggleBtn(e){
	var togglePanel = e.parentNode.parentNode.getElementsByTagName("div")[4].style.display;
	
	if(togglePanel == "block"){
		e.getElementsByTagName("div")[0].innerHTML = "<img src='UI/formUI/arrow2.png' alt=''/>";
		e.parentNode.parentNode.getElementsByTagName("div")[4].style.display = "none";
	}else{
		e.getElementsByTagName("div")[0].innerHTML = "<img src='UI/formUI/arrow.png' alt=''/>";
		e.parentNode.parentNode.getElementsByTagName("div")[4].style.display = "block";
	}
}

// ********************************************	popups - Text Editor / Asset Search
var getTrg;

function textEditor(trg){
	getTrg = trg;
	window.open("textEditor.html","TextEditor", "width=600,height=480,toolbar=no");
}

function getParentTextField(){
  return getTrg;
}

function setParentTextField(HTML){
	//validation(getTrg.parent(),HTML,getTrg.parent().parent().parent().parent().parent().parent(),true);
	FormModified();
	getTrg.html(HTML);
}

function openAssetPopup(txtObj, type){
	var url = location.href.substring(location.href.indexOf("?url="));
	url = url.replace("?url=","");
	window.open(url + "&fieldid=optionAudio" + txtObj.id + "&type="+type, "win", "width=730,height=480,toolbar=no");
}

function openAssetPopupForStaticFields(strFieldId, type){
	var url = location.href.substring(location.href.indexOf("?url="));
	url = url.replace("?url=","");
	window.open(url + "&fieldid=" + strFieldId + "&type="+type, "win", "width=730,height=480,toolbar=no");
}

function deleteAsset(trg,constantTitle){
	var TRG = $(trg.parentNode.parentNode);
	var assetName = TRG.find(constantTitle+"Span");
	
	if(assetName.val() != ""){
	
		if(confirm("Are you sure you want to delete this asset?")){
			assetName.val("");
			TRG.find(constantTitle+"Ver").val("");
			TRG.find(constantTitle).val("");
			FormModified();
		}
	
	}
}