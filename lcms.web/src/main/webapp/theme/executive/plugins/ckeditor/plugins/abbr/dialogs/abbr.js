CKEDITOR.dialog.add( 'abbrDialog', function ( editor ) {
    return {
        title: 'Course Material',
        minWidth: 400,
        minHeight: 200,

        contents: [
            {
                id: 'tab-basic',
                label: '',
                elements: [
					{
						type : 'html',
						html : '<div id="resultsSMLstSlide"></div>' 
					},
                ]
            }
        ],
		
		onOk : function()
		{
			$("#resultsSMLstSlide input:checked").each(function() {
				link = editor.document.createElement( 'a' );
				link.setHtml( "&nbsp;&nbsp;<a href="+$(this).val().replace(/ /g, "%20")+">"+$(this).attr("name")+"</a>" );
				editor.insertElement( link );
			});     
		},
		
		onShow : function()
		{
			var document = this.getElement().getDocument();
			var element = document.getById('resultsSMLstSlide');
			
			if (element) {
				element.setHtml(getValidationResult());
			}
			
		},
		onCancel : function()
		{
			 //var elem = document.getElementById("resultsSMLstSlide");
			 //elem.parentElement.removeChild(elem);
		}
    };
	
	
});


function getValidationResult(){
	var obj;
	
	jQuery.ajax({
		  url: '/lcms/getSpprtMtrByCourse',
		  dataType: "text",
		  type: "POST",
		  cache: false,
		  data:'varCourseId='+getUrlParameter ('id'),
		  async: false,  
		  success: function(response) {
				 obj = $.parseJSON(response);
		}
	 });
	 
	var htmlFormat = '';
	var initTable = "<table style='border:1px solid #ddd;' class='table table-striped table-bordered table-hover dataTable'>"+
					"<thead>"+
					"	<tr>"+
					"		<th></th>"+
					"		<th><strong>Asset Name</strong></th>"+
					"		<th ><strong>File Name</strong></th>"+
					"	</tr>"+
					"</thead>";
	var endTable = "</table>";
	
	for ( var i = 0; i <  obj.length; i++) 
		htmlFormat = htmlFormat + 
					"<tr> "+
					" 	<td width='20'> " +
					" 		<input name='"+obj[i].assetName+"' type='checkbox' id='' value='"+obj[i].location+"' />" +
					" 	</td>" +
					" 	<td width='189'>"+obj[i].assetName+"</td>" +
					" 	<td width='222'>"+obj[i].fileName+"</td> " +
					" </tr>";				

	return initTable + htmlFormat + endTable;
}

