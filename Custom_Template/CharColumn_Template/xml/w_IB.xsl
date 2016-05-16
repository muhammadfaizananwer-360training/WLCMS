<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes"/>
  
  <xsl:template match="/">
    <div class="FormWrapper form-horizontal">
	
	    <!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/promptQuestionText/@label}">
			 <div class="caption">
				<h5><strong><xsl:value-of select="root/promptQuestionText/@label"/></strong></h5>
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
					<textarea id="insText_{root/@sid}" class="form-control" name="insText_{root/@sid}" rows="4"><xsl:value-of select="root/promptQuestionText" disable-output-escaping="yes"/></textarea>
				  </div>
			   </div>
			</div>
		  </div>
		</div>
		<!-- END PORTLET -->
		
		<!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/audioAssets/@label}">
			<div class="caption">
				<h5><strong><xsl:value-of select="root/audioAssets/@label"/> </strong> - 1 asset</h5>
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
						  <tbody class="audioAssets">
							<tr class="hide">
							  <td class="name"></td>
							  <td class="ver"></td>
							  <td class="path">
								<a class="btn blue-text" href="javascript:;" target="_blank"><i class="glyphicon glyphicon-play"></i></a>
							  </td>
						    </tr>
							<xsl:choose>
								<xsl:when test="root/audioAssets/@name != ''">
									<tr class="asset">
									  <td class="name"><xsl:value-of select="root/audioAssets/@name" /></td>
									  <td class="ver"><xsl:value-of select="root/audioAssets/@ver" /></td>
									  <td class="path">
										<a class="btn blue-text" href="{root/audioAssets}" target="_blank"><i class="glyphicon glyphicon-play"></i></a>
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
		  <div class="portlet-title" title="{root/columnTitles/@label}">
			 <div class="caption">
				<h5><strong><xsl:value-of select="root/columnTitles/@label"/></strong></h5>
			 </div>
			 <div class="tools">
				<i class="text-icon" id="slide_{root/@sid}_p3"></i>
				<a href="javascript:;" class="collapse"></a>
			 </div>
		  </div>
		  <div class="portlet-body">
			<div class="form-body">
			   <xsl:variable name="count" select="count(root/columnTitles/list)"/>
			   <div class="form-group gray-text">
					<label class="col-md-2 control-label">Column</label>
					<div class="col-md-8">
						<select class="form-control" onchange="TEMPLATE.CHART_COL_TEMPLATE.CHANGE_COLUMN(this)">
							<xsl:if test="$count = '2'">
								<option value="2">Two</option>
								<option value="3">Three</option>
							</xsl:if>
							<xsl:if test="$count = 3">
								<option value="2">Two</option>
								<option value="3" selected="selected">Three</option>
							</xsl:if>
						</select>
					</div>
			   </div>
			   <div class="form-group gray-text">
					<label class="col-md-2 control-label"></label>
					<div class="col-md-8">
						
						<div class="errPrepend">
							
							<div class="table-scrollable">
								<table class="table table-str-border table-bordered">
								  <thead>
									<tr class="td-header">
									  <td class="text-center">
										Column Titles
									  </td>
									</tr>
								  </thead>
								  <tbody class="columnTitles">
									
									<xsl:for-each select="root/columnTitles/list">
									  <xsl:variable name="i" select="position()" />
									
									  <xsl:choose>
										<xsl:when test="$i = 1">
										  <tr class="hide">
											<td><input type="text" class="form-control cat-title" onblur="TEMPLATE.CHART_COL_TEMPLATE.POPULATE_LIST()" value="Partial" placeholder="Enter the column titles."/></td>
										  </tr>
										</xsl:when>
									  </xsl:choose>
									
										<tr>
											<td><input type="text" class="form-control cat-title" onblur="TEMPLATE.CHART_COL_TEMPLATE.POPULATE_LIST()" value="{.}" placeholder="Enter the column titles." /></td>
										</tr>
									
									</xsl:for-each>
									
								  </tbody>
								</table>
							</div>
							
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
				<i class="text-icon" id="slide_{root/@sid}_p4"></i>
				<a href="javascript:;" class="collapse"></a>
			 </div>
		  </div>
		  <div class="portlet-body">
			<div role="alert" class="alert alert-warning">
				<i class="icon-warning-sign"></i> Click the Add Row button ( <span class="btn btn-default btn-sm" disabled="disabled"><i class="icon-plus"></i></span> ) to add more rows.
			</div>
			<div class="form-body">
			   <div class="form-group">
					<div class="col-md-offset-2 col-md-8">
					  <div class="errPrepend_0"></div>
					  <div class="errPrepend">
						<div class="table-scrollable">
							<table class="table table-str-border table-bordered">
							  <thead>
								<tr class="td-header">
								  <th colspan="7">
									<div class='btn-group'>
										<a class="btn btn-default upload cond" href="javascript:;" title="Add item" onclick="TEMPLATE.ADD_ROW(this,'_p4',6)"><i class="icon-plus blue-text"></i></a>
										<a class="btn btn-default detach disabled" onclick="TEMPLATE.DELETE_ROW(this,'_p4',false,6)" href="javascript:;" title="Delete item(s)"><i class="icon-minus red-text"></i></a>
									</div>
								  </th>
								</tr>
								<tr class="td-header-bottom-border">
								  <th style="width:1%"><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,true)" class="checker" /></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/questionStatement/@label"/></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/answer/@label"/></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/correctFeedBack/@label"/></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/correctAudio/@label"/></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/incorrectFeedback/@label"/></th>
								  <th style="width:16.5%"><xsl:value-of select="root/options/option/incorrectAudio/@label"/></th>
								</tr>
							  </thead>
							  <tbody class="options">
								
								<xsl:for-each select="root/options/option">
								  <xsl:variable name="i" select="position()" />
								
								  <xsl:choose>
									<xsl:when test="$i = 1">
									  <tr class="hide">
										<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
										<td class="edit-area questionStatement" data-modal-title="{questionStatement/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												
											</div>
										</td>
										<td class="answer">
											<select class="form-control" onfocus="TEMPLATE.CHART_COL_TEMPLATE.CHECK_COLUMN(this)">
												<option value="0">--Select--</option>
												<xsl:for-each select="../../columnTitles/list">
													<option value="{@value}"><xsl:value-of select="(.)" disable-output-escaping="yes"/></option>
												</xsl:for-each>
											</select>
										</td>
										<td class="edit-area correctFeedBack" data-modal-title="{correctFeedBack/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												
											</div>
										</td>
										<td class="edit-area correctAudio" data-modal-title="{correctAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
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
										<td class="edit-area incorrectFeedback" data-modal-title="{incorrectFeedback/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												
											</div>
										</td>
										<td class="edit-area incorrectAudio" data-modal-title="{incorrectAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
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
									<xsl:when test="questionStatement != ''">
										<tr>
											<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
											<td class="edit-area questionStatement" data-modal-title="{questionStatement/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
												<div class="content">
													<xsl:value-of select="string(questionStatement)" disable-output-escaping="yes"/>
												</div>
											</td>
											<td class="answer">
												<xsl:variable name="cTLId" select="answer/@columnTitleListId"/>
												<select class="form-control" onfocus="TEMPLATE.CHART_COL_TEMPLATE.CHECK_COLUMN(this)">
													<option value="0">--Select--</option>
													<xsl:for-each select="../../columnTitles/list">
														<xsl:choose>
															<xsl:when test="@value = $cTLId">
																<option value="{@value}" selected="selected"><xsl:value-of select="."/></option>
															</xsl:when>
															<xsl:otherwise>
																<option value="{@value}"><xsl:value-of select="."/></option>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:for-each>
												</select>
											</td>
											<td class="edit-area correctFeedBack" data-modal-title="{correctFeedBack/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
												<div class="content">
													<xsl:value-of select="correctFeedBack" disable-output-escaping="yes"/>
												</div>
											</td>
											<td class="edit-area correctAudio" data-modal-title="{correctAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
												<div class="content">
													<div class="asset">
													  <span class="name"><xsl:value-of select="correctAudio/@name" /></span>
													  <span class="ver hide"><xsl:value-of select="correctAudio/@ver" /></span>
													  <span class="path hide">
														<a href="{correctAudio}" target="_blank"></a>
													  </span>
													</div>
												</div>
											</td>
											<td class="edit-area incorrectFeedback" data-modal-title="{incorrectFeedback/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
												<div class="content">
													<xsl:value-of select="incorrectFeedback" disable-output-escaping="yes"/>
												</div>
											</td>
											<td class="edit-area incorrectAudio" data-modal-title="{incorrectAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
												<div class="content">
													<div class="asset">
													  <span class="name"><xsl:value-of select="incorrectAudio/@name" /></span>
													  <span class="ver hide"><xsl:value-of select="incorrectAudio/@ver" /></span>
													  <span class="path hide">
														<a href="{incorrectAudio}" target="_blank"></a>
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
		</div>
		<!-- END PORTLET -->
		
    </div>
  </xsl:template>
</xsl:stylesheet>