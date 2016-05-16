function setXml(){
	
	var xmlDoc;
	xmlDoc = TEMPLATE.DATA.documentElement.cloneNode(true);
	
	var errors = [];
	
	// =======================================================	Set mainText value
	var mainTextNode = xmlDoc.getElementsByTagName("mainText")[0].childNodes[0];
	var mainTextData = CKEDITOR.instances["mainText_"+TEMPLATE.SID].getData();
	mainTextNode.data = mainTextData;
	var $mainTextElement = $("#cke_mainText_"+TEMPLATE.SID);
	//var mainTextPlain = CKEDITOR.instances["mainText_"+TEMPLATE.SID].document.getBody().getText();
	
	//***********	Begin - Validate For Blank
	if(TEMPLATE.VALIDATE.MATCH($mainTextElement,mainTextData,"",$mainTextElement.parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	//***********	End - Validate For Blank
	
	// =======================================================	Set questionText value
	var questionTextNode = xmlDoc.getElementsByTagName("questionText")[0].childNodes[0];
	var questionTextData = CKEDITOR.instances["questionText_"+TEMPLATE.SID].getData();
	questionTextNode.data = questionTextData;
	var $questionTextElement = $("#cke_questionText_"+TEMPLATE.SID);
	
	//***********	Begin - Validate For Blank
	if(TEMPLATE.VALIDATE.MATCH($questionTextElement,questionTextData,"",$questionTextElement.parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	//***********	End - Validate For Blank
	
	// =======================================================	Set promptAudio value
	var $promptAudio = $("#inputform_"+TEMPLATE.SID).find(".promptAudio > .asset");
	var promptAudioData = TEMPLATE.GET_ASSET_DATA($promptAudio);
	xmlDoc.getElementsByTagName("promptAudio")[0].childNodes[0].data = promptAudioData.path;
	xmlDoc.getElementsByTagName("promptAudio")[0].setAttribute('name', promptAudioData.name);
	xmlDoc.getElementsByTagName("promptAudio")[0].setAttribute('ver', promptAudioData.ver);
	
	//***********	Begin - Asset Check
	/*if(typeof $promptAudio.html() == "undefined")
	{
		$promptAudio = $("#inputform_"+TEMPLATE.SID).find(".promptAudio");
		TEMPLATE.VALIDATE.MATCH($promptAudio.parent().parent(),"","",$promptAudio.parent().parent().parent().parent().parent().parent().parent());
		errors.push(0);
	}
	else
	{
		$promptAudio = $("#inputform_"+TEMPLATE.SID).find(".promptAudio");
		TEMPLATE.VALIDATE.MATCH($promptAudio.parent().parent(),"done","",$promptAudio.parent().parent().parent().parent().parent().parent().parent());
	}*/
	//***********	End - Asset Check
	
	
	// =======================================================	Set Image value
	var $leftImage = $("#inputform_"+TEMPLATE.SID).find(".leftImage > .asset");
	var leftImageData = TEMPLATE.GET_ASSET_DATA($leftImage);
	xmlDoc.getElementsByTagName("leftImage")[0].childNodes[0].data = leftImageData.path;
	xmlDoc.getElementsByTagName("leftImage")[0].setAttribute('name', leftImageData.name);
	xmlDoc.getElementsByTagName("leftImage")[0].setAttribute('ver', leftImageData.ver);
	
	//***********	Begin - Asset Check
	if(typeof $leftImage.html() == "undefined")
	{
		$leftImage = $("#inputform_"+TEMPLATE.SID).find(".leftImage");
		TEMPLATE.VALIDATE.MATCH($leftImage.parent().parent(),"","",$leftImage.parent().parent().parent().parent().parent().parent().parent());
		errors.push(0);
	}
	else
	{
		$leftImage = $("#inputform_"+TEMPLATE.SID).find(".leftImage");
		TEMPLATE.VALIDATE.MATCH($leftImage.parent().parent(),"done","",$leftImage.parent().parent().parent().parent().parent().parent().parent());
	}
	//***********	End - Asset Check
	
	
	// ==========================================================================================
	//	Get copy of first option node ****************************
	var optionsNode = xmlDoc.getElementsByTagName("options")[0];
	var cloneNode = optionsNode.firstChild;
	
	//	Remove All Children Nodes
	while (optionsNode.firstChild) {
		optionsNode.removeChild(optionsNode.firstChild);
	}
	
	//	Inset New option nodes in xmlDoc from tables ****************************
	var $options = $("#inputform_"+TEMPLATE.SID).find(".options");
	
	//***********	Begin - Options
	if(TEMPLATE.VALIDATE.MATCH($options.parent().parent(),$options.children().length,"1",$options.parent().parent().parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	//***********	End - Options
	
	$options.children().each(function(i,elem){
		if($(this).attr("class") != "hide")
		{
			//	Get elements
			var $optionText = $(this).find(".optionText > .content");
			var $feedBack = $(this).find(".feedBack > .content");
			var $optionAudio = $(this).find(".optionAudio > .content");
			var $optionStatus = $(this).find(".optionStatus > select");
			
			//	Get data
			var optionTextData = $optionText.html();
			var feedBackData = $feedBack.html();
			var audioData = TEMPLATE.GET_ASSET_DATA($optionAudio);
			var statusData = $optionStatus.val();
			
			//	Set values in xml
			cloneNode.setAttribute("id",i);
			cloneNode.getElementsByTagName("optionText")[0].childNodes[0].data = optionTextData;
			cloneNode.getElementsByTagName("feedBack")[0].childNodes[0].data = feedBackData;
			cloneNode.getElementsByTagName("optionAudio")[0].childNodes[0].data = audioData.path;
			cloneNode.getElementsByTagName("optionAudio")[0].setAttribute("name",audioData.name);
			cloneNode.getElementsByTagName("optionAudio")[0].setAttribute("ver",audioData.ver);
			cloneNode.getElementsByTagName("optionStatus")[0].setAttribute("value",statusData);
			
			//	Append with xml
			optionsNode.appendChild(cloneNode.cloneNode(true));
			
			//***********	Begin - Each Option
			if(TEMPLATE.VALIDATE.MATCH($optionText.parent(),optionTextData,"",$optionText.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent()))
			{
				errors.push(0);
			}
			if(TEMPLATE.VALIDATE.MATCH($feedBack.parent(),feedBackData,"",$feedBack.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent()))
			{
				errors.push(0);
			}
			//***********	End - Each Option
		};
	});
	
	TEMPLATE.IS_VALID = Boolean(errors.length == 0);
	
	if(TEMPLATE.IS_VALID){
		TEMPLATE.DATA = TEMPLATE.XML_TO_STRING(xmlDoc);
		document.getElementById("txtXml").value = TEMPLATE.DATA;	//	For Testing
	}else{
		document.getElementById("txtXml").value = TEMPLATE.IS_VALID;
	}
}