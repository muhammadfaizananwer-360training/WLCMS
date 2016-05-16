<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes"/>
  
  <xsl:template match="/">
    <div class="FormWrapper form-horizontal">
	
	    <!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/instructionText/@label}">
			 <div class="caption">
				<h5><strong><xsl:value-of select="root/instructionText/@label"/></strong></h5>
			 </div>
			 <div class="tools">
				<i class="text-icon" id="slide_{root/@sid}_p1"></i>
				<a href="javascript:;" class="collapse"></a>
			 </div>
		  </div>
		  <div class="portlet-body">
			<div class="form-body">
			   <div class="form-group">
				  <div class="col-md-offset-2 col-md-8 errPrepend">
					<textarea id="insText_{root/@sid}" class="form-control" name="insText_{root/@sid}" rows="4"><xsl:value-of select="root/instructionText" disable-output-escaping="yes"/></textarea>
				  </div>
			   </div>
			</div>
		  </div>
		</div>
		<!-- END PORTLET -->
		
		<!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/promptAudio/@label}">
			<div class="caption">
				<h5><strong><xsl:value-of select="root/promptAudio/@label"/> </strong> - 1 asset</h5>
			</div>
			<div class="tools">
				<i class="audio-icon" id="slide_{root/@sid}_p2"></i>
				<a href="javascript:;" class="collapse"></a>
			</div>
		  </div>
		  <div class="portlet-body form">
			<div class="form-body">
			   <div class="form-group">
				  <div class="col-md-offset-2 col-md-8 errPrepend">
					<div class="table-scrollable">
						<table class="table table-str-border table-bordered">
						  <thead>
							<tr class="td-header">
							  <th colspan="4">
								<div class='btn-group'>
									<a class="btn btn-default find" href="javascript:;" title="Find Audio Asset and Attach to Slide" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'SearchAudioAsset','audio','_p2')"><i class="glyphicon glyphicon-new-window"></i></a>
									<a class="btn btn-default upload" href="javascript:;" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'UploadAudioAsset','audio','_p2')" title="Upload Audio Asset and Attach to Slide"><i class="icon-plus blue-text"></i></a>
									<a class="btn btn-default detach disabled" href="javascript:;" onclick="TEMPLATE.DELETE_ROW(this,'_p2',true)" title="Detach Asset"><i class="icon-minus red-text"></i></a>
								</div>
							  </th>
							</tr>
							<tr class="td-header-bottom-border">
							  <th style="width:70%">Asset Name</th>
							  <th style="width:15%">Version</th>
							  <th style="width:15%">Preview</th>
							</tr>
						  </thead>
						  <tbody class="promptAudio">
							<tr class="hide">
							  <td class="name"></td>
							  <td class="ver"></td>
							  <td class="path">
								<a class="btn blue-text" href="javascript:;" target="_blank"><i class="glyphicon glyphicon-play"></i></a>
							  </td>
						    </tr>
							<xsl:choose>
								<xsl:when test="root/promptAudio/@name != ''">
									<tr class="asset">
									  <td class="name"><xsl:value-of select="root/promptAudio/@name" /></td>
									  <td class="ver"><xsl:value-of select="root/promptAudio/@ver" /></td>
									  <td class="path">
										<a class="btn blue-text" href="{root/promptAudio}" target="_blank"><i class="glyphicon glyphicon-play"></i></a>
									  </td>
									</tr>
								</xsl:when>
							</xsl:choose>
						  </tbody>
						</table>
					</div>
				  </div>
			   </div>
			</div>
		  </div>
		</div>
		<!-- END PORTLET -->
		
		<!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/options/@label}">
			<div class="caption">
				<h5><strong><xsl:value-of select="root/options/@label"/></strong></h5>
			</div>
			<div class="tools">
				<i class="text-icon" id="slide_{root/@sid}_p3"></i>
				<a href="javascript:;" class="collapse"></a>
			</div>
		  </div>
		  <div class="portlet-body form">
			<div role="alert" class="alert alert-warning">
				<i class="icon-warning-sign"></i> In this activity, learners drag objects to drop spots. You may have between 2 and 5 drop spots; fill in as many rows below as you desire drop spots to display. The drop spots will display in the order they are shown here; to change the order, select the drop spot you want to move and click the up arrow <span class="btn btn-default" disabled="disabled"><i class="icon-move-up"></i></span> or down arrow <span class="btn btn-default" disabled="disabled"><i class="icon-move-down"></i></span> to change its position.
			</div>
			<div class="form-body">
			   <div class="form-group">
			      <div class="col-md-offset-2 col-md-8 errPrepend_0"></div>
				  <div class="col-md-offset-2 col-md-8 errPrepend">
					  
					  <div class="table-scrollable">
						<table class="table table-str-border table-bordered">
						  <thead>
							<tr class="td-header">
							  <th colspan="7">
								<div class='btn-group'>
									<a class="btn btn-default upload cond" href="javascript:;" title="Add item" onclick="TEMPLATE.ADD_ROW(this,'_p3',5);"><i class="icon-plus blue-text"></i></a>
									<a class="btn btn-default detach disabled" onclick="TEMPLATE.DELETE_ROW(this,'_p3',false,5)" href="javascript:;" title="Delete item(s)"><i class="icon-minus red-text"></i></a>
								</div>
								<div class='btn-group' style="padding-left:10px">
									<a title="Move Up" class="btn btn-default" href="javascript:TEMPLATE.MOVE_ROW(true,'.options');"><i class="icon-move-up"></i></a>
									<a title="Move Down" class="btn btn-default" href="javascript:TEMPLATE.MOVE_ROW(false,'.options');"><i class="icon-move-down"></i></a>
								</div>
							  </th>
							</tr>
							<tr class="td-header-bottom-border">
							  <th style="width:1%"><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,true)" class="checker" /></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/dropText/@label"/></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/draggableObject/@label"/></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/feedback/@label"/></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/feedbackAudio/@label"/></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/hint/@label"/></th>
							  <th style="width:16.5%"><xsl:value-of select="root/options/option/hintAudio/@label"/></th>
							</tr>
						  </thead>
						  <tbody class="options">
							
							<xsl:for-each select="root/options/option">
							  <xsl:variable name="i" select="position()" />
							
							  <xsl:choose>
								<xsl:when test="$i = 1">
								  <tr class="hide">
									<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
									<td class="edit-area dropText" data-modal-title="{dropText/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area draggableObject" data-modal-title="{draggableObject/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area feedback" data-modal-title="{feedback/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area feedbackAudio" data-modal-title="{feedbackAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
										<div class="content">
											<div class="asset">
											  <span class="name"></span>
											  <span class="ver hide"></span>
											  <span class="path hide">
												<a href="." target="_blank"></a>
											  </span>
											</div>
										</div>
									</td>
									<td class="edit-area hint" data-modal-title="{hint/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area hintAudio" data-modal-title="{hintAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
										<div class="content">
											<div class="asset">
											  <span class="name"></span>
											  <span class="ver hide"></span>
											  <span class="path hide">
												<a href="." target="_blank"></a>
											  </span>
											</div>
										</div>
									</td>
								  </tr>
								</xsl:when>
							  </xsl:choose>
							
							  <xsl:choose>
								<xsl:when test="dropText != ''">
									<tr>
										<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
										<td class="edit-area dropText" data-modal-title="{dropText/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<xsl:value-of select="string(dropText)" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area draggableObject" data-modal-title="{draggableObject/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<xsl:value-of select="draggableObject" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area feedback" data-modal-title="{feedback/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<xsl:value-of select="feedback" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area feedbackAudio" data-modal-title="{feedbackAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
											<div class="content">
												<div class="asset">
												  <span class="name"><xsl:value-of select="feedbackAudio/@name" /></span>
												  <span class="ver hide"><xsl:value-of select="feedbackAudio/@ver" /></span>
												  <span class="path hide">
													<a href="{feedbackAudio}" target="_blank"></a>
												  </span>
												</div>
											</div>
										</td>
										<td class="edit-area hint" data-modal-title="{hint/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<xsl:value-of select="hint" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area hintAudio" data-modal-title="{hintAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
											<div class="content">
												<div class="asset">
												  <span class="name"><xsl:value-of select="hintAudio/@name" /></span>
												  <span class="ver hide"><xsl:value-of select="hintAudio/@ver" /></span>
												  <span class="path hide">
													<a href="{hintAudio}" target="_blank"></a>
												  </span>
												</div>
											</div>
										</td>
									</tr>
								</xsl:when>
							  </xsl:choose>
							
							</xsl:for-each>
							
						  </tbody>
						</table>
					</div>
				  </div>
			   </div>
			</div>
		  </div>
		</div>
		<!-- END PORTLET -->
		
    </div>
  </xsl:template>
</xsl:stylesheet>