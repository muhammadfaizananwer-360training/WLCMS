package com.softech.ls360.lcms.contentbuilder.utils;

public class AssetUtil {

public static String getFileSizeInKb(String fileSize){

		String sizeInKb ="";
		try{
			if(fileSize!=null && !fileSize.equals("")){
				if(StringUtil.isNumber(fileSize)){
					long size = Long.parseLong(fileSize)/1024;
					sizeInKb = size+"KB";

				}
			}
		}catch(Exception e){

		}

		return sizeInKb;
	}

}
