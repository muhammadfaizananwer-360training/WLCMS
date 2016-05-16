function setXml(){
	
	var xmlDoc = TEMPLATE.DATA.cloneNode(true);
	var errors = [];
	var $form = $("#inputform_"+TEMPLATE.SID);
	
	// =======================================================	Set mainText value
	var mainTextData = CKEDITOR.instances["insText_"+TEMPLATE.SID].getData();
	var $mainTextElement = $("#cke_insText_"+TEMPLATE.SID);
	SET_CDATA(xmlDoc,xmlDoc.getElementsByTagName("promptQuestionText")[0],mainTextData);
	//***********	Begin - Validate For Blank
	if(TEMPLATE.VALIDATE.MATCH($mainTextElement,mainTextData,"",$mainTextElement.parent().parent().parent().parent().parent()))
	{
		errors.push(0);
	}
	//***********	End - Validate For Blank
	
	
	// =======================================================	Set audioAssets value
	var $audioAssets = $form.find(".audioAssets > .asset");
	var audioAssetsData = TEMPLATE.GET_ASSET_DATA($audioAssets);
	var audioAssetsNode = xmlDoc.getElementsByTagName("audioAssets")[0];
	if(typeof audioAssetsData.path == "undefined")
	{
		REMOVE_CDATA(audioAssetsNode);
	}
	else
	{
		SET_CDATA(xmlDoc,xmlDoc.getElementsByTagName("audioAssets")[0],audioAssetsData.path);
	}
	audioAssetsNode.setAttribute('name', audioAssetsData.name);
	audioAssetsNode.setAttribute('ver', audioAssetsData.ver);
	
	
	
	// =======================================================	Set columnTitles values
	var columnTitlesNode = xmlDoc.getElementsByTagName("columnTitles")[0];
	var clonelistNode = columnTitlesNode.firstChild;
	var dontHideMsg_1 = "undefined";
	
	//	Remove All Children Nodes
	while (columnTitlesNode.firstChild) {
		columnTitlesNode.removeChild(columnTitlesNode.firstChild);
	}
	
	var $columnTitles = $form.find("tBody.columnTitles");
	$columnTitles.children().each(function(i){
		
		var $trg = $(this);
		if($trg.attr("class") != "hide")
		{
			var $catElm = $trg.find("input.cat-title");
			var catData = $catElm.val();
			SET_CDATA(xmlDoc,clonelistNode,catData);
			clonelistNode.setAttribute("value",i);
			columnTitlesNode.appendChild(clonelistNode.cloneNode(true));
			
			//***********	Begin - Validate For Blank
			if(TEMPLATE.VALIDATE.MATCH($catElm,catData,"| ",$columnTitles.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_1))
			{
				dontHideMsg_1 = true;
				errors.push(0);
			}
			//***********	End - Validate For Blank
		}
	});
	
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
	TEMPLATE.VALIDATE.MSG.BLANK = "<div class='error'>There must be at least one row in table to proceed.</div>";
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
			var $questionStatement = $this.find(".questionStatement > .content");
			var $answer = $this.find(".answer > select");
			var $correctFeedBack = $this.find(".correctFeedBack > .content");
			var $correctAudio = $this.find(".correctAudio > .content");
			var $incorrectFeedback = $this.find(".incorrectFeedback > .content");
			var $incorrectAudio = $this.find(".incorrectAudio > .content");
			
			//	Get data
			var questionStatement = $questionStatement.html();
			var answer = $answer.val();
			var correctFeedBack = $correctFeedBack.html();
			var correctAudio = TEMPLATE.GET_ASSET_DATA($correctAudio);
			var incorrectFeedback = $incorrectFeedback.html();
			var incorrectAudio = TEMPLATE.GET_ASSET_DATA($incorrectAudio);
			
			var optNode = cloneOptNode;//optionsNode.childNodes[i-1];
			SET_CDATA(xmlDoc,optNode.childNodes[0],questionStatement);
			optNode.childNodes[1].setAttribute("columnTitleListId",answer);
			SET_CDATA(xmlDoc,optNode.childNodes[2],correctFeedBack);
			
			SET_CDATA(xmlDoc,optNode.childNodes[3],correctAudio.path);
			optNode.childNodes[3].setAttribute("name",correctAudio.name);
			optNode.childNodes[3].setAttribute("ver",correctAudio.ver);
			
			SET_CDATA(xmlDoc,optNode.childNodes[4],incorrectFeedback);
			
			SET_CDATA(xmlDoc,optNode.childNodes[5],incorrectAudio.path);
			optNode.childNodes[5].setAttribute("name",incorrectAudio.name);
			optNode.childNodes[5].setAttribute("ver",incorrectAudio.ver);
			
			//***********	Begin - Each Option
			if(TEMPLATE.VALIDATE.MATCH($questionStatement.parent(),questionStatement,"| ",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($answer.parent(),answer,"0",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
			{
				dontHideMsg_2 = true;
				errors.push(0);
			}
			
			if(TEMPLATE.VALIDATE.MATCH($correctFeedBack.parent(),correctFeedBack,"| ",$options.parent().parent().parent().parent().parent().parent().parent().parent(),dontHideMsg_2))
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
		TEMPLATE.GREEN_ICON($("#slide_"+TEMPLATE.SID+"_p3"),true);
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