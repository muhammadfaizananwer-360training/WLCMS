/* ***************************************************************************
	Instructions:

		xmlSampleData variable should be initialize from the string xml.
****************************************************************************** */

var xmlSampleData = '<?xml version="1.0" encoding="utf-8"?>'+
'<?xml-stylesheet type="text/xsl" href="xml/IB.xsl"?>'+
	
'<root>'+

	'<instructionText type="Text" label="Prompt Question and Instruction Text"></instructionText>'+
	
	'<promptAudio type="Audio" label="Audio Assets" name="" ver=""></promptAudio>'+
	
	'<options type="MultipleOptions" label="Drop Spots and Draggable Objects">'+
	
		'<option type="SingleOption" id="1" name="SubOptions">'+
		
			'<dropText label="Drop Spot Text" type="Text"></dropText>'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<feedback label="Correct Response Feedback" type="Text"></feedback>'+
			
			'<feedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></feedbackAudio>'+
			
			'<hint label="Incorrect Response Feedback" type="Text"></hint>'+
			
			'<hintAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></hintAudio>'+
		
		'</option>'+
		
		'<option type="SingleOption" id="2" name="SubOptions">'+
		
			'<dropText label="Drop Spot Text" type="Text"></dropText>'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<feedback label="Correct Response Feedback" type="Text"></feedback>'+
			
			'<feedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></feedbackAudio>'+
			
			'<hint label="Incorrect Response Feedback" type="Text"></hint>'+
			
			'<hintAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></hintAudio>'+
		
		'</option>'+
		
		'<option type="SingleOption" id="3" name="SubOptions">'+
		
			'<dropText label="Drop Spot Text" type="Text"></dropText>'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<feedback label="Correct Response Feedback" type="Text"></feedback>'+
			
			'<feedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></feedbackAudio>'+
			
			'<hint label="Incorrect Response Feedback" type="Text"></hint>'+
			
			'<hintAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></hintAudio>'+
		
		'</option>'+
		
		'<option type="SingleOption" id="4" name="SubOptions">'+
		
			'<dropText label="Drop Spot Text" type="Text"></dropText>'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<feedback label="Correct Response Feedback" type="Text"></feedback>'+
			
			'<feedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></feedbackAudio>'+
			
			'<hint label="Incorrect Response Feedback" type="Text"></hint>'+
			
			'<hintAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></hintAudio>'+
		
		'</option>'+
		
		'<option type="SingleOption" id="5" name="SubOptions">'+
		
			'<dropText label="Drop Spot Text" type="Text"></dropText>'+
			
			'<draggableObject label="Draggable Object Label" type="Text"></draggableObject>'+
			
			'<feedback label="Correct Response Feedback" type="Text"></feedback>'+
			
			'<feedbackAudio type="Audio" label="Correct Feedback Audio" name="" ver=""></feedbackAudio>'+
			
			'<hint label="Incorrect Response Feedback" type="Text"></hint>'+
			
			'<hintAudio type="Audio" label="Incorrect Feedback Audio" name="" ver=""></hintAudio>'+
		
		'</option>'+
	
	'</options>'+
	

'</root>';

var xmlSampleData_ForCustomTemplate;