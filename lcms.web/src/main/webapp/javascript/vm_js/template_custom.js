function getSlideTemplateId(){
if (typeof IsGetSlideTemplateId == 'undefined')
{

	Template_Visual_Left_ID = 0;
	Template_Visual_Right_ID = 0;
	Template_Visual_Top_ID = 0;
	Template_Visual_Bottom_ID = 0;
	Template_Visual_Streaming_Center_ID = 0;
	Template_MC_Template_ID = 0;
	Template_DND_Matching_Template_ID = 0;
	Template_DND_Image_Template_ID = 0;
	Template_DND_Category_Template_ID = 0;
	Template_CharColumn_Template_ID = 0;
	console.log('Calling getSlideTemplateID********');
	APP.AJAX({
		url : 'getSlideTemplateID',
	data: { },

		type : 'GET',
		async: false,
		success : function(data) {

			slideTemplates = eval(data);
			for ( var i = 0, len = slideTemplates.length; i < len; ++i) {
				var varSlideTemplate = slideTemplates[i];
				if(varSlideTemplate.slideTemplateName.indexOf('Visual Left')>=0){
					Template_Visual_Left_ID = varSlideTemplate.slideTemplateID;
				}
				else if(varSlideTemplate.slideTemplateName.indexOf('Visual Right')>=0){
					Template_Visual_Right_ID = varSlideTemplate.slideTemplateID;
				}
				else if(varSlideTemplate.slideTemplateName.indexOf('Visual Top')>=0){
					Template_Visual_Top_ID = varSlideTemplate.slideTemplateID;
				}
				else if(varSlideTemplate.slideTemplateName.indexOf('Visual Bottom')>=0){
					Template_Visual_Bottom_ID = varSlideTemplate.slideTemplateID;
				}
				else if(varSlideTemplate.slideTemplateName.indexOf('Video Streaming Center')>=0){
					Template_Visual_Streaming_Center_ID = varSlideTemplate.slideTemplateID;
				}
				else if(varSlideTemplate.slideTemplateName.indexOf('MC Scenario')>=0){
					Template_MC_Template_ID = varSlideTemplate.slideTemplateID;
				}else if(varSlideTemplate.slideTemplateName.indexOf('Drag and Drop Matching')>=0){
					Template_DND_Matching_Template_ID = varSlideTemplate.slideTemplateID;
				}else if(varSlideTemplate.slideTemplateName.indexOf('Drag-and-Drop Matching')>=0){
					Template_DND_Matching_Template_ID = varSlideTemplate.slideTemplateID;
				}else if(varSlideTemplate.slideTemplateName.indexOf('Drag-and-Drop Image')>=0){
					Template_DND_Image_Template_ID = varSlideTemplate.slideTemplateID;
				}else if(varSlideTemplate.slideTemplateName.indexOf('Drag-and-Drop Category')>=0){
					Template_DND_Category_Template_ID = varSlideTemplate.slideTemplateID;
				}else if(varSlideTemplate.slideTemplateName.indexOf('Activity - Chart')>=0){
					Template_CharColumn_Template_ID = varSlideTemplate.slideTemplateID;
				}



			}
		},
		error : function (data) {
			console.log ("error" + eval (data));
		}
	});

	if(Template_MC_Template_ID==0 && Template_DND_Matching_Template_ID==0 && Template_DND_Image_Template_ID==0 && Template_DND_Category_Template_ID==0) {
		$('#ActivityTemplate').hide();
	} else{
		if(Template_MC_Template_ID==0)
			{
				$('#Template_MC_Template_A').hide();
			}

		if(Template_DND_Matching_Template_ID==0)
			{
				$('#Template_DND_Matching_Template_A').hide();
			}

		if(Template_DND_Image_Template_ID==0)
			{
				$('#Template_DND_Image_Template_A').hide();
			}

		if(Template_DND_Category_Template_ID==0)
			{
				$('#Template_DND_Category_Template_A').hide();
			}
	}

	IsGetSlideTemplateId = true;
	}
}
