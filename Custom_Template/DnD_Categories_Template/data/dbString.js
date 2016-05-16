/* ***************************************************************************
	Instructions:

		xmlSampleData variable should be initialize from the string xml.
****************************************************************************** */

var xmlSampleData = '<?xml version="1.0" encoding="utf-8"?>'+
'<?xml-stylesheet type="text/xsl" href="xml/IB.xsl"?>'+

'<root>'+

	'<instructionText type="Text" label="Prompt Question and Instruction Text"></instructionText>'+
	
	'<promptAudio type="Audio" label="Audio Assets" name="" ver=""></promptAudio>'+
	
	'<column type="ColumnBank" label="Drop Spot Categories" >'+
		'<list value="1"></list>'+
		'<list value="2"></list>'+
		'<list value="3"></list>'+
	'</column>'+
	
	'<vennDiagram type="Text" label="Venn Background">0</vennDiagram>'+
	
	'<options type="MultipleOptions" label="Draggable Objects">'+
	
		'<option type="SingleOption" id="1" name="SubOptions">'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<correctCategory label="Correct Category" type="answer" columnListId="0" name="correctCategory"></correctCategory>'+
			
			'<correctFeedback label="Correct Response Feedback" type="Text"></correctFeedback>'+
			
			'<correctFeedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></correctFeedbackAudio>'+
			
			'<incorrectFeedback label="Incorrect Response Feedback" type="Text"></incorrectFeedback>'+
			
			'<incorrectFeedbackAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></incorrectFeedbackAudio>'+
		
		'</option>'+
	
	'</options>'+
	

'</root>';

var xmlSampleData_ForCustomTemplate;