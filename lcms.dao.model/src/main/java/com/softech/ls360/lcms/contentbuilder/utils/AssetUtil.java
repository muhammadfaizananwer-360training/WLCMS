package com.softech.ls360.lcms.contentbuilder.utils;

public class AssetUtil {
	
public static String getFileSizeInKb(String fileSize){
		
		String sizeInKb ="";
		try{
			if(fileSize!=null && !fileSize.equals("")){
				if(StringUtil.isNumber(fileSize.toString())){
					Long size = (Long.valueOf(fileSize.toString())/1024);
					sizeInKb = size.toString()+"KB";
					 
				}
			}
		}catch(Exception e){
			
		}
		
		return sizeInKb;
	}

}
