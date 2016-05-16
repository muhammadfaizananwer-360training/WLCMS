function XMLtoString(elem){
	var serialized;
	try {
		// XMLSerializer exists in current Mozilla browsers
		serializer = new XMLSerializer();
		serialized = serializer.serializeToString(elem);
	} 
	catch (e) {
		// Internet Explorer has a different approach to serializing XML
		serialized = elem.xml;
	}
	return serialized;
}
function setXml(xmlDoc,tableID){
	
	var newCDATA;
	//	errPanelArr Initialize every time when save click
	errPanelArr = [];
	
	// =======================================================	Set mainText value
	var mainTextElement = xmlDoc.getElementsByTagName("mainText")[0];
	mainTextElement.text = "";
	newCDATA = xmlDoc.createCDATASection($("#mainText").val());
	mainTextElement.appendChild(newCDATA);
	blankValidation($("#mainText").parent(),$("#mainText").val(),$("#mainText").parent().parent().parent());


	// =======================================================	Set promptAudio value
	xmlDoc.getElementsByTagName("promptAudio")[0].text = $("#promptAudio").val();
	xmlDoc.getElementsByTagName("promptAudio")[0].setAttribute('name', $("#promptAudioSpan").val());
	xmlDoc.getElementsByTagName("promptAudio")[0].setAttribute('ver', $("#promptAudioVer").val());
	//blankValidation($("#promptAudioSpan"),$("#promptAudioSpan").val(),$("#promptAudioSpan").parent().parent().parent());
	

	// =======================================================	Set Image value
	xmlDoc.getElementsByTagName("leftImage")[0].text = $("#leftImage").val();
	xmlDoc.getElementsByTagName("leftImage")[0].setAttribute('name', $("#leftImageSpan").val()); 
	xmlDoc.getElementsByTagName("leftImage")[0].setAttribute('ver', $("#leftImageVer").val()); 
	blankValidation($("#leftImageSpan"),$("#leftImageSpan").val(),$("#leftImageSpan").parent().parent().parent());
	
	
	// =======================================================	Set questionText value
	var questionTextElement = xmlDoc.getElementsByTagName("questionText")[0];
	questionTextElement.text = "";
	newCDATA = xmlDoc.createCDATASection($("#questionText").val());
	questionTextElement.appendChild(newCDATA);
	blankValidation($("#questionText").parent(),$("#questionText").val(),$("#questionText").parent().parent().parent());
	// ==========================================================================================
	
	//	Copy option nodes from xmlDoc ****************************
	var optionNode = xmlDoc.getElementsByTagName("option")[0];


	//	Inset New option nodes in xmlDoc from tables ****************************
	var table = document.getElementById(tableID);
	var getRow = $("#"+tableID+" tr");
	var thisPanel = $(table).parent().parent();
	
	
	//	Validation - At Least one row show in the table
	if(rowValidation(getRow.length, 2, thisPanel)){
		
		//	Remove old option nodes from xmlDoc ****************************
		var oldOptionNode = xmlDoc.getElementsByTagName("option");
		var xLength = oldOptionNode.length;
	
		for(i=0;i<xLength;i++){
			if(BrowserDetect.browser == "Explorer"){
				xmlDoc.documentElement.childNodes[4].removeChild(oldOptionNode[i]);
			}else{
				xmlDoc.documentElement.childNodes[4].removeChild(oldOptionNode[0]);
			}
		}
		
	}

	getRow.each(function(i, element) {
		if(i > 1){
			
			var optNode = xmlDoc.getElementsByTagName("options")[0];
			optNode.appendChild(optionNode.cloneNode(true));

			
			//	Set Option ID ****************************
			var opt = optNode.childNodes[i-2];
			opt.setAttribute('id',i-1);
			
			var J = 1;
			$(this).children(this).each(function(j){
				if(j > 0){
					
					
					// =======================================================	Set option Element values by gridTable
					var optElements = opt.childNodes[J-1];
					
					switch(optElements.tagName){
						case "optionText":
							optElements.text = "";
							newCDATA = xmlDoc.createCDATASection($(this).children(this).html());
							optElements.appendChild(newCDATA);
							blankValidation($(this),$(this).children(this).html(),thisPanel);
							break;
						case "feedBack":
							optElements.text = "";
							newCDATA = xmlDoc.createCDATASection($(this).children(this).html());
							optElements.appendChild(newCDATA);
							blankValidation($(this),$(this).children(this).html(),thisPanel);
							break;
						case "optionAudio":
							
							switch($(this).children(this).attr('class')){
								case "optionAudio":
									optElements.text = $(this).children(this).val();
									break;
								case "optionAudioSpan":
									optElements.setAttribute("name",$(this).children(this).val());
									//blankValidation($(this),$(this).children(this).val(),thisPanel);
									break;
								case "optionAudioVer":
									optElements.setAttribute("ver",$(this).children(this).val())
									break;
							}
							
							break;
						case "optionStatus":
							var selectedVal = $(this).children(this).val();
							optElements.setAttribute('value',selectedVal);
							break;
					}
					
					if(optElements.tagName != "optionAudio" || $(this).find('.optionAudioVer').html() == ""){
						J++;
					}
					// ==========================================================================================

					//alert(eachOpt.childNodes[i-1].childNodes[j-1].text);
					//alert($(this).children(this).val());
				}
			});
		}
    });

	if(errPanelArr.length <= 0){
		document.getElementById("txtXml").value = XMLtoString(xmlDoc);
		_IsForModified = false;
	}else{
		document.getElementById("txtXml").value = "invalid";
	}
}