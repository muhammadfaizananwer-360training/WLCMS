var getButtonID;
var getPreID;
var checkI;
var Icheck;
var isFirstTime = true;
var isChecking = [];
var EventStart = [];
var Audio = document.getElementById("Audio");
var MDiv = document.getElementById("MainDiv")
var mText = document.getElementById("MainText");
var qText = document.getElementById("QText");
var option = document.getElementById("Option");
var Feedback = document.getElementById("FeedbackText2");
var ImgDiv = document.getElementById("Image");
Audio.Switch = 0;
var isPlay;
var t1;
var OPT = [];
var sceneTitle;

//-----------------------------------------------------------------------------------------------------------

//	iScroll setup will run when touch device is dedected
if(BrowserDetect.browser == 'Explorer'){
	var font_percent = 11;
}else{
	var font_percent = 11;
}
font_percent = font_percent/1000;

//	iScroll setup will run when touch device is dedected
if(BrowserDetect.OS == "iPhone/iPod"){
	var mainTxt_scroller;
	var fb_scroller;
	var opt_scroller;
	
	function loaded(){
		mainTxt_scroller = new iScroll('scrollableMainText');
		fb_scroller = new iScroll('scrollableFeedbackText');
		opt_scroller = new iScroll('scrollableOption');
	}

	//document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
	document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 200); }, false);
}

//-----------------------------------------------------------------------------------------------------------

mText.style.overflowY = 'auto';
//qText.style.overflowY = 'auto';
//option.style.overflowY = 'auto';
Feedback.style.overflowY = 'auto';

/*MDiv.style.position= "absolute"; 
MDiv.style.width = "99%"; 
MDiv.style.height = "99%"; 
MDiv.style.border = "#999 1px solid"; 
MDiv.style.overflow = "hidden";*/

//-----------------------------------------------------------------------------------------------------------
//---- starting function ----
//-----------------------------------------------------------------------------------------------------------

function init(){
	//--- Dyamically inserting Audio, Image and Text ----

	var getHeading = jQuery(document).find(".sceneTitle div");

	//alert((getHeading.find("p").html() != "") && (getHeading.html() != ""));
	
	if((getHeading.find("p").html() != "") && (getHeading.html() != "")){
		jQuery(document).find("#MainDiv").css("top", getHeading.height()+10);
	}else{
		jQuery(document).find("#MainDiv").css("top", 0);
	}


	PlayAudio(loadData.promptAudio);
	
	ImgDiv.innerHTML = "<img src='"+loadData.leftImage+"' id='MImage' />";
	
	mText.innerHTML = loadData.mainText;
	
	qText.innerHTML = loadData.questionText;
	if(BrowserDetect.browser == 'Firefox'){
		jQuery('#QText').css({'height':'15%'});
	
	}
	/*if(BrowserDetect.browser == 'Explorer'){
		//jQuery('#QText').css({'padding-left':'3%','position':'static'});
		//jQuery('#scrollableOption').css({'padding-left':'3%','position':'static'});
		jQuery('#QText').css({'width':'92%'});
		jQuery('#scrollableOption').css({'width':'92%'});
		jQuery('#scrollableFeedbackText').css({'width':'96%'});
	}*/
	
	//alert(loadData.optionText.length);
	for( i = 0 ; i<(loadData.optionText.length) ; i++){		
		CreateMC(i);
		isChecking[i] = true;			
	}
}


