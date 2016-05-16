package com.softech.ls360.lcms.contentbuilder.web.controller;

 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

 

import com.aspose.slides.ISlide;
import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;

public class MainConverter {

	public MainConverter()
	{}

	
	public  static List<String> convert(String file, String ext)
	{
		if(file == null){
			System.out.println( "no file" );
			return null;
		}

		List<String> fileList = new ArrayList<String>();
		
		float maxHeight = 450;
		
		try{
			checkLicence();	
			float scale = 1;
			Presentation pres = new Presentation(file);
			/*
			float slideHeight = (float)pres.getSlideSize().getSize().getHeight();
			if (slideHeight > maxHeight){
				scale = 1.0f - ((slideHeight- maxHeight)/slideHeight);  
			}
			*/
			int count = pres.getSlides().size();
					
			ISlide sld ;
			BufferedImage image;
			for(int i=0; i <count; i++){
				sld = pres.getSlides().get_Item(i);

				//Create a full scale image
				 image = sld.getThumbnail(scale,scale);//scale here

				//Save the image to disk in JPEG format
				String fname = file.replaceAll("." +ext, "-" + (i+1) + ".jpg");
				ImageIO.write(image,"jpeg",new File(fname));	
				fileList.add(fname);				
			}
			
			 
		}
		catch(Exception ee){
			ee.printStackTrace();
		}
		
		/*
		if (ext.equals("ppt")){
			FileInputStream is = null;
			try{
				is = new FileInputStream(file);

				SlideShow ppt = new SlideShow(is);
				Dimension pgsize = ppt.getPageSize();
				int width = (int)(pgsize.width*scale);
				int height = (int)(pgsize.height*scale);

				Slide[] slide = ppt.getSlides();
				for (int i = 0; i < slide.length; i++) {
					if (slidenum != -1 && slidenum != (i+1)) continue;

					String title = slide[i].getTitle();
					System.out.println("Rendering slide "+slide[i].getSlideNumber() + (title == null ? "" : ": " + title));

					BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
					Graphics2D graphics = img.createGraphics();
					graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

					graphics.setPaint(Color.white);
					graphics.fill(new Rectangle2D.Float(0, 0, width, height));

					graphics.scale((double)width/pgsize.width, (double)height/pgsize.height);

					slide[i].draw(graphics);

					String fname = file.replaceAll("\\.ppt", "-" + (i+1) + ".png");
					FileOutputStream out = new FileOutputStream(fname);
					ImageIO.write(img, "png", out);
					out.close();
					fileList.add(fname);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}	     

		}

		else if (ext.equals("pptx")){

			//String file = "G:/eclipse_workspace/convert/SQL Server 2012.pptx";
			System.out.println("Processing " + file);
			XMLSlideShow ppt=null;

			try {
				ppt = new XMLSlideShow(OPCPackage.open(file));
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Dimension pgsize = ppt.getPageSize();
			int width = (int) (pgsize.width * scale);
			int height = (int) (pgsize.height * scale);

			XSLFSlide[] slide = ppt.getSlides();
			for (int i = 0; i < slide.length; i++) {
				if (slidenum != -1 && slidenum != (i + 1)) continue;

				String title = slide[i].getTitle();
				System.out.println("Rendering slide " + (i + 1) + (title == null ? "" : ": " + title));

				BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = img.createGraphics();

				// default rendering options
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

				graphics.setColor(Color.white);
				graphics.clearRect(0, 0, width, height);

				graphics.scale(scale, scale);

				// draw stuff
				slide[i].draw(graphics);

				// save the result
				int sep = file.lastIndexOf(".");
				String fname = file.substring(0, sep == -1 ? file.length() : sep) + "-" + (i + 1) +".png";
				FileOutputStream out = null;

				try {
					out = new FileOutputStream(fname);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					ImageIO.write(img, "png", out);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
					fileList.add(fname);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Done with slides");
			}

		}
		*/
		return fileList;		
	}
	
	private static void checkLicence() throws IOException{
		String licPath = System.getProperty( "catalina.base" ) + "/webapps/lcms/WEB-INF/classes/Aspose.Slides.lic";
		FileInputStream fstream = new FileInputStream(licPath);
	 
		try
		 {
		   //Create a stream object containing the license file
		   //Instantiate the License class
		   License license=new License();
		   //Set the license through the stream object
		   license.setLicense(fstream);
		 }
		 catch(Exception ex)
		 {
		   //Printing the exception, if it occurs
		   System.out.println(ex.toString());
		 }
		 finally
		 {
		   //Closing the stream finally
		   if(fstream != null)
		        fstream.close();
		 }
		
	}
}
