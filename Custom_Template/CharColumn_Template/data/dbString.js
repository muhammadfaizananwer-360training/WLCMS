/* ***************************************************************************
	Instructions:

		xmlSampleData variable should be initialize from the string xml.
****************************************************************************** */

var xmlSampleData = '<?xml version="1.0" encoding="utf-8"?>'+
'<?xml-stylesheet type="text/xsl" href="xml/IB.xsl"?>'+

'<root>'+

	'<promptQuestionText type="Text" label="Prompt Question and Instruction Text"></promptQuestionText>'+
	
	'<audioAssets type="Audio" label="Audio Assets" name="" ver=""></audioAssets>'+
	
	'<columnTitles type="ColumnBank" label="Column Titles">'+
		'<list value="1">True</list>'+
		'<list value="2">False</list>'+
		'<list value="3">Partial</list>'+
	'</columnTitles>'+
	
	'<options type="MultipleOptions" label="Rows">'+
	
		'<option type="SingleOption" id="1" name="radioOptions">'+
		
			'<questionStatement label="Question or Statement" type="Text"></questionStatement>'+
			
			'<answer label="Correct Column" type="answer" columnTitleListId="1" name="CorrectColumn"></answer>'+
			
			'<correctFeedBack label="Correct Response Feedback" type="Text"></correctFeedBack>'+
			
			'<correctAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></correctAudio>'+
			
			'<incorrectFeedback label="Incorrect Response Feedback" type="Text"></incorrectFeedback>'+
			
			'<incorrectAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></incorrectAudio>'+
		
		'</option>'+
	
	'</options>'+
	
'</root>';

var xmlSampleData_ForCustomTemplate;