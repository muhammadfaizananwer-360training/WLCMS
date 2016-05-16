/* ***************************************************************************
	Instructions:

		xmlSampleData variable should be initialize from the string xml.
****************************************************************************** */

var xmlSampleData = '<?xml version="1.0" encoding="utf-8"?>'+
'<?xml-stylesheet type="text/xsl" href="xml/IB.xsl"?>'+
	
'<root>'+

	'<instructionText type="Text" label="Prompt Question and Instruction Text"></instructionText>'+
	
	'<promptAudio type="Audio" label="Audio Assets" name="" ver=""></promptAudio>'+
	
	'<categories type="MultipleOptions" label="Drop Spot Categories">'+
		
		'<category type="SingleOption" id="1" name="SubOptions">'+
			
			'<categoryTitle label="Category Title" type="Text"></categoryTitle>'+
			
			'<categoryImage label="Category Image" type="image" name="" ver=""></categoryImage>'+
		
		'</category>'+
		
		'<category type="SingleOption" id="2" name="SubOptions">'+
			
			'<categoryTitle label="Category Title" type="Text"></categoryTitle>'+
			
			'<categoryImage label="Category Image" type="image" name="" ver=""></categoryImage>'+
		
		'</category>'+
		
	'</categories>'+
	
	'<options type="MultipleOptions" label="Draggable Objects">'+
	
		'<option type="SingleOption" id="1" name="SubOptions">'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<correctCategory label="Correct Category" type="answer" categoryId="0" name="correctCategory"></correctCategory>'+
			
			'<correctFeedback label="Correct Response Feedback" type="Text"></correctFeedback>'+
			
			'<correctFeedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></correctFeedbackAudio>'+
			
			'<incorrectFeedback label="Incorrect Response Feedback" type="Text"></incorrectFeedback>'+
			
			'<incorrectFeedbackAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></incorrectFeedbackAudio>'+
		
		'</option>'+
	
	'</options>'+
	

'</root>';

var xmlSampleData_ForCustomTemplate;