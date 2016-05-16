
var CK_CONFIG = {

	TITLE:{
		toolbar : [
			[ 'Bold', 'Italic', 'Subscript', 'Superscript', '-', 'RemoveFormat']
		]
	},
	CC:{
		toolbar : [
			{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
			{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
			{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
			{ name: 'insert', items: [ 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe' ] },
			{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
			{ name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
			{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
			{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
			{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
			{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
			{ name: 'colors', items: [ 'TextColor', 'BGColor' ] }
		]
	},
	TEXT:{
		toolbar : [
			{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
			{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
			{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
			{ name: 'insert', items: [ 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe' ] },
			{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
			{ name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
			{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
			{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
			{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
			{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
			{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
			{ name: 'others', items: [ 'Abbr' ]}
		]
	},
	ANSWER_CHOICE:{
		toolbar : [
			{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
			{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
			{ name: 'links', items: [ 'Link', 'Unlink' ] },
			{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
			{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] }
		]
	}
}

var APP = function () {
	var indexLLO = 0;
    return {

		//	BEGIN EDIT AND VIEW TOGGLE
		EDIT_OR_VIEW_TOGGLE : function(){

		},
		//	END EDIT AND VIEW TOGGLE

		//	BEGIN LEFT NAV
		LEFT_NAV : {
			lastID : null,
			lastSubMenu : null,
			width : '200px',
			init : function(STATE,TRG_ID)
			{
				//	BEGIN MENU STATES
				$(".list-sub-group .list-group-item a").click(function()
				{

					if(APP.LEFT_NAV.lastSubMenu != null){
						APP.LEFT_NAV.lastSubMenu.removeClass('active');
					}
					$(this).addClass('active');

					APP.LEFT_NAV.lastSubMenu = $(this);
				});
				//	END MENU STATES

				//	BEGIN SUB MENU
				$('#leftNavPanel .sub-menu').click(function ()
				{
					var trg = $(this).parent().children('ul.list-sub-group');
					leftNavCollapse(trg,$(this));
				});
				//	END SUB MENU

				//	BEGIN ACCORDIONS
				$('#leftNavPanel .btn-sub-group').click(function ()
				{
					var trgElm = $("#leftNavPanel .list-holder");
					if(trgElm.css('width') != '42px')
					{
						var panelElm = $(this).parent().children('ul.list-sub-group');
						var lastBtnElm;
						var lastPanelElm;

						//	---------------- Active and De-Active State Toggling
						if(APP.LEFT_NAV.lastID != null)
						{
							lastBtnElm = $("#"+APP.LEFT_NAV.lastID);
							lastPanelElm = lastBtnElm.parent().children('ul.list-sub-group');
							lastBtnElm.removeClass('active');
						}

						$(this).addClass('active');

						if(typeof panelElm.html() != 'undefined')
						{
							//	---------------- Accordion Button
							leftNavCollapse(panelElm,$(this));

							if(APP.LEFT_NAV.lastID != null)
							{
								if(typeof lastPanelElm.html() != 'undefined')
								{
									if(lastBtnElm.attr("id") != $(this).attr("id"))
									{
										leftNavCollapse(lastPanelElm,lastBtnElm);
										lastBtnElm.addClass('active');
									}
									else
									{
										lastBtnElm.removeClass('active');
										APP.LEFT_NAV.lastID = null;
										accordion_btn_actions("course_welcome");
										return;
									}
								}
							}
						}
						else
						{
							//	---------------- Simple Button
							if(APP.LEFT_NAV.lastID != null)
							{
								if(lastBtnElm.attr("id") != $(this).attr("id"))
								{
									leftNavCollapse(lastPanelElm,lastBtnElm);
								}
							}
						}
						APP.LEFT_NAV.lastID = $(this).attr("id");
					}

					accordion_btn_actions($(this).attr("id"));
				});
				//	END ACCORDIONS

				//	BEGIN INITIAL STATE
				if(typeof STATE != 'undefined')
				{
					if(typeof TRG_ID != 'undefined')
					{
						var trgBtnElm = $("#"+TRG_ID);
						var trgPanelElm = trgBtnElm.parent().children('ul.list-sub-group');

						trgBtnElm.addClass('active');

						if(typeof trgPanelElm.html() != 'undefined')
						{
							leftNavCollapse(trgPanelElm,trgBtnElm);
						}

						APP.LEFT_NAV.lastID = TRG_ID;
					}

					if(STATE == "OPEN")
					{
						toggle_leftNavPanel();
					}
				}
				//	END INITIAL STATE

				//	BEGIN RESIZE
				{

					var leftNavElm = $("#leftNavPanel");
					analyseScreenWidth(leftNavElm);

					$(window).resize(function()
					{
						analyseScreenWidth(leftNavElm);
					});
				}
				//	END RESIZE
			}
		},
		//	END LEFT NAV

		//	BEGIN COURSE OUTLINE TREE
		LEFT_NAV_COURSE_OUTLINE : function(JSON_FILE){
			$('#tree').aciTree({
				ajax: {
					url: 'assets/plugins/aciTree/json/'+JSON_FILE+'.json'
				}
			});

			// Listen for the events before we init the tree
			$('#tree').on('acitree', function(event, api, item, eventName, options)
			{
				// get the item ID
				var itemId = api.getId(item);
				if (eventName == 'selected') {

				}
			});
		},
		//	END COURSE OUTLINE TREE

		//	BEGIN PORTLET COLLAPSE
		BODY_COLLAPSES : function(STATE){

			$('.portlet-btn > .portlet-title').click(function (e) {
				e.preventDefault();
				var el = jQuery(this).parent().find(".portlet-body");

				if (jQuery(this).find(".tools > a").hasClass("collapse")) {
					jQuery(this).find(".tools > a").removeClass("collapse").addClass("expand");
					el.slideUp(200);
				} else {
					jQuery(this).find(".tools > a").removeClass("expand").addClass("collapse");
					el.slideDown(200);
				}
			});

			if(STATE == "CLOSE")
			{
				jQuery('.portlet-btn > .portlet-title > .tools > a').removeClass("collapse").addClass("expand");
				jQuery('.portlet-btn > .portlet-body').css('display','none');
			}
		},
		//	END PORTLET COLLAPSE

		//	BEGIN ACCORDION PANEL

		ACCORDION_MOVEABLE : function(){









		},
		//	END ACCORDION PANEL

		//	BEGIN ROUNDED CORNERS
		APPLY_ROUNDED_CORNERS : function(){
			if(BrowserDetect.browser == 'Explorer' && BrowserDetect.version == 8)
			{
				$('.str-navigator').corner('10px');
				$('.badge').corner('20px').css('padding', '12px 15px 25px');
				$('.str-navigator ul li').css("border",'none').corner('15px');
			}
		},
		//	END ROUNDED CORNERS

		//	BEGIN CKEDITOR
		CKEDITOR : function(TxtID,toolbar){

			//	Global Configuration
			CKEDITOR.config.height = '100px';
			CKEDITOR.config.extraPlugins = 'abbr';


			$("#"+TxtID).attr("data-config",toolbar);

			//	Specific Configurations
			switch(toolbar){
				case "TITLE":
					CKEDITOR.replace(TxtID, CK_CONFIG.TITLE);
					break;

				case "STANDARD":
					CKEDITOR.replace(TxtID, CK_CONFIG.CC);
					break;

				case "TEXT":
					CKEDITOR.replace(TxtID, CK_CONFIG.TEXT);
					break;
				case "ANSWER_CHOICE":
					CKEDITOR.replace(TxtID, CK_CONFIG.ANSWER_CHOICE);
					break;
			}
		},
		//	END CKEDITOR

		//	BEGIN DATAGRID
		DATAGRID : function(TID,isMoveable,searchable,addOrDelete,sortable){
			TableManaged.init(TID,searchable,sortable,addOrDelete);
			if(isMoveable){
				TableManaged.moveable(TID);
			}

		},
		//	END DATAGRID

		//	BEGIN SEARCHGRID
		SEARCHGRID : {
			init : function(TID,searchable,sortable,configuration,extention){
				return TableManaged.init(TID,searchable,sortable,false,configuration,extention);
			},
			columnFilter : function(TID){
				TableManaged.hide_able_column(TID);
			}
		},
		//	END SEARCHGRID

		//	BEGIN SAVE COURSE
		SAVE_COURSE: function(){

			$(".wrapper > .messages").html(msg('content'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE COURSE

		//	BEGIN SAVE OVERVIEW FORM
		SAVE_OVERVIEW: function(){

			$(".wrapper > .messages").html(msg('overview'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE OVERVIEW FORM

		//	BEGIN SAVE SETTINGS FORM
		SAVE_SETTINGS: function(){






		},
		//	END SAVE SETTINGS FORM

		//	BEGIN SAVE AVAILABILITY FORM
		SAVE_AVAILABILITY: function(){

			$(".wrapper > .messages").html(msg('overview'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE AVAILABILITY FORM

		//	BEGIN SAVE PRICING FORM
		SAVE_PRICING: function(){

			$(".wrapper > .messages").html(msg('done'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE PRICING FORM

		//	BEGIN SAVE MARKETING FORM
		SAVE_MARKETING: function(){

			$(".wrapper > .messages").html(msg('done'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},

		//	BEGIN SAVE PUBLISH FORM
		SAVE_PUBLISH: function(){

			$(".wrapper > .messages").html(msg('done'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE PUBLISH FORM

		//	BEGIN SAVE OFFER FORM
		SAVE_OFFER: function(){

			$(".wrapper > .messages").html(msg('send'));

			window.setTimeout(function() {
				$(".alert").addClass('in');
			}, 100);
		},
		//	END SAVE OFFER FORM

		//	BEGIN DATE PICKER
		DATE_PICKER: function(ID){
			$(ID).datepicker();
			$(ID).on('changeDate', function(ev) {
				if(ev.viewMode == "days")
				{
					$(this).datepicker('hide');

				}
			});
		},
		//	END DATE PICKER

		//	BEGIN ACCORDION EVENT
		ACCORDION_EVENT: function(ID){

			$(ID).on('hide.bs.collapse', function () {


			  //	FIND OUT CLOSE PANEL INSTANCE
			  $trgPanel = $("#"+APP.CACHE_BAR).find(".in").attr('id');


			})

			$(ID).on('shown.bs.collapse', function () {


			  //	FIND OUT OPEN PANEL INSTANCE
			  $trgPanel = $("#"+APP.CACHE_BAR).find(".in").attr('id');


			})

		},
		//	END ACCORDION EVENT
        //    BEGIN MODAL EVENT
        MODAL_EVENT: function(ID){

              $(ID).on('hidden.bs.modal', function (e) {

                switch(ID)
                {
                    case "#addVisualAssetModal":
                                populateVisualAssetModel($(this), false);
                                break;
                    case "#addAssetModal":
                                populateAudioAssetModel($(this), false);
								break;
					case "#selectTemplateModal":
								populateTemplateSelectionModel($(this), false);
								break;
                }

              })

              $(ID).on('show.bs.modal', function (e) {

                switch(ID)
                {
                    case "#addVisualAssetModal":
                                populateVisualAssetModel($(this), true);
                                break;
                    case "#addAssetModal":
                                populateAudioAssetModel($(this), true);
								break;
					 case "#selectTemplateModal":
								populateTemplateSelectionModel($(this), true);
								break;
					 case "#addSlideModal":
								setDefaultValueForAddSlides($(this), true);
								break;

                }

              })

        },
        //    END MODAL EVENT

		//	BEGIN PLACEHOLDER FIX CODE
		PLACEHOLDER_FIX: function(){
			var test = document.createElement('input');

			if (!('placeholder' in test))
			{
				$('input').each(function()
				{
				   if ($(this).attr('placeholder') != "" && this.value=="")
				   {
					   $(this).val($(this).attr('placeholder')).css('color', 'grey')
						  .on({
							  focus: function()
							  {
								  if (this.value==$(this).attr('placeholder'))
								  {
									$(this).val("").css('color', '#000');
								  }
							  },
							  blur: function()
							  {
								  if (this.value=="")
								  {
									$(this).val($(this).attr('placeholder'))
										.css('color', 'grey');
								  }
							  }
							});
				   }
				});
			}
		},
		//	END PLACEHOLDER FIX CODE

		//	BEGIN MESSAGES CLEAR
		MESSAGES_CLEAR: function(){
			$(".wrapper > .messages").html("");
		},
		//	END MESSAGES CLEAR

		//	BEGIN LOADER
		LOADING_BTN: function(ID){

			$(ID).click(function () {
				var btn = $(this);
				btn.button('loading');
			  });
		},
		//	END LOADER

		//	BEGIN LOADER
		LOADING_BTN_RESET: function(ID,RID){

			$(ID).click(function () {
				$(RID).button('reset');
			  });

		},
		//	END LOADER

		//	BEGIN LOADING BAR
		LOADING_BAR: function (){

			$(".ui-ios-overlay").remove();

			var opts = {
			  lines: 13, // The number of lines to draw
			  length: 6, // The length of each line
			  width: 5, // The line thickness
			  radius: 2, // The radius of the inner circle
			  corners: 0, // Corner roundness (0..1)
			  rotate: 0, // The rotation offset
			  direction: 1, // 1: clockwise, -1: counterclockwise
			  color: '#fff', // #rgb or #rrggbb or array of colors
			  speed: 1, // Rounds per second
			  trail: 60, // Afterglow percentage
			  shadow: false, // Whether to render a shadow
			  hwaccel: false, // Whether to use hardware acceleration
			  className: 'spinner', // The CSS class to assign to the spinner
			  zIndex: 2e9, // The z-index (defaults to 2000000000)
			  top: '50%', // Top position relative to parent
			  left: '50%' // Left position relative to parent
			};

			var target = document.getElementById('foo');
			var spinner = new Spinner(opts).spin(target);

			var overlay = iosOverlay({
				text: "Saving...",
				spinner: spinner
			});

			if(Math.round(Math.random()))
			{
				window.setTimeout(function() {
					overlay.update({
						icon: "assets/plugins/iosOverlay-js/img/check.png",
						text: "Success"
					});
				}, 3e3);
			}
			else
			{
				window.setTimeout(function() {
					overlay.update({
						icon: "assets/plugins/iosOverlay-js/img/cross.png",
						text: "Fail"
					});
				}, 3e3);
			}

			window.setTimeout(function() {
				overlay.hide();
			}, 5e3);
		},
		//	END LOADING BAR

		//	BEGIN LLO ADD OR DELETE
		LLO: function (trg,isAdd,isModal){

			$table = $(trg).parent().parent().parent().parent().parent();
			$allChecker = $table.find('thead');
			$allChecker = $allChecker.find('.checker');
			$table = $table.find('tbody');

			LLOname='LLO_'+indexLLO++ ;
			varTextBox = '';
			if (isModal) {
				varTextBox = '<input type="text" maxlength="50" value="" id=\"'+ LLOname + '\" name=\"' + LLOname + '\" placeholder="Enter learning objective name." class="form-control lloEmptyCheck checkForDuplicate lloCommaCheck" onfocusout="onKeyFocusOut(this.form)" />'
			}else {
				varTextBox = '<input type="text" maxlength="50" value="" id=\"'+ LLOname + '\" name=\"' + LLOname + '\" placeholder="Enter learning objective name." class="form-control lloEmptyCheck checkForDuplicate lloCommaCheck" onfocusout="onKeyFocusOut(this.form)" />'
			}


			var strHTML = '<tr>'+
								'<td>'+
									'<input type="checkbox" class="checks" onclick="APP.CHECKBOX(this,false)" />'+
								'</td>'+
								'<td>'+
									varTextBox
								'</td>'+
						  '</tr>';

			if(isAdd)
			{
				//	ADD
				$table.append(strHTML);
				$allChecker.prop("checked",false);

				$parentDiv = $(trg).parent().parent().parent().parent().parent();
				$parentDiv.find('#removeObjective').removeAttr('disabled');
				$parentDiv.find('#learningObjectiveCheckbox').removeAttr('disabled');

				$parentDiv.find('#BT_LLO_DELETE').removeAttr('disabled');
				$parentDiv.find('#LLO_CHECKBOX').removeAttr('disabled');
			}
			else
			{
				$table = $(trg).parent().parent().parent().parent().parent();
				var count = $table.find ('tbody > tr').length;
				var checkbox  = $table.find("input:checked").length;

				APP.CACHE = [$table,$allChecker];
				$trgModal = $("#confirmationModal");
				if (count > 0 && checkbox > 0){
					//	BEGIN TITLE, MESSAGE AND BUTTONS
					var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';
					var msg = '<p>Deleting this learning objective will also permanently remove the associated question bank and all of its questions from the lesson quiz. Do you want to continue?</p>';
					var btns = '<button type="button" class="btn blue" onclick="APP.LLO_CONFIRM_REMOVE(true)" data-dismiss="modal">YES</button>'+
								'<button type="button" class="btn btn-default" onclick="APP.LLO_CONFIRM_REMOVE(false)" data-dismiss="modal">NO</button>';
					//	END TITLE, MESSAGE AND BUTTONS

					$trgModal.find(".modal-title").html(title);
					$trgModal.find(".modal-body").html(msg);
					$trgModal.find(".modal-footer").html(btns);

					$trgModal.modal('show');
				}
			}
		},
		//	END LLO ADD OR DELETE

		//	BEGIN LLO DELETE
		LLO_CONFIRM_REMOVE: function (confirm){

			if(confirm)
			{
				$table = APP.CACHE[0];
				//	DELETE
				$table.find('tr').each(function(index,element){

					if($(element).find('.checks').prop('checked') == true)
					{
						// CALL application delete function
						// before removing element
						var contentObject_id = $(element).closest('form').parent ().parent().parent().attr ('id');
						deleteLLO ($(element).find('.lloEmptyCheck').attr('id'), contentObject_id);
						$(element).remove();
					}

				});

				$(APP.CACHE[1]).prop("checked",false);

				var $table = APP.CACHE[0];
				var $thBody = $table.parent();

				var tableLength = $table.find ('tbody > tr ').length;

				if(tableLength < 1){

					$thBody.find('#removeObjective').attr('disabled','disabled');
					$thBody.find('#learningObjectiveCheckbox').attr('disabled','disabled');

					$thBody.find('#BT_LLO_DELETE').attr('disabled','disabled');
					$thBody.find('#LLO_CHECKBOX').attr('disabled','disabled');
				}
			}
			APP.CACHE = "";
		},
		//	END LLO DELETE

		//	BEGIN CHECKALL
		CHECKBOX: function (trg,isChecker){

			$table = $(trg).parent().parent().parent().parent();

			if(isChecker)
			{
				//	HEADER CHECKBOX CONTROL BODY CHECKBOXES
				$tbody = $table.find('tbody');

				$tbody.children().each(function(index, element){

					//	Update Body CheckBoxs
					$(element).find('.checks').prop('checked',trg.checked);

				});
			}
			else
			{

				//	BODY CHECKBOX CONTROL HEADER CHECKBOXES
				$tbody = $table.find('tbody');

				$allChecker = $table.find('thead');
				$allChecker = $allChecker.find('.checker');

				if(trg.checked)
				{
					$isCheckAll = true;

					$tbody.children().each(function(index, element){

						if($(element).find('.checks').prop('checked') == false)
						{
							$isCheckAll = false;
						}

					});

					$allChecker.prop('checked',$isCheckAll);
				}
				else
				{
					$allChecker.prop('checked',trg.checked);
				}
			}
		},
		//	END CHECKALL
//		BEGIN CHECKALL WITH BTN
		CHECKBOX_WITH_BTN: function (trg,isChecker,condBtn){

			$table = $(trg).parent().parent().parent().parent();

			if(isChecker)
			{
				//	CONDITIONAL BUTTON
				if(trg.checked)
				{
					$("#"+condBtn).removeClass("disabled").removeAttr("disabled","disabled");
				}
				else
				{
					$("#"+condBtn).addClass("disabled").attr("disabled","disabled");
				}

				//	HEADER CHECKBOX CONTROL BODY CHECKBOXES
				$tbody = $table.find('tbody');

				$tbody.children().each(function(index, element){

					//	Update Body CheckBoxs
					$(element).find('.checks').prop('checked',trg.checked);

				});
			}
			else
			{
				//	BODY CHECKBOX CONTROL HEADER CHECKBOXES
				$tbody = $table.find('tbody');

				$allChecker = $table.find('thead');
				$allChecker = $allChecker.find('.checker');

				if(trg.checked)
				{
					var $isCheckAll = true;

					$tbody.children().each(function(index, element){

						if($(element).find('.checks').prop('checked') == false)
						{
							$isCheckAll = false;
						}
					});

					$allChecker.prop('checked',$isCheckAll);

					//	CONDITIONAL BUTTON
					$("#"+condBtn).removeClass("disabled").removeAttr("disabled","disabled");
				}
				else
				{
					$allChecker.prop('checked',trg.checked);

					var $isAnyCheck = false;
					$tbody.children().each(function(index, element){

						if($(element).find('.checks').prop('checked') == true)
						{
							$isAnyCheck = true;
						}

					});

					//	CONDITIONAL BUTTON
					if(!$isAnyCheck)
					{
						$("#"+condBtn).addClass("disabled").attr("disabled","disabled");
					}
				}
			}
		},
		//	END CHECKALL WITH BTN
		//	BEGIN CACHE
		CACHE: "",
		//	END CACHE

		//	BEGIN BAR CACHE
		CACHE_BAR: "",
		//	END BAR CACHE

		//	BEGIN POPOVER
		BOOTSTRAP_POPOVER:function(ID){
			$(ID).popover();
		},
		//	END POPOVER

		//	BEGIN TOOLTIP
		BOOTSTRAP_TOOLTIP:function(ID){
			$(ID).tooltip();
		},
		//	END TOOLTIP

		//	BEGIN BOTTOM BAR
		BOTTOM_BAR: function(){
			$(window).scroll(function()
			{
				if ($(this).scrollTop() > 300)
				{
					$('body').addClass("gritter-save-body");
					$('#btnSaveUp').parent().parent().addClass("gritter-save");
				}
				else
				{
					$('body').removeClass("gritter-save-body");
					$('#btnSaveUp').parent().parent().removeClass("gritter-save");
				}
			});
		},
		//	END BOTTOM BAR

		//	BEGIN AJAX
		AJAX:function(option){
			var providedBeforeSend = option.beforeSend;

			option.beforeSend = function(xhr){
				if(providedBeforeSend != null) {
					providedBeforeSend(xhr);
				}
				xhr.setRequestHeader('isAjax', 'true');
			},
			$.ajax(option).always(function(data, textStatus, jqXHR){
				if (data.status == 302) {
					console.log("logout");
					window.location.href = 'login';
				}
			});
		},
		UPLOAD_FILE:{
			PLUPLOAD:{},
			DELETE :function(condition,instance)
			{
				var trg = APP.UPLOAD_FILE.PLUPLOAD[instance];
				if(condition)
				{
					plupload.each(trg.settings.selected_files, function(file) {
						if(file != "predefined")
						{
							var file = trg.getFile(file);
							trg.removeFile(file);
							$('#' + file.id).remove();
						}
						else
						{
							$('#' + file).remove();
						}
						$('#previewVideoPlayer').addClass("hide");

						if(typeof playerInstance != "undefined")
						{
							playerInstance.stop();
						}
					});
					trg.settings.selected_files = [];
					APP.UPLOAD_FILE.CAN_ADD_MORE_FILES(true,trg);
					APP.UPLOAD_FILE.CHECK_DATA(trg);
				}
			},

			CAN_ADD_MORE_FILES: function (condition,trg)
			{
				trg.disableBrowse(!condition);
				if(condition)
				{
					$(trg.settings.browse_button).removeAttr("disabled","disabled").removeClass("disabled");
					if(trg.features.dragdrop)
					{
						$(trg.settings.drop_element[0]).removeClass("hide");
					}
					if(typeof trg.settings.root_browse_button != "undefined")
					{
						$('#'+trg.settings.root_browse_button).removeAttr("disabled","disabled").removeClass("disabled");
					}
				}
				else
				{

					$(trg.settings.browse_button).attr("disabled","disabled").addClass("disabled");
					$(trg.settings.drop_element[0]).addClass("hide");
					if(typeof trg.settings.root_browse_button != "undefined")
					{
						$('#'+trg.settings.root_browse_button).attr("disabled","disabled").addClass("disabled");
					}
				}
			},

			CHECK_DATA: function (trg)
			{
				if($("#"+trg.settings.list_element).find('.attached').length > 0)
				{
					//	If already data present in the list
					$("#"+trg.settings.remove_button).removeAttr("disabled","disabled").removeClass("disabled");
				}
				else
				{
					//	If empty list
					$("#"+trg.settings.remove_button).attr("disabled","disabled").addClass("disabled");
				}
			}
		}
		//	END AJAX

	}
}();





function msg(cases)
{
	var str = '';

	switch(cases){

		case "content":
			str += '<div class="alert alert-success alert-dismissible fade" role="alert">'+
					'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
					'<strong>Success!</strong> The content has been saved.'+
				  '</div>';
			break;

		case "overview":
			str += '<div class="alert alert-success alert-dismissible fade" role="alert">'+
					'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
					'<strong>Whoo Hoo! A new course!</strong> Complete each section in the left navigation panel to build your course and publish it.'+
				  '</div>';
			break;

		case "done":
			str += '<div class="alert alert-success alert-dismissible fade" role="alert">'+
					'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
					'<strong>Success!</strong> this section has been saved.'+
				  '</div>';
			break;

		case "send":
			str += '<div class="alert alert-success alert-dismissible fade" role="alert">'+
					'<button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'+
					'<strong>Success!</strong> request has been submit.'+
				  '</div>';
			break;
	};

	return str;
}

function remove_panel(trg, deletionType, id)
{
	APP.CACHE = trg;
	$trgModal = $("#confirmationModal");

	//	BEGIN TITLE, MESSAGE AND BUTTONS
	var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';

	if(deletionType==1){
		var msg = '<p>Are you sure you want to remove this lesson and all associated slides, question banks, and questions?</p>';
	}else if(deletionType==3){
		var msg = '<p>Deleting this question bank will permanently remove it and all of its questions from this quiz. It will also remove the lesson learning objective associated with this bank. Do you want to continue?</p>';
	}else if(deletionType==4){
		var msg = '<p>Are you sure you want to remove this question from the quiz?</p>';
	}else if(deletionType==5){
		var msg = '<p>Are you sure you want to turn off the quiz for this lesson?</p>';
	}else if(deletionType==6){
		var msg = '<p>Are you sure you want to remove this question from the exam?</p>';
	}else if(deletionType==7){
		var msg = '<p>Are you sure you want to delete this support material? Re-publishing of the course will be required.</p>';
	}else{
		var msg = '<p>Are you sure you want to remove this slide?</p>';
	}
	var btns = '<button type="button" class="btn blue" onclick="confirm_remove(true,'+ deletionType +' , '+ id +')" data-dismiss="modal">YES</button>'+
				'<button type="button" class="btn btn-default" onclick="confirm_remove(false, -1, -1)" data-dismiss="modal">NO</button>';
	//	END TITLE, MESSAGE AND BUTTONS

	$trgModal.find(".modal-title").html(title);
	$trgModal.find(".modal-body").html(msg);
	$trgModal.find(".modal-footer").html(btns);

	$trgModal.modal('show');
	//$('#btnUpdateSelectedSlideTemplate').attr("data-dismiss", "modal");
}

function confirm_remove(confirm, deletionType, id)
{
	if(confirm)
	{
		//deletionType=1 mean 'Lesson'
		//deletionType=2 mean 'slide'
		if(deletionType==1){
			deleteLesson(id);
		}else if(deletionType==2){
			deleteSlide(id);
		}else if(deletionType==3){
			deleteAssessmentItemBank(id);
		}else if(deletionType==4){
			deleteAssessmentItem(id);
		}else if(deletionType==5){
			deleteQuiz(id);
		}else if(deletionType==6){
			deleteAssessmentItem(id);
		}else if(deletionType==7){
			deleteSupportMaterial(id);
		}

		$bar = $(APP.CACHE).parent().parent().parent();
		$bankbar = $(APP.CACHE).parent().parent().parent().parent();
		$bar.animate({height: "0px"}, 500);
		window.setTimeout(function() {
			if(deletionType==2){
				var contentObjectId = $bar.parent().attr('id').replace('slide_','');
			}
			$bar.remove();
			AfterremoveQuestion($bankbar);
			showHideDualSave();
			if(deletionType==1){
				SetArrowVisibilityContentObject();
			}else if(deletionType==2){
				SetArrowVisibilityScene(contentObjectId);
			}else if(deletionType==7){
				setArrowVsbltySpprtMtrl($("#hidLessonId").val());
			}

		}, 500);

	}
	APP.CACHE = "";
}

// THESE FUNCTION SHOULD NOT BE HERE,
// THESE SHOULD BE ON SLIDECOMPONENT.JS
// REFECTORING REQUIRED.
function updateSelectedSlideTemplate(trg) {

	if($("#isTemplateSlctnFrmForAddOrUpdate").val()=="1"){

		var varslctedTempId = $("#hidTemplateIdForSetupFormSelection").val();
		$("#hidSlideTemplateIdForAddForm").val(varslctedTempId);
		toggleDisplayResolutionArea(false, "");

		switch(varslctedTempId){
		case Template_Visual_Left_ID.toString():
			$("#slideTemplateNameForAddForm").html("Visual Left");
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-0');
			break;
		case Template_Visual_Right_ID.toString():
			$("#slideTemplateNameForAddForm").html("Visual Right");
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-1');
			break;
		case Template_Visual_Top_ID.toString():
			$("#slideTemplateNameForAddForm").html("Visual Top");
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-2');
			break;
		case Template_Visual_Bottom_ID.toString():
			$("#slideTemplateNameForAddForm").html("Visual Bottom");
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-3');
			break;
		case Template_Visual_Streaming_Center_ID.toString():
			$("#slideTemplateNameForAddForm").html(WLCMS_CONSTANTS.TEMPLATE_TYPE_FOR_STREAMING_TXT_L);
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-10');
			toggleDisplayResolutionArea(true, "");
			break;
		case Template_MC_Template_ID.toString():
			$("#slideTemplateNameForAddForm").html(WLCMS_CONSTANTS_TEMPLATE_MC_Scenario.TEMPLATE_TYPE_FOR_STREAMING_TEXT);
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-11');
			break;
		case Template_DND_Matching_Template_ID.toString():
			$("#slideTemplateNameForAddForm").html('Drag and Drop Matching');
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-14');
			break;
		case Template_DND_Image_Template_ID.toString():
			$("#slideTemplateNameForAddForm").html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_IMAGE_TEXT_HEADING);
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-25');
			break;
		case Template_DND_Category_Template_ID.toString():
			$("#slideTemplateNameForAddForm").html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_DND_CATEGORY_TEXT_HEADING);
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-15');
			break;
		case Template_CharColumn_Template_ID.toString():
			$("#slideTemplateNameForAddForm").html(WLCMS_LOCALIZED.CUSTOM_TEMPLATE_CharColumn_TEXT_HEADING);
			$("#slideTemplateIconForAddForm").attr('class', 'temp-icon-13');
			break;
		}


		$("#hidTemplateIdForSetupFormSelection").val('');
		$('#btnUpdateSelectedSlideTemplate').attr("data-dismiss", "modal");

	}else if($("#isTemplateSlctnFrmForAddOrUpdate").val()=="2"){

		APP.CACHE = trg;
		$trgModal = $("#confirmationModal");

		//	BEGIN TITLE, MESSAGE AND BUTTONS
		var title = '<i class="glyphicon glyphicon-warning-sign"></i> Please Confirm';

		var msg = '<p>Changing the page template will remove components not used by the selected template. Do you want to continue?</p>';
		var btns = '<button type="button" class="btn blue" onclick="updateSelectedSlideTemplate_Confirm(true)" data-dismiss="modal">YES</button>'+
					'<button type="button" class="btn btn-default" onclick="updateSelectedSlideTemplate_Confirm(false)" data-dismiss="modal">NO</button>';
		//	END TITLE, MESSAGE AND BUTTONS

		$trgModal.find(".modal-title").html(title);
		$trgModal.find(".modal-body").html(msg);
		$trgModal.find(".modal-footer").html(btns);

		$trgModal.modal('show');

		$('#btnUpdateSelectedSlideTemplate').attr("data-dismiss", "modal");
	}
}

function toggleDisplayResolutionArea(doDisplay, sceneId){

	if(doDisplay){
		$("#displayselection" + sceneId).css("display","block");
	}else{
		$("#displayselection" + sceneId).css("display","none");
	}
}

function updateSelectedSlideTemplate_Confirm(confirm) {


	if(confirm){
		updtSlctdSldeTmpltUserConfirmed();
	}


	$("#hidTemplateIdForSetupFormSelection").val('');

	APP.CACHE = "";
}


















function accordion_btn_actions(ID)
{
	var courseId = getParameterByName('id');
	var courseType = getParameterByName('cType');

	if (courseId ){
		switch(ID)
		{
			case "nav_accordion_0":
				checkForFormChanged('/lcms/editCourseOverview?id=' + courseId + "&cType=" +  WLCMS_CONSTANTS_COURSE_TYPE.ONLINE_COURSE);
				break;
			case "nav_accordion_0_cr":
				checkForFormChanged('/lcms/editClassroomWebinarCourse?id=' + courseId + "&cType=" + WLCMS_CONSTANTS_COURSE_TYPE.CLASSROOM_COURSE);
				break;
			case "nav_accordion_0_wb":
				checkForFormChanged('/lcms/editClassroomWebinarCourse?id=' + courseId + "&cType=" + WLCMS_CONSTANTS_COURSE_TYPE.WEBINAR_COURSE);
				break;
			case "nav_accordion_1":
				checkForFormChanged('/lcms/coursestructure?id=' + courseId + "&cType=" + courseType);
				break;
			case "nav_accordion_2":
				checkForFormChanged('/lcms/courseSettings?id=' + courseId + "&cType=" + courseType);
				break;
			case "nav_accordion_2c":
				checkForFormChanged('/lcms/coursesettingc?id=' + courseId + "&cType=" + courseType);
				break;
			case "nav_accordion_2w":
				checkForFormChanged('/lcms/coursesettingw?id=' + courseId + "&cType=" + courseType);
				break;
			case "pricing":
				checkForFormChanged('/lcms/pricing?id=' + courseId + "&cType=" + courseType);
				break;
			case "course_welcome":

				break;
			case "publishing":
				checkForFormChanged('/lcms/publishing?id=' + courseId + "&cType=" + courseType);
				break;
			case "webinar_publishing":
				checkForFormChanged('/lcms/webinar_publishing?id=' + courseId + "&cType=" + courseType);
				break;
			case "webinarSetup":
				checkForFormChanged('/lcms/webinarSetup?id=' + courseId + "&cType=" + courseType);
				break;
			case "marketing":
				checkForFormChanged('/lcms/setMarketing?id=' + courseId+ "&cType=" + courseType);
				break;
			case "availability":
				checkForFormChanged('/lcms/availability?id=' + courseId + "&cType=" + courseType);
			    break;
			case "schedule":
				checkForFormChanged('/lcms/schedule?id=' + courseId+ "&cType=" + courseType);
				break;
			case "instructor":
				checkForFormChanged('/lcms/instructor?id=' + courseId+ "&cType=" + courseType);
				break;
			case "leftMenuOfferOn360Marketplace":
				checkForFormChanged('/lcms/displayOfferPage?id=' + courseId+ "&cType=" + courseType);
				break;
			case "classroomsetup":
				checkForFormChanged('/lcms/classroom-classes?id=' + courseId+ "&cType=" + courseType);
				break;
			case "locationList":
				checkForFormChanged('/lcms/locationList?id=' + courseId+ "&cType=" + courseType);
				break;
		}
	}
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function toggle_leftNavPanel()
{

	var trgElm = $("#leftNavPanel");

	if(trgElm.css('width') != '42px')
	{
		APP.LEFT_NAV.width = trgElm.css('width');
		trgElm.css('width','42px');
		$("#leftNavPanel .list-holder .list-group").addClass("only-icon-panel");

	}
	else
	{
		trgElm.css('width',APP.LEFT_NAV.width);
		$("#leftNavPanel .list-holder .list-group").removeClass("only-icon-panel");

	}
}

function leftNavCollapse(trg,thisTrg)
{
	if(trg.css("display") == 'none')
	{
		thisTrg.removeClass("open-sign");
		thisTrg.addClass("close-sign");
	}
	else
	{
		thisTrg.removeClass("close-sign");
		thisTrg.addClass("open-sign");
	}

	trg.toggle(250);
}

function applyResizable(condition)
{
	if(condition)
	{

		$( "#leftNavPanel" ).resizable({
			maxWidth: 500,
			minWidth: 200,
			handles: 'e'
		});
	}
	else
	{

		$( "#leftNavPanel" ).resizable('destroy');
	}
}

function analyseScreenWidth(trg)
{
	if($(window).width() < 391)
	{
		if(trg.css('width') != '42px')
		{
			toggle_leftNavPanel();
		}
	}
}