function PlayAudio(aud)
{
	//alert(aud);
	if(BrowserDetect.OS == "iPhone/iPod" || BrowserDetect.OS == "iPad"){

		/*Audio.innerHTML =	'<embed'+
							' href="'+aud+'"'+
							' type="audio/x-mp3"'+
							' target="myself"'+
							' scale="1"'+
							' controller="false"'+
							' loop="false"'+
							' autoplay="true"'+
							' allowembedtagoverrides="true"'+
							' height="1"'+
							' width="1"'+
							' />"';*/

		Audio.innerHTML = '<audio id="aud" src="'+aud+'" autoplay/>';
		
		if(aud != ''){
			document.getElementById("iaudioBtnHolder").innerHTML = '<button id="iAudioBtn" style="width:56px; height:26px; left:82%; position:fixed; border:none; bottom:56px; background-image:url(../Custom_Template/MC_Simple_Template/UI/PNG/iaudio_play_btn.png)" onclick="iaudioPlayonclick()"></button>';
		}else{
			document.getElementById("iaudioBtnHolder").innerHTML = '';	
		}
	
		
	}else{
	
		if(BrowserDetect.browser == 'Explorer'){
			
			Audio.innerHTML = "<object classid='clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95' height='1' width='1'><param name='src' value='" + aud + "' /><param name='autoStart' value='true' /><param name='controller' value='true' /></object>";
					
		}else if(BrowserDetect.browser == 'Firefox' && BrowserDetect.version < 23){
			
			//alert(BrowserDetect.browser +' - '+BrowserDetect.version);
			
			Audio.innerHTML = "<embed src='"+aud+"' autostart='true'/>";
			
		}else{
		
			Audio.innerHTML = '<audio src="'+aud+'" autoplay="true"/>';
			
		}
		
	}
}


//	Special Case only for iPhone/iPad/iPod
function iaudioPlayonclick(){
	var sound = document.getElementById("aud");
	
	if(!sound.paused){
		document.getElementById("iAudioBtn").style.backgroundImage = 'url(../Custom_Template/MC_Simple_Template/UI/PNG/iaudio_play_btn.png)';
		sound.pause();
		sound.currentTime = 0;

	}else{
		document.getElementById("iAudioBtn").style.backgroundImage = 'url(../Custom_Template/MC_Simple_Template/UI/PNG/iaudio_pause_btn.png)';
		sound.play();
	}
}

function CreateMC(i) {

	OPT[i] = document.createElement("div");
	option.appendChild(OPT[i]);

	this['Opt'+i] = OPT[i];	
	this['Opt'+i].style.position = "relative";
	this['Opt'+i].style.minHeight = "20%";
	this['Opt'+i].style.width = "100%";
//	this['Opt'+i].style.overflow = "hidden";
	//this['Opt'+i].style.paddingTop = "2%";
	//this['Opt'+i].style.top = '1%';

	this['Opt'+i].innerHTML = ["<div style='position:relative; width:7.5%;'><img src='../Custom_Template/MC_Template/UI/PNG/normal.png' class='ImgBut'></div></img><input type='hidden' border='transparent'/><div id="+['OP'+i]+" class='OptDiv'>"+loadData.optionText[i]+"</div><font size='-6'><br></font>"];

	this['OP'+i] = document.getElementById(['OP'+i]);
	this['OP'+i].style.overflowY = 'auto';
	
	var Img;
	if(loadData.optionStatus[i]=="c"){
		Img = 'correct.png';
	}else if(loadData.optionStatus[i]=="pc"){
		Img = 'Pcorrect.png';
	}else if(loadData.optionStatus[i]=="ic"){
		Img = 'incorrect.png';
	}
	
	var Imag = $('#MainDiv img').get(i+1);
	var feedBack = $('#MainDiv input').get(i);
	feedBack.value = loadData.feedBack[i];
	Imag.id = i;
	
	if(BrowserDetect.browser == "Explorer"){
		
		Imag.onmousedown = function(){
					if(isChecking[i]){
						FeedBText(i,loadData.optionStatus[this.id],this,Img);
						PlayAudio(loadData.optionAudio[i]);
					}
		}
		Imag.onmouseover = function(){
			 ChangeImage('over.png',Imag,i);
		}
		Imag.onmouseout = function(){
			ChangeImage('normal.png',Imag,i);
		}
	
	}else{
		
		if(BrowserDetect.OS == "iPhone/iPod"){
			EventStart = ['empty','touchstart','touchend','touchout'];
		}else{
			EventStart = ['empty','mouseover','mousedown','mouseout'];
		}
		
		Imag.addEventListener(EventStart[2] ,function(event){
											 					if(isChecking[this.id]){
																	FeedBText(this.id,loadData.optionStatus[this.id],this,Img);
																	PlayAudio(loadData.optionAudio[this.id]);																	
																}
															 },false);
		Imag.addEventListener(EventStart[1],function(event){
												   ChangeImage('over.png',this,this.id);
													 },false);
		
		Imag.addEventListener(EventStart[3],function(event){
													ChangeImage('normal.png',this,this.id);
													},false);
													
	}
}

