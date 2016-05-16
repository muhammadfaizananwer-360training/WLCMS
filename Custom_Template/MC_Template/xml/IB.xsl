<?xml version="1.0" encoding="utf-8"?>
<!-- DWXMLSource="mc_scenario.xml" -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="html" encoding="utf-8" doctype-public="-//WAPFORUM//DTD XHTML Mobile 1.0//EN" doctype-system="http://www.wapforum.org/DTD/xhtml-mobile10.dtd"/>
  <xsl:template match="/">
    <div class="FormWrapper">
      <div class="Panel">
        <div class="PanelHeader"><xsl:value-of select="root/mainText/@label"/></div>
        <div class="PanelText">
          <div class="validationMsg"></div>
              <div class="xStandardContainer">
                  <object type="application/x-xstandard" id="mainText" name="mainText" width="100%" height="200" onkeydown="DetectKeyDown(event)">
                    <param name="ToolbarWysiwyg" value="ordered-list,unordered-list,hyperlink,directory,spellchecker,," />
                    <param name="Value" value="{root/mainText}" />
                  </object>
	          </div>
        </div>
      </div>
      <div class="Panel">
        <div class="PanelHeader"><xsl:value-of select="root/questionText/@label"/></div>
        <div class="PanelText">
          <div class="validationMsg"></div>
              <div class="xStandardContainer">
                  <object type="application/x-xstandard" id="questionText" name="questionText" width="100%" height="200" onkeydown="DetectKeyDown(event)">
                    <param name="ToolbarWysiwyg" value="ordered-list,unordered-list,hyperlink,directory,spellchecker,," />
                    <param name="Value" value="{root/questionText}" />
                  </object>
	          </div>
        </div>
      </div>
      <div class="Panel">
        <div class="PanelHeader"><xsl:value-of select="root/promptAudio/@label"/></div>
        <div class="PanelText">
          <div class="validationMsg"></div>
          <div class="leftFloat">
            <input type="text" id="promptAudioSpan" value="{root/promptAudio/@name}" readonly="readonly"/>
          </div>
          <div class="leftFloat">
            <input type="button" onclick="openAssetPopupForStaticFields('promptAudio', 'audio');" class="searchBtn" title="Search Assets"/>
          </div>
          <div class="leftFloat">
              	<input type="button" onclick="deleteAsset(this,'#promptAudio');" class="deleteAssetBtn" title="Delete Asset"/>
          </div>
          <div class="leftFloat">
            <input type="text" style="visibility:hidden" name="promptAudio" id="promptAudio" value="{root/promptAudio}" readonly="readonly"/>
          </div>
          <div class="leftFloat">
            <input type="text" style="visibility:hidden;width:10px;" name="promptAudioVer" id="promptAudioVer" value="{root/promptAudio/@ver}"/>
          </div>
        </div>
      </div>
      <div class="Panel">
        <div class="PanelHeader"><xsl:value-of select="root/leftImage/@label"/></div>
        <div class="PanelText">
          <div class="validationMsg"></div>
          <div class="leftFloat">
            <input type="text" id="leftImageSpan"  value="{root/leftImage/@name}" readonly="readonly"/>
          </div>
          <div class="leftFloat">
            <input type="button" onclick="openAssetPopupForStaticFields('leftImage', 'image');" class="searchBtn" title="Search Assets"/>
          </div>
          <div class="leftFloat">
            <input type="text" style="visibility:hidden" name="leftImage" id="leftImage" value="{root/leftImage}" readonly="readonly" />
          </div>
          <div class="leftFloat">
            <input type="text" style="visibility:hidden;width:10px;" name="leftImageVer" id="leftImageVer" value="{root/leftImage/@ver}" readonly="readonly" />
          </div>
        </div>
      </div>
      <div class="Panel">
        <div class="PanelHeader"><xsl:value-of select="root/options/@label"/></div>
        <div class="PanelText">
          <div class="validationMsg"></div>
          <div class="tableHeaderBtn">
            <input type="button" onclick="deleteRow('gridTbl');" class="addRow" title="Delete Row"/>
            <input type="button" onclick="addRow('gridTbl');" class="deleteRow" title="Add Row"/>
          </div>
          <table border="0" id="gridTbl" class="gridTable" cellspacing="0">
            <tr>
              <td class="gridSNoHeaderCell"><input name="checkAll" id="checkAll" type="checkbox" onclick="checkAll(this)"/></td>
              <td class="gridHeaderCell"><xsl:value-of select="root/options/option/optionText/@label"/></td>
              <td class="gridHeaderCell"><xsl:value-of select="root/options/option/feedBack/@label"/></td>
              <td colspan="5" class="gridHeaderCell"><xsl:value-of select="root/options/option/optionAudio/@label"/></td>
              <td class="gridHeaderCell"><xsl:value-of select="root/options/option/optionStatus/@label"/></td>
            </tr>
            
              <tr style="display:none">
                <td class="gridSNoCell"><input name="option" id="option0" type="checkbox"/></td>
                <td class="gridCell"><div name="optionText" id="optionText0" onclick="textEditor($(this))" class="optionField"></div></td>
                <td class="gridCell"><div name="feedBack" id="feedBack0" onclick="textEditor($(this))" class="optionField"></div></td>
                <td class="gridCell"><input type="text" class="optionAudioSpan" name="optionAudioSpan" id="optionAudio0Span" value="" readonly="readonly"/></td>
                <td><input type="button" id="0" name="optionAudioName" onclick="openAssetPopup(this, 'audio');" class="searchBtn" title="Search Assets" /></td>
                <td><input type="button" onclick="deleteAsset(this,'.optionAudio');" class="deleteAssetBtn" title="Delete Selected Asset"/>
      	          </td>
                <td><input class="optionAudio" name="optionAudio" id="optionAudio0" type="hidden" value="" readonly="readonly" /></td>
                <td><input name="optionAudioVer" class="optionAudioVer" id="optionAudio0Ver" type="hidden" value="" readonly="readonly" /></td>
                <td class="gridCell"><select name="optionStatus" class="dropdownField" id="optionStatus0" onchange="FormModified()">
                    <!--<xsl:for-each select="root/options/option/optionStatus/list">-->
                        <option value="c">Correct</option>
                        <option value="ic">Incorrect</option>
                        <option value="pc">Partial Correct</option>
                    <!--</xsl:for-each>-->
                  </select></td>
              </tr>
            
            <xsl:for-each select="root/options/option">
              <tr>
                <td class="gridSNoCell"><input name="option" id="option{@id}" type="checkbox"/></td>
                <td class="gridCell"><div name="optionText" id="optionText{@id}" onclick="textEditor($(this))" class="optionField"><xsl:value-of select="optionText" disable-output-escaping="yes"/></div></td>
                <td class="gridCell"><div name="feedBack" id="feedBack{@id}" onclick="textEditor($(this))" class="optionField"><xsl:value-of select="feedBack" disable-output-escaping="yes"/></div></td>
                <td class="gridCell"><input type="text" class="optionAudioSpan" name="optionAudioSpan" id="optionAudio{@id}Span" value="{optionAudio/@name}" readonly="readonly"/></td>
                <td><input type="button" id="{@id}" name="optionAudioName" onclick="openAssetPopup(this, 'audio');" class="searchBtn" title="Search Assets" /></td>
                <td><input type="button" onclick="deleteAsset(this,'.optionAudio');" class="deleteAssetBtn" title="Delete Selected Asset"/></td>
                <td><input class="optionAudio" name="optionAudio" id="optionAudio{@id}" type="hidden" value="{optionAudio}" readonly="readonly" /></td>
                <td><input name="optionAudioVer" class="optionAudioVer" id="optionAudio{@id}Ver" type="hidden" value="{optionAudio/@ver}" readonly="readonly" /></td>
                <td class="gridCell"><select name="optionStatus" class="dropdownField" id="optionStatus{@id}" onchange="FormModified()">
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
                  </select></td>
              </tr>
            </xsl:for-each>
          </table>
        </div>
      </div>
    </div>
  </xsl:template>
</xsl:stylesheet>