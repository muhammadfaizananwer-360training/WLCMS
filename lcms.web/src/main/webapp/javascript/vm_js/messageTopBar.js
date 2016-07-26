var TopMessageBar = {

		vType : 1, 	// defualt message type 1: success, 2: failure, 3: notification
		vMsg : "<strong>Success!</strong> It's been saved.", //default message to be displayed
		vClassCSS : "alert alert-success alert-dismissible fade in", //defualt css classes to be used
		bShowCloseBtn : true, // should message bar contains close button
		bFadeOut: true, // should message bar be fadout after sometime
		timeFadeOut:9000, //default time after which message bar gets fadeout - used only if bFadeOut is set to true

		displayMessageTopBar : function(messageBarObject){

			if(messageBarObject != null && messageBarObject != 'undefined'){

		        this.vType = (messageBarObject.vType != null && messageBarObject.vType != 'undefined') ? messageBarObject.vType : this.vType;
				switch(this.vType){
		   			case 1:
		   				this.vMsg = "<strong>Success!</strong> It's been saved.";
			   			this.vClassCSS = "alert alert-success alert-dismissible fade in";
		   				break;
		   			case 2:
		   				this.vClassCSS = "alert alert-danger alert-dismissible fade in";
		   				break;
		   			case 3:
		   				this.vClassCSS = "alert-notification  fade in";
		   				break;
		   			default:
		   				this.vMsg = "<strong>Success!</strong> It's been saved.";
		   				this.vClassCSS = "alert alert-success alert-dismissible fade in";
				}

				this.vMsg = (messageBarObject.vMsg != null && messageBarObject.vMsg != 'undefined') ? messageBarObject.vMsg : this.vMsg;
				this.vClassCSS = (messageBarObject.vClassCSS != null && messageBarObject.vClassCSS != 'undefined') ? messageBarObject.vClassCSS : this.vClassCSS;
				this.bShowCloseBtn = (messageBarObject.bShowCloseBtn != null && messageBarObject.bShowCloseBtn != 'undefined') ? messageBarObject.bShowCloseBtn : this.bShowCloseBtn;
				this.bFadeOut = (messageBarObject.bFadeOut != null && messageBarObject.bFadeOut != 'undefined') ? messageBarObject.bFadeOut : this.bFadeOut;
				this.timeFadeOut = (messageBarObject.timeFadeOut != null && messageBarObject.timeFadeOut != 'undefined') ? messageBarObject.timeFadeOut : this.timeFadeOut;

		   		var closeBtnHtml = "";
		   		if(this.bShowCloseBtn == true){
		   		  //closeBtnHtml = "Close";
		   		  closeBtnHtml = "<button data-dismiss='alert' class='close' type='button'><span aria-hidden='true'>&#215;</span><span class='sr-only'>Close</span> </button>";
		   		}
			}
	   		$('#TopMsgBarDiv').html("");
				$('#TopMsgBarDiv').append("<div  class='messages'>"+
					"<div role='alert' class='" + this.vClassCSS + "'>"+
					closeBtnHtml +
					this.vMsg +"</div></div>");

			if(this.bFadeOut){
				this.elementFadeOut('#TopMsgBarDiv', this.timeFadeOut);
				this.elementclick('#TopMsgBarDiv');
			}
	   	},

		elementFadeOut: function(id, time){
			setTimeout(function(){
				$(id).html('');
			},time);
		},

		elementclick: function(id){
		$('.close').click(function(){
			$(id).html('');
		});
		}

};;