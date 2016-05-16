var TEMPLATE = function () {

    return {
		IS_VALID:'',
		DATA:'',
		CACHE:'',
		SID:'',
		IS_NEW:'',
		GARBAGE_CLEANER_LOCKED : false,
		//	BEGIN CHECKALL
		CHECKBOX: function (trg,isChecker){

			$table = $(trg).parent().parent().parent().parent();
			var isAnyCheck = false;
			if(isChecker)
			{
				//	HEADER CHECKBOX CONTROL BODY CHECKBOXES
				$tbody = $table.find('tbody');
				$tbody.children().each(function(index, element){
					//	Update Body CheckBoxes
					if(index !== 0)
					{
						$(element).find('.checks').prop('checked',trg.checked);
					}
					if(trg.checked && !isAnyCheck)
					{
						isAnyCheck = true;
					}
				});
				//	Recheck in case no records available in the grid
				if($tbody.children().length === 1)
				{
					isAnyCheck = false;
				}
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
					$tbody.children().each(function(index, element)
					{
						var chk = $(element).find('.checks').prop('checked');
						if(!chk && index >= 1)
						{
							$isCheckAll = false;
						}
						if(index !== 0 && chk && !isAnyCheck)
						{
							isAnyCheck = true;
						}
					});
					$allChecker.prop('checked',$isCheckAll);
				}
				else
				{
					$allChecker.prop('checked',trg.checked);
					$tbody.children().each(function(index, element){
						var chk = $(element).find('.checks').prop('checked');
						if(index !==0 && chk && !isAnyCheck)
						{
							isAnyCheck = true;
						}
					});
				}
			}
			var detachBtn = $table.find(".detach");
			if(typeof detachBtn !== "undefined" && isAnyCheck)
			{
				detachBtn.removeClass("disabled");
			}
			else
			{
				detachBtn.addClass("disabled");
			}
		},
		//	END CHECKALL
		//	BEGIN GREEN ICON
		GREEN_ICON:function(trg,appear){
			if(appear)
			{
				if(typeof TEMPLATE.CACHE._this !== "undefined"){
					TEMPLATE.CACHE._this.parent().parent().find(".detach").removeClass("disabled");
				}
				if(trg !== false)
				{
					trg.addClass("green");
				}
			}
			else
			{
				if(typeof TEMPLATE.CACHE._this !== "undefined"){
					TEMPLATE.CACHE._this.parent().parent().find(".detach").addClass("disabled");
				}
				if(trg !== false)
				{
					trg.removeClass("green");
				}
			}
		},
		//	END GREEN ICON
		//	BEGIN CKEDITOR BLUR
		CKEDITOR_BLUR:function(ID,icon_id){
			var cke = CKEDITOR.instances[ID];
			cke._iconId = icon_id;
			cke.on('instanceReady', function (event)
			{
				this.on('blur', function(e)
				{
					var str = this.getData();
					str = str.replace(/[\n\r\s]+/g, '');
					TEMPLATE.GREEN_ICON($("#slide_"+TEMPLATE.SID+this._iconId),str.length !== 0);
				});
			});
		},
		//	END CKEDITOR BLUR
		//	BEGIN LOAD XML
		LOAD_XML:function(path,returnFn){
			$.ajax({
				type: "GET",
				url: path,
				dataType: "xml",
				success: function(xml) {
					returnFn(xml);
				}
			});
		},
		//	END LOAD XML
		//	BEGIN LOAD MODAL VIEW
		MODAL_VIEW:function(eID,htmlPage){
			$(eID).html("<div class='modal-header text-center'>Loading...</div>");
			$(eID).load(htmlPage);
		},
		//	END LOAD MODAL VIEW
		//	BEGIN DYNAMIC MODAL GARBAGE CLEANER
		DM_GARBAGE_CLEANER: function(){
			$('#dynamicModal').on('hidden.bs.modal', function (e) {
				$(this).find(".modal-content").html("");
				if(!TEMPLATE.GARBAGE_CLEANER_LOCKED){
					TEMPLATE.CACHE = "";
				}
			})
		},
		//	END DYNAMIC MODAL GARBAGE CLEANER
		//	BEGIN DYNAMIC MODAL
		DYNAMIC_MODAL: function(style,trg,htmlPage,type,icon_id,_limit){
			var $trg = $(trg);
			if($trg.hasClass("disabled"))
			{
				return;
			}
			var obj = {
				_this : $(trg),
				_table : $(trg).parent().parent().parent().parent().parent().find("tbody"),
				_style : style,//"Table",
				_type : type,
				_iconId : icon_id,
				_limit : (typeof _limit === "undefined")?1:_limit
			};
			TEMPLATE.CACHE = obj;
			pageRelatedModal = getRelatedRefreshedModal(htmlPage);
			if(pageRelatedModal) {
				//	Begin - Use in tech people
				$(pageRelatedModal).modal('show');
				//	End - Use in tech people
			}
			else {
				var $mElm = $("#dynamicModal");
				TEMPLATE.MODAL_VIEW($mElm.find(".modal-content"),"theme/Modals/"+htmlPage+".html");
				$mElm.modal('show');
			}
		},
		//	END DYNAMIC MODAL
		//	BEGIN GET ASSET DATA
		GET_ASSET_DATA:function($elm){
			return {name:$elm.find(".name").text(),
			ver:$elm.find(".ver").text(),
			path:$elm.find(".path > a").attr("href")}
		},
		//	END GET ASSET DATA
		//	BEGIN ASSET DATA
		ASSET_DATA:{
			name:"",
			ver:"",
			path:""
		},
		//	END ASSET DATA
		//	BEGIN RETURN DYNAMIC MODAL
		RETURN_DYNAMIC_MODAL: function(name,ver,path){
			TEMPLATE.ASSET_DATA.name = name;
			TEMPLATE.ASSET_DATA.ver = ver;
			TEMPLATE.ASSET_DATA.path = path;
			TEMPLATE.SET_ASSET();
			//	Begin - Enhance Version ***************************************
			if(TEMPLATE.CACHE._table.children().length > TEMPLATE.CACHE._limit)
			{
				var $btns = TEMPLATE.CACHE._this.parent();
				$btns.find(".find").addClass("disabled");
				$btns.find(".upload").addClass("disabled");
			}
			if(TEMPLATE.CACHE._limit !== 1)
			{
				delete TEMPLATE.CACHE._this;
			}
			//	End - Enhance Version ***************************************
			TEMPLATE.GREEN_ICON($("#slide_"+TEMPLATE.SID+TEMPLATE.CACHE._iconId),true); //TEMPLATE.CACHE._isSingle
			$("#dynamicModal").modal('hide');
			TEMPLATE.CACHE = '';
			//	mobile out of focus on hide - BUG
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		},
		//	END RETURN DYNAMIC MODAL
		//	BEGIN SET ASSET
		SET_ASSET:function(){
			var $asset;
			if(TEMPLATE.CACHE._style === "Table")
			{
				$asset = TEMPLATE.CACHE._table.find(".asset");
			}
			else
			{
				$asset = TEMPLATE.CACHE._this.find(".asset");
			}
			var isClone = typeof $asset.html() === "undefined";
			if(TEMPLATE.CACHE._limit !== 1)
			{
				isClone = true;
			}
			//	Clone Check
			if(isClone)
			{
				if(TEMPLATE.CACHE._style === "Table")
				{
					$asset = TEMPLATE.CACHE._table.find(".hide").clone();
				}
				else
				{
					$asset = TEMPLATE.CACHE._this.find(".hide").clone();
				}
				$asset.removeClass("hide");
				$asset.addClass("asset");
			}
			//	Set Values
			$asset.find(".name").html(TEMPLATE.ASSET_DATA.name);
			$asset.find(".ver").html(TEMPLATE.ASSET_DATA.ver);
			$asset.find(".path > a").attr("href",TEMPLATE.ASSET_DATA.path);
			if(TEMPLATE.CACHE._type === "image"){
				$asset.find(".path > a > img").attr("src",TEMPLATE.ASSET_DATA.path);
			}
			//	Append if Clone true
			if(isClone)
			{
				TEMPLATE.CACHE._table.append($asset);
			}
		},
		//	END SET ASSET
		//	BEGIN TEXT MODAL
		TEXT_MODAL: function(trg){
			TEMPLATE.CACHE = trg;
			var $mElm = $("#dynamicModal");
			TEMPLATE.MODAL_VIEW($mElm.find(".modal-content"),"theme/Modals/TextEditor.html");
			$mElm.modal('show');
		},
		//	END TEXT MODAL
		//	BEGIN RETURN TEXT MODAL
		RETURN_TEXT_MODAL: function(){
			var ckModalData = CKEDITOR.instances.ckModalText.getData();
			var $ckModalElement = $("#cke_ckModalText");
			if(TEMPLATE.VALIDATE.MATCH($ckModalElement,ckModalData,""))
			{
				return;
			}
			$(TEMPLATE.CACHE).find(".content").html(ckModalData);
			TEMPLATE.CACHE = '';
			$("#dynamicModal").modal('hide');
			//	mobile out of focus on hide - BUG
			$('body').removeClass('modal-open');
			$('.modal-backdrop').remove();
		},
		//	END RETURN TEXT MODAL
		//	BEGIN ADD ROW
		ADD_ROW: function(trg,icon_id,limit){
			var $trg = $(trg);
			if($trg.hasClass("disabled"))
			{
				return;
			}
			var $table = $(trg).parent().parent().parent().parent().parent();
			$table.find("thead").find(".checker").prop("checked",false);
			var $tbody = $table.find("tbody");
			var $row = $tbody.find("tr:first-child").clone();
			$row.find("td > .checks").prop("checked",false);
			var classess = $row.attr("class");
			if(typeof classess !== "undefined")
			{
				var length = classess.split(" ").length;
				if(length === 1)
				{
					$row.removeAttr("class");
				}
				else
				{
					$row.removeClass("hide");
				}
			}
			$tbody.append($row);
			TEMPLATE.GREEN_ICON($("#slide_"+TEMPLATE.SID+icon_id),true);
			//	Apply Limitation
			TEMPLATE.LIMIT_CHECK_ROW($(trg),$tbody.children().length,limit);
		},
		//	END ADD ROW
		//	BEGIN DELETE ROW
		DELETE_ROW: function (trg,icon_id,isSingle,limit){
			var $trg = $(trg);
			if($trg.hasClass("disabled"))
			{
				return;
			}
			
			var $table = $trg.parent().parent().parent().parent().parent();
			var $allChecker = $table.find('thead');
			$allChecker = $allChecker.find('.checker');
			$table = $table.find('tbody');
			var obj = {
				_this: $trg,
				_table: $table,
				_checks: $allChecker,
				_iconId: icon_id,
				_isSingle: isSingle
			};
			if(typeof limit !== "undefined")
			{
				obj._limit = limit;
			}
			TEMPLATE.CACHE = obj;
			var $trgModal = $("#confirmationModal");
			//	BEGIN TITLE, MESSAGE AND BUTTONS
			var title = '<i class="icon-exclamation-sign"></i> Please Confirm';
			var msg = '<p>Are you sure you want to delete selected item(s)?</p>';
			var btns = '<button type="button" class="btn blue" onclick="TEMPLATE.CONFIRM_DELETE_ROW(true)" data-dismiss="modal">YES</button>'+
						'<button type="button" class="btn btn-default" onclick="TEMPLATE.CONFIRM_DELETE_ROW(false)" data-dismiss="modal">NO</button>';
			//	END TITLE, MESSAGE AND BUTTONS
			$trgModal.find(".modal-title").html(title);
			$trgModal.find(".modal-body").html(msg);
			$trgModal.find(".modal-footer").html(btns);
			$trgModal.modal('show');
		},
		//	END DELETE ROW
		//	BEGIN CONFIRM DELETE ROW
		CONFIRM_DELETE_ROW: function (confirm){
			if(confirm)
			{
				var $table = TEMPLATE.CACHE._table;
				//	Begin - Enhance Version ***************************************
				if(TEMPLATE.CACHE._table.children().length > TEMPLATE.CACHE._limit || typeof TEMPLATE.CACHE._limit === "undefined")
				{
					var $btns = TEMPLATE.CACHE._this.parent();
					$btns.find(".find").removeClass("disabled");
					$btns.find(".upload").removeClass("disabled");

				}
				//	End - Enhance Version ***************************************
				//	DELETE
				$table.children().each(function(index,element)
				{
					if($(element).find('.checks').prop('checked') === true || TEMPLATE.CACHE._isSingle === true)
					{
						if($(element).attr("class") !== "hide")
						{
							$(element).remove();
						}
					}
				});
				$(TEMPLATE.CACHE._checks).prop("checked",false);
				if($table.children().length > 1)
				{
					TEMPLATE.GREEN_ICON(false,false);
				}
				else
				{
					TEMPLATE.GREEN_ICON($("#slide_"+TEMPLATE.SID+TEMPLATE.CACHE._iconId),false);
				}
				//	Limitation Checking
				TEMPLATE.LIMIT_CHECK_ROW($(TEMPLATE.CACHE._this).parent().find(".upload"),0,TEMPLATE.CACHE._limit);
			}
			TEMPLATE.CACHE = "";
		},
		//	END CONFIRM DELETE ROW
		//	BEGIN LIMITATION ROW CHECK
		LIMIT_CHECK_ROW: function($trg,length,limit){
			if(typeof limit !== "undefined")
			{
				if(length > limit)
				{
					$trg.addClass("disabled");
				}
				else
				{
					$trg.removeClass("disabled");
				}
			}
		},
		//	END LIMITATION ROW CHECK
		//	BEGIN MOVE ROW
		MOVE_ROW: function(isUp,tBodyTrg){
			var $row;
			if(isUp)
			{
				$(tBodyTrg).children().each(function(i,elm){
					if(i !== 0)
					{
						$row = $(elm);
						if($row.find(".checks").prop("checked"))
						{
							if($row.prev().attr("class") !== "hide")
							{
								$row.insertBefore($row.prev());
							}
						}
					}
				});
			}
			else
			{
				$(tBodyTrg).children().each(function(i,elm){
					if(i !== 0)
					{
						$row = $(elm);
						if($row.find(".checks").prop("checked"))
						{
							if($row.next().attr("class") !== "hide")
							{
								$row.insertAfter($row.next());
							}
						}
					}
				});
			}
		},
		//	END MOVE ROW
		//	BEGIN FIX HTML TAGS
		FIX_HTML_TAGS: function ($trg){
			$trg.children().each(function(i,element)
			{
				var html = String($(this).html());
				html = html.replace(/&lt;/gi,'<');
				html = html.replace(/&gt;/gi,'>');
				$(this).html(html);
			});
		},
		//	END FIX HTML TAGS
		//	BEGIN VALIDATE
		VALIDATE:{
			//	BEGIN MESSAGES
			MSG : {
				BLANK:"<div class='error'>The indicated field(s) must be completed.</div>"
			},
			//	END MESSAGES
			MSG_DIV:".errPrepend",
			//	BEGIN ERROR ALERT
			ERROR_ALERT:function($trg,msg,$panel){
				var $errHolder;
				if(typeof $panel === "undefined")
				{
					$errHolder = $trg.parent();
				}
				else
				{
					var $head = $panel.find(".portlet-title > .tools > a");
					$head.removeClass("expand");
					$head.addClass("collapse");
					var $body = $panel.find(".portlet-body");
					$body.css("display","block");
					var $errPrepend = $body.find(TEMPLATE.VALIDATE.MSG_DIV); //".errPrepend"
					if(typeof $errPrepend.html() !== "undefined")
					{
						$errHolder = $errPrepend;
					}
					else
					{
						$errHolder = $body;
					}
				}
				var $err = $errHolder.find(".error");
				if(typeof $err.html() === "undefined")
				{
					$errHolder.prepend(TEMPLATE.VALIDATE.MSG.BLANK);
				}
				$trg.addClass("errorIn");
			},
			//	BEGIN ERROR ALERT
			//	BEGIN ERROR FREE
			ERROR_FREE:function($trg,$panel,dontHideMsg){
				var $errHolder;
				if(typeof $panel === "undefined")
				{
					$errHolder = $trg.parent();
				}
				else
				{
					$errHolder = $panel.find(".portlet-body");
				}
				var $errHolderTemp = $errHolder.find(TEMPLATE.VALIDATE.MSG_DIV);
				if(typeof $errHolderTemp !== "undefined")
				{
					$errHolder = $errHolderTemp;
				}
				if(typeof dontHideMsg === "undefined")
				{
					$errHolder.find(".error").remove();
				}
				$trg.removeClass("errorIn");
			},
			//	BEGIN ERROR FREE
			//	BEGIN MATCH
			MATCH: function($trg,value,check,$panel,dontHideMsg){
				var checkArr = String(check).split("|");
				var result = false;
				for(var i=0; i<checkArr.length; i++)
				{
					if(!result)
					{
						result = Boolean(value === checkArr[i]);
					}
				}
				if(result)
				{
					TEMPLATE.VALIDATE.ERROR_ALERT($trg,msg,$panel);
				}
				else
				{
					if(typeof dontHideMsg === "undefined" || dontHideMsg === "undefined")
					{
						TEMPLATE.VALIDATE.ERROR_FREE($trg,$panel);
					}
					else
					{
						TEMPLATE.VALIDATE.ERROR_FREE($trg,$panel,true);
					}
				}
				return result;
			}
			//	END MATCH
		},
		//	END VALIDATE
		//	BEGIN XML TO STRING
		XML_TO_STRING: function(elem){
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
		//	END XML TO STRING
	}
}();
