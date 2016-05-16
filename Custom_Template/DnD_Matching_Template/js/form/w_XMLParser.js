function setXml(){
	
	var xmlDoc = TEMPLATE.DATA.cloneNode(true);
	var errors = [];
	
	// =======================================================	Set mainText value
	var mainTextData = CKEDITOR.instances["insText_"+TEMPLATE.SID].getData();
	var $mainTextElement = $("#cke_insText_"+TEMPLATE.SID);
	SET_CDATA(xmlDoc,xmlDoc.getElementsByTagName("instructionText")[0],mainTextData);
	//***********	Begin - Validate For Blank
	if(TEMPLATE.VALIDATE.MATCH($mainTextElement,mainTextData,"",$mainTextElement.parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	//***********	End - Validate For Blank
	
	
	// =======================================================	Set promptAudio value
	var $promptAudio = $("#inputform_"+TEMPLATE.SID).find(".promptAudio > .asset");
	var promptAudioData = TEMPLATE.GET_ASSET_DATA($promptAudio);
	var promptAudioNode = xmlDoc.getElementsByTagName("promptAudio")[0];
	if(typeof promptAudioData.path == "undefined")
	{
		REMOVE_CDATA(promptAudioNode);
	}
	else
	{
		SET_CDATA(xmlDoc,xmlDoc.getElementsByTagName("promptAudio")[0],promptAudioData.path);
	}
	promptAudioNode.setAttribute('name', promptAudioData.name);
	promptAudioNode.setAttribute('ver', promptAudioData.ver);
	
	
	// ==========================================================================================
	//	Get option nodes ****************************
	var optionsNode = xmlDoc.getElementsByTagName("options")[0];
	
	//	Clear All Data from Children Nodes
	var optNode;
	for (var i=0; i<optionsNode.childNodes.length; i++)
	{
		optNode = optionsNode.childNodes[i];
		REMOVE_CDATA(optNode.childNodes[0]);
		REMOVE_CDATA(optNode.childNodes[1]);
		REMOVE_CDATA(optNode.childNodes[2]);
		REMOVE_CDATA(optNode.childNodes[3]);
		optNode.childNodes[3].setAttribute("name","");
		optNode.childNodes[3].setAttribute("ver","");
		REMOVE_CDATA(optNode.childNodes[4]);
		REMOVE_CDATA(optNode.childNodes[5]);
		optNode.childNodes[5].setAttribute("name","");
		optNode.childNodes[5].setAttribute("ver","");
	}
	
	//	Inset New option nodes in xmlDoc from tables ****************************
	var $options = $("#inputform_"+TEMPLATE.SID).find(".options");
	
	//***********	Begin - Options
	var msg = TEMPLATE.VALIDATE.MSG.BLANK;
	TEMPLATE.VALIDATE.MSG.BLANK = "<div class='error'>There should be at least two 'Drop Spot' to proceed.</div>";
	var divClass = TEMPLATE.VALIDATE.MSG_DIV;
	TEMPLATE.VALIDATE.MSG_DIV = ".errPrepend_0";
	if(TEMPLATE.VALIDATE.MATCH($options.parent().parent(),$options.children().length,"0|1|2",$options.parent().parent().parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	TEMPLATE.VALIDATE.MSG_DIV = divClass;
	TEMPLATE.VALIDATE.MSG.BLANK = msg;
	//***********	End - Options
	
	var dontHideMsg = "undefined";
	$options.children().each(function(i,elem){
		if($(this).attr("class") != "hide")
		{
			//	Get elements
			var $dropText = $(this).find(".dropText > .content");
			var $draggableObject = $(this).find(".draggableObject > .content");
			var $feedback = $(this).find(".feedback > .content");
			var $feedbackAudio = $(this).find(".feedbackAudio > .content");
			var $hint = $(this).find(".hint > .content");
			var $hintAudio = $(this).find(".hintAudio > .content");
			
			//	Get data
			var dropText = $dropText.html();
			var draggableObject = $draggableObject.html();
			var feedback = $feedback.html();
			var feedbackAudio = TEMPLATE.GET_ASSET_DATA($feedbackAudio);
			var hint = $hint.html();
			var hintAudio = TEMPLATE.GET_ASSET_DATA($hintAudio);
			
			optNode = optionsNode.childNodes[i-1];
			SET_CDATA(xmlDoc,optNode.childNodes[0],dropText);
			SET_CDATA(xmlDoc,optNode.childNodes[1],draggableObject);
			SET_CDATA(xmlDoc,optNode.childNodes[2],feedback);
			
			SET_CDATA(xmlDoc,optNode.childNodes[3],feedbackAudio.path);
			optNode.childNodes[3].setAttribute("name",feedbackAudio.name);
			optNode.childNodes[3].setAttribute("ver",feedbackAudio.ver);
			
			SET_CDATA(xmlDoc,optNode.childNodes[4],hint);
			
			SET_CDATA(xmlDoc,optNode.childNodes[5],hintAudio.path);
			optNode.childNodes[5].setAttribute("name",hintAudio.name);
			optNode.childNodes[5].setAttribute("ver",hintAudio.ver);
			
			//***********	Begin - Each Option
			if(TEMPLATE.VALIDATE.MATCH($dropText.parent(),dropText,"| ",$dropText.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg))
			{
				dontHideMsg = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($draggableObject.parent(),draggableObject,"| ",$draggableObject.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg))
			{
				dontHideMsg = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($feedback.parent(),feedback,"| ",$feedback.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg))
			{
				dontHideMsg = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($hint.parent(),hint,"| ",$hint.parent().parent().parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg))
			{
				dontHideMsg = true;
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

function SET_CDATA(xmlDoc,Node,Data)
{
	REMOVE_CDATA(Node);
	var CDATA = xmlDoc.createCDATASection(Data);
	Node.appendChild(CDATA);
}

function REMOVE_CDATA(Node)
{
	if (Node.hasChildNodes())
	{
		Node.removeChild(Node.childNodes[0]);
	}
}