function ChangeImage(ChangeImage,ButtonID,i){
	if(isChecking[i]){
		ButtonID.src = "../Custom_Template/MC_Template/UI/PNG/"+ChangeImage;
	}
}

function Reset(ButtonID,i){
	// make previous images active
	ButtonID.src = "../Custom_Template/MC_Template/UI/PNG/normal.png";	
	isChecking[i] = true;
}

function FeedBText(ObjN,CheckIt,Obj,Img){
	if(getButtonID != Obj){
		getPreID = getButtonID;
	var scrol=	jQuery("#scrollableFeedbackText").scrollTop(0);
		
		getButtonID = Obj;
		checkI = Icheck;
		Icheck = ObjN;			
	}

	if(getPreID != undefined){
		Reset(getPreID,checkI);
	}
	for(i=0;i<loadData.optionText.length;i++){
		isChecking[i] = false;
	}
	switch(CheckIt){
		case 'c':
			Feedback.style.color = "#608E3E";			
		break;
		case 'pc':
			Feedback.style.color = "#CA992C";
		break;
		default:
			Feedback.style.color = "#C82128";
		break;
	}
	Feedback.innerHTML = loadData.feedBack[ObjN];
	
	Obj.src="../Custom_Template/MC_Template/UI/PNG/"+Img;

	//	touch device detection
	if(BrowserDetect.OS == "iPhone/iPod"){
		fb_scroller.refresh();
	
		for(i=0;i<loadData.optionText.length;i++){
			isChecking[i] = true;
		}
		isChecking[ObjN] = false;
	}else{
		
		if(isPlay){
			t1.stop();
		}
		
		t1 = new Tween(Feedback.style,'top',Tween.strongEaseOut,60,2,1,'px');
		isPlay = true;
		t1.start();	

		for(i=0;i<loadData.optionText.length;i++){
			isChecking[i] = true;
		}
		isChecking[ObjN] = false;
			
		t1.onMotionStopped = function(){
			isPlay = false;
		}
	}
}


//-----------------------------------------------------------------------------------------------------------


var loadData = {
	init:function(Data){

		this.promptAudio = Data.find('promptAudio').text();
		
		this.leftImage = Data.find('leftImage').text();
		
		this.mainText = Data.find('mainText').text();
		
		this.questionText = Data.find('questionText').text();

		// do you think the following statements are true or false? Click a response to see the result.</strong>";
		var optText = [];
		var fbText = [];
		var optAudio = [];
		var optStatus = [];
		
		Data.find('option').each(function(i){
			optText[i] = jQuery(this).find('optionText').text();
			fbText[i] = jQuery(this).find('feedBack').text();
			optAudio[i] = jQuery(this).find('optionAudio').text();
			optStatus[i] = jQuery(this).find('optionStatus').attr('value');
		});
		
		this.optionText = optText;
		this.feedBack = fbText;
		this.optionStatus = optStatus;
		this.optionAudio = optAudio;

		jQuery(document).ready(function(jQuery) {
			document.getElementById("loader").style.display = "none";
			init();
			addWinResizeListener(runit);
			runit();
			document.onselectstart = function() {return false;} // ie
			
			//document.onmousedown = function() {return false;} // mozilla
			//alert(document.getElementById("QText").scrollHeight);
		});
	}
}
jQuery(document).ready(function(){

sceneTitle = document.getElementById("title");
//alert(jQuery(sceneTitle).html());
    if( jQuery(sceneTitle).html() != ''){
		
	if(BrowserDetect.browser == 'Explorer'){
	
		jQuery('#QText').css({'width':'92%'});
		jQuery('#scrollableOption').css({'width':'92%'});
		jQuery('#scrollableFeedbackText').css({'width':'96%'});
	}
	}	
	
});
//-----------------------------------------------------------------------------------------------------------
var xmlDoc = jQuery.parseXML(xmlSampleData_ForCustomTemplate);
$xml = jQuery(xmlDoc);
loadData.init($xml);