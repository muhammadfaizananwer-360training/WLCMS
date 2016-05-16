package com.softech.ls360.lcms.contentbuilder.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import antlr.InputBuffer;

public class IOConvertor {
	private static Logger logger = LoggerFactory.getLogger(IOConvertor.class);
	public static byte[] convertBufferedImageToDataBytes(BufferedImage img, String imageFormat) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte=null;
		try {
			ImageIO.write(img, imageFormat, baos );
			baos.flush();
			imageInByte = baos.toByteArray();
		}finally{
			baos.close();
		}
		return imageInByte;
	}
	
	
	public static byte[] convertStreamToDataBytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] dataBytes=null;
		try {
			IOUtils.copy(is, baos);
			baos.flush();
			dataBytes = baos.toByteArray();
		}finally{
			baos.close();
		}
		return dataBytes;
	}
}
