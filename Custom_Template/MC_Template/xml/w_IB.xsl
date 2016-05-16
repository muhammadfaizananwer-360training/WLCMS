<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" indent="yes"/>
  
  <xsl:template match="/">
    <div class="FormWrapper form-horizontal">
	
	    <!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/mainText/@label}">
			 <div class="caption">
				<h5><strong><xsl:value-of select="root/mainText/@label"/></strong></h5>
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
					<textarea id="mainText_{root/@sid}" class="form-control" name="mainText_{root/@sid}" rows="4"><xsl:value-of select="root/mainText" disable-output-escaping="yes"/></textarea>
				  </div>
			   </div>
			</div>
		  </div>
		</div>
		<!-- END PORTLET -->
		
		<!-- BEGIN PORTLET -->
		<div class="portlet portlet-btn">
		  <div class="portlet-title" title="{root/questionText/@label}">
			 <div class="caption">
				<h5><strong><xsl:value-of select="root/questionText/@label"/></strong></h5>
			 </div>
			 <div class="tools">
				<i class="text-icon" id="slide_{root/@sid}_p2"></i>
				<a href="javascript:;" class="collapse"></a>
			 </div>
		  </div>
		  <div class="portlet-body">
			<div class="form-body">
			   <div class="form-group">
				  <div class="col-md-offset-2 col-md-8 errPrepend">
					<textarea id="questionText_{root/@sid}" class="form-control" name="questionText_{root/@sid}" rows="4"><xsl:value-of select="root/questionText" disable-output-escaping="yes"/></textarea>
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
				<i class="audio-icon" id="slide_{root/@sid}_p3"></i>
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
									<a class="btn btn-default find" href="javascript:;" title="Find Audio Asset and Attach to Slide" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'SearchAudioAsset','audio','_p3')"><i class="glyphicon glyphicon-new-window"></i></a>
									<a class="btn btn-default upload" href="javascript:;" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'UploadAudioAsset','audio','_p3')" title="Upload Audio Asset and Attach to Slide"><i class="icon-plus blue-text"></i></a>
									<a class="btn btn-default detach disabled must" href="javascript:;" onclick="TEMPLATE.DELETE_ROW(this,'_p3',true)" title="Detach Asset"><i class="icon-minus red-text"></i></a>
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
		  <div class="portlet-title" title="{root/leftImage/@label}">
			<div class="caption">
				<h5><strong><xsl:value-of select="root/leftImage/@label"/> </strong> - 1 asset</h5>
			</div>
			<div class="tools">
				<i class="camera-icon" id="slide_{root/@sid}_p4"></i>
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
									<a class="btn btn-default find" href="javascript:;" title="Find Visual Asset and Attach to Slide" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'SearchImageAsset','image','_p4')"><i class="glyphicon glyphicon-new-window"></i></a>
									<a class="btn btn-default upload" href="javascript:;" onclick="TEMPLATE.DYNAMIC_MODAL('Table',this,'UploadImageAsset','image','_p4')" title="Upload Visual Asset and Attach to Slide"><i class="icon-plus blue-text"></i></a>
									<a class="btn btn-default detach disabled must" href="javascript:;" onclick="TEMPLATE.DELETE_ROW(this,'_p4',true)" title="Detach Asset"><i class="icon-minus red-text"></i></a>
								</div>
							  </th>
							</tr>
							<tr class="td-header-bottom-border">
							  <th style="width:70%">Asset Name</th>
							  <th style="width:15%">Version</th>
							  <th style="width:15%">Preview</th>
							</tr>
						  </thead>
						  <tbody class="leftImage">
							<tr class="hide">
							  <td class="name"></td>
							  <td class="ver"></td>
							  <td class="path">
								<a href="javascript:;" target="_blank">
									<img src="." style="width:36px" />
								</a>
							  </td>
							</tr>
							<xsl:choose>
								<xsl:when test="root/leftImage/@name != ''">
									<tr class="asset">
									  <td class="name"><xsl:value-of select="root/leftImage/@name" /></td>
									  <td class="ver"><xsl:value-of select="root/leftImage/@ver" /></td>
									  <td class="path">
										<a href="{root/leftImage}" target="_blank">
											<img src="{root/leftImage}" style="width:36px" />
										</a>
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
				<i class="text-icon" id="slide_{root/@sid}_p5"></i>
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
							  <th colspan="5">
								<div class='btn-group'>
									<a class="btn btn-default upload" href="javascript:;" title="Add item" onclick="TEMPLATE.ADD_ROW(this,'_p5');"><i class="icon-plus blue-text"></i></a>
									<a class="btn btn-default detach disabled" onclick="TEMPLATE.DELETE_ROW(this,'_p5',false)" href="javascript:;" title="Delete item(s)"><i class="icon-minus red-text"></i></a>
								</div>
							  </th>
							</tr>
							<tr class="td-header-bottom-border">
							  <th style="width:1%"><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,true)" class="checker" /></th>
							  <th style="width:25%"><xsl:value-of select="root/options/option/optionText/@label"/></th>
							  <th style="width:25%"><xsl:value-of select="root/options/option/feedBack/@label"/></th>
							  <th style="width:25%"><xsl:value-of select="root/options/option/optionAudio/@label"/></th>
							  <th style="width:24%"><xsl:value-of select="root/options/option/optionStatus/@label"/></th>
							</tr>
						  </thead>
						  <tbody class="options">
							
							<xsl:for-each select="root/options/option">
							  <xsl:variable name="i" select="position()" />
							
							  <xsl:choose>
								<xsl:when test="$i = 1">
								  <tr class="hide">
									<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
									<td class="edit-area optionText" data-modal-title="{optionText/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area feedBack" data-modal-title="{feedBack/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
										<div class="content">
											
										</div>
									</td>
									<td class="edit-area optionAudio" data-modal-title="{optionAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
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
									<td class="optionStatus">
										<select class="form-control input-medium">
											<xsl:for-each select="optionStatus/list">
											  <option value="{@value}"><xsl:value-of select="."/></option>
											</xsl:for-each>
										</select>
									</td>
								  </tr>
								</xsl:when>
							  </xsl:choose>
							
							  <xsl:choose>
								<xsl:when test="optionText != ' '">
									<tr>
										<td><input type="checkbox" onclick="TEMPLATE.CHECKBOX(this,false)" class="checks" /></td>
										<td class="edit-area optionText" data-modal-title="{optionText/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<!-- <xsl:value-of select="util:strip-tags(optionText)" /> -->
												<xsl:value-of select="string(optionText)" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area feedBack" data-modal-title="{feedBack/@label}" onclick="TEMPLATE.TEXT_MODAL(this)">
											<div class="content">
												<xsl:value-of select="feedBack" disable-output-escaping="yes"/>
											</div>
										</td>
										<td class="edit-area optionAudio" data-modal-title="{optionAudio/@label}" onclick="TEMPLATE.DYNAMIC_MODAL('Cell',this,'MultiPanelAsset','audio')">
											<div class="content">
												<div class="asset">
												  <span class="name"><xsl:value-of select="optionAudio/@name" /></span>
												  <span class="ver hide"><xsl:value-of select="optionAudio/@ver" /></span>
												  <span class="path hide">
													<a href="{optionAudio}" target="_blank"></a>
												  </span>
												</div>
											</div>
										</td>
										<td class="optionStatus">
											<select class="form-control input-medium">
												<xsl:for-each select="optionStatus/list">
												  <xsl:choose>
													<xsl:when test="@value = ../@value">
													  <option value="{@value}" selected="selected"><xsl:value-of select="."/></option>
													</xsl:when>
													<xsl:otherwise>
													  <option value="{@value}"><xsl:value-of select="."/></option>
													</xsl:otherwise>
												  </xsl:choose>
												</xsl:for-each>
											</select>
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