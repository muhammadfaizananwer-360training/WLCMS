function setXml(){
	
	var xmlDoc = TEMPLATE.DATA.cloneNode(true);
	var errors = [];
	var $form = $("#inputform_"+TEMPLATE.SID);
	
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
	var $promptAudio = $form.find(".promptAudio > .asset");
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
	
	
	// =======================================================	Set column values
	var columnNode = xmlDoc.getElementsByTagName("column")[0];
	var clonelistNode = columnNode.firstChild;
	var dontHideMsg_1 = "undefined";
	
	//	Remove All Children Nodes
	while (columnNode.firstChild) {
		columnNode.removeChild(columnNode.firstChild);
	}
	
	var $column = $form.find("tBody.column");
	$column.children().each(function(i){
		
		var $trg = $(this);
		if($trg.attr("class") != "hide")
		{
			var $catElm = $trg.find("input.cat-title");
			var catData = $catElm.val();
			SET_CDATA(xmlDoc,clonelistNode,catData);
			clonelistNode.setAttribute("value",i);
			columnNode.appendChild(clonelistNode.cloneNode(true));
			
			//***********	Begin - Validate For Blank
			if(TEMPLATE.VALIDATE.MATCH($catElm,catData,"| ",$column.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_1))
			{
				dontHideMsg_1 = true;
				errors.push(0);
			}
			//***********	End - Validate For Blank
		}
	});
	
	// =======================================================	Set Venn BG
	var vennBgNode = xmlDoc.getElementsByTagName("vennDiagram")[0];
	var $vennBg = $form.find("#venn_bg_"+TEMPLATE.SID);
	var isChecked = ($vennBg.prop("checked"))?1:0;
	SET_CDATA(xmlDoc,vennBgNode,isChecked);	
	
	
	// =======================================================	Set Options	
	//	Get option nodes ****************************
	var optionsNode = xmlDoc.getElementsByTagName("options")[0];
	var cloneOptNode = optionsNode.firstChild;
	
	//	Remove All Children Nodes
	while (optionsNode.firstChild)
	{
		optionsNode.removeChild(optionsNode.firstChild);
	}
	
	//	Inset New option nodes in xmlDoc from tables ****************************
	var $options = $form.find(".options");
	
	//***********	Begin - Options
	var msg = TEMPLATE.VALIDATE.MSG.BLANK;
	TEMPLATE.VALIDATE.MSG.BLANK = "<div class='error'>There must be at least one 'Draggable Object' to proceed.</div>";
	var divClass = TEMPLATE.VALIDATE.MSG_DIV;
	TEMPLATE.VALIDATE.MSG_DIV = ".errPrepend_0";
	if(TEMPLATE.VALIDATE.MATCH($options.parent().parent(),$options.children().length,"0|1",$options.parent().parent().parent().parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	TEMPLATE.VALIDATE.MSG_DIV = divClass;
	TEMPLATE.VALIDATE.MSG.BLANK = msg;
	//***********	End - Options
	
	var dontHideMsg_2 = "undefined";
	$options.children().each(function(i,elem){
		var $this = $(this);
		if($this.attr("class") != "hide")
		{
			//	Get elements
			var $draggableObject = $this.find(".draggableObject > .content");
			var $correctCategory = $this.find(".correctCategory > select");
			var $correctFeedback = $this.find(".correctFeedback > .content");
			var $correctFeedbackAudio = $this.find(".correctFeedbackAudio > .content");
			var $incorrectFeedback = $this.find(".incorrectFeedback > .content");
			var $incorrectFeedbackAudio = $this.find(".incorrectFeedbackAudio > .content");
			
			//	Get data
			var draggableObject = $draggableObject.html();
			var correctCategory = $correctCategory.val();
			var correctFeedback = $correctFeedback.html();
			var correctFeedbackAudio = TEMPLATE.GET_ASSET_DATA($correctFeedbackAudio);
			var incorrectFeedback = $incorrectFeedback.html();
			var incorrectFeedbackAudio = TEMPLATE.GET_ASSET_DATA($incorrectFeedbackAudio);
			
			var optNode = cloneOptNode;//optionsNode.childNodes[i-1];
			SET_CDATA(xmlDoc,optNode.childNodes[0],draggableObject);
			optNode.childNodes[1].setAttribute("columnListId",correctCategory);
			SET_CDATA(xmlDoc,optNode.childNodes[2],correctFeedback);
			
			SET_CDATA(xmlDoc,optNode.childNodes[3],correctFeedbackAudio.path);
			optNode.childNodes[3].setAttribute("name",correctFeedbackAudio.name);
			optNode.childNodes[3].setAttribute("ver",correctFeedbackAudio.ver);
			
			SET_CDATA(xmlDoc,optNode.childNodes[4],incorrectFeedback);
			
			SET_CDATA(xmlDoc,optNode.childNodes[5],incorrectFeedbackAudio.path);
			optNode.childNodes[5].setAttribute("name",incorrectFeedbackAudio.name);
			optNode.childNodes[5].setAttribute("ver",incorrectFeedbackAudio.ver);
			
			//***********	Begin - Each Option
			if(TEMPLATE.VALIDATE.MATCH($draggableObject.parent(),draggableObject,"| ",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($correctCategory.parent(),correctCategory,"0",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($correctFeedback.parent(),correctFeedback,"| ",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($incorrectFeedback.parent(),incorrectFeedback,"| ",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			//***********	End - Each Option
			
			optionsNode.appendChild(optNode.cloneNode(true));
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