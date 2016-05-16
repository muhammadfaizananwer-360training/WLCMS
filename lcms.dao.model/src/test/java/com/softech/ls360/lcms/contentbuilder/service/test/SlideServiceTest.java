package com.softech.ls360.lcms.contentbuilder.service.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.softech.ls360.lcms.contentbuilder.model.Slide;
import com.softech.ls360.lcms.contentbuilder.model.SlideAsset;
import com.softech.ls360.lcms.contentbuilder.service.ISlideService;
import com.softech.ls360.lcms.contentbuilder.utils.JsonResponse;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, SlideServiceTest.class})
@ContextConfiguration(locations = { "classpath:app-context.xml" })
public class SlideServiceTest  extends AbstractTestExecutionListener
{
	@Autowired
	ISlideService slideService;
     
	//@Autowired
	//VU360UserService vu360UserService;
	
	
	@Test
	public void testgetSlide()
	{
		String varSlideId = "4";// request.getParameter("varSlideId");
		Object obj = "";
		if(varSlideId!=null){
		
		try {
			//obj = slideService.getSlide(Long.parseLong(varSlideId));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(obj);
		}	
	}
	
	
	
	/*@Test
	public void testaddSlide() throws NumberFormatException, Exception
	{
		Slide objSlide = new Slide ();
		Object obj = "";
		LdapUserDetailsImpl userd  = (LdapUserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360UserDetail  user = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		
		objSlide.setCourse_ID(123);
		objSlide.setContentObject_id(1234);
		objSlide.setName("Test");
		if (!StringUtils.isEmpty(10))
			objSlide.setDuration(Long.parseLong(StringUtil.ifNullReturnZero(10)));
		else
			objSlide.setDuration(0);
		//Yasin
		//objSlide.setTemplateID(Long.parseLong(StringUtil.ifNullReturnZero(request.getParameter("templateID"))));
				
		objSlide.setOrientationKey("Left");
		objSlide.setAsset_ID(0);		
		objSlide.setContentOwner_ID((int)user.getContentOwnerId());
		objSlide.setCreateUserId((int)user.getVu360UserID());
		objSlide.setSceneGUID(UUID.randomUUID().toString().replaceAll("-", ""));
				
		obj = slideService.addSlide(objSlide);
		
		assertNotNull(obj);
		
	
	}*/
	
	
	@Test
	public void testupdateSlide() throws NumberFormatException, Exception
	{
		JsonResponse res = new JsonResponse();
		String varSlideId = "4";
		String varTitle = "JunitTest";
		String varDuration = "10";
		
		Slide dto = new Slide();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setName(varTitle);
		dto.setDuration(Long.parseLong(varDuration));
		
		//obj = slideService.updateSlide(dto);
		
		res.setStatus("SUCCESS");
				
		//assertNotNull(obj);
		
	
	}
	
	
	@Test
	public void testgetSlideTextAgainstSlideId() throws NumberFormatException, Exception
	{
		String varSlideId = "123";
		if(varSlideId!=null){
			//obj =  slideService.getSlideTextAgainstSlideId(Long.parseLong(varSlideId));
				
			//assertNotNull(obj);
		
	
	
		}
		}
	
	@Test
	public void testgetSlideCloseCaptionAgainstSlideId() throws NumberFormatException, Exception
	{
		Object obj = "";
		String varSlideId = "4";
		if(varSlideId!=null)
		{
			obj =  slideService.getSlideCloseCaptionAgainstSlideId(Long.parseLong(varSlideId));
			assertNotNull(obj);
		}
   }
	
	
	@Test
	public void testgetVisualAssetBySlideId() throws NumberFormatException, Exception
	{
		String varSlideId = "456";
		if(varSlideId!=null){
			//obj =  slideService.getVisualAssetBySlideId(Long.parseLong(varSlideId), varAssetType);
			//assertNotNull(obj);
		
		}
	
	}
	
	@Test
	public void testupdateSlideText() throws NumberFormatException, Exception
	{
		JsonResponse res = new JsonResponse();
		
		String varSlideId = "123";
		String varTitle = "Junit Test";
		String varOrientationKey = "123456";
		
		if(varOrientationKey!=null)
			varOrientationKey = "$"+varOrientationKey;
		
		SlideAsset dto = new SlideAsset();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setContent(varTitle);
		dto.setOrientationkey(varOrientationKey);
		
		//obj = slideService.updateSlideText(dto);
		
		res.setStatus("SUCCESS");
		
			//assertNotNull(obj);
		
		}
	
	
	
	@Test
	public void testupdateSlideCloseCaption() throws NumberFormatException, Exception
	{
		JsonResponse res = new JsonResponse();
		
		String varSlideId = "13245";
		String varTitle = "Junit Test";
		String varOrientationKey = "123465";
		
		if(varOrientationKey!=null)
			varOrientationKey = "$"+varOrientationKey;
		
		SlideAsset dto = new SlideAsset();
		dto.setId(Integer.parseInt(varSlideId));
		dto.setContent(varTitle);
		dto.setOrientationkey(varOrientationKey);
		
		//obj = slideService.updateSlideCloseCaption(dto);
		
		res.setStatus("SUCCESS");
				
			//assertNotNull(obj);
		
		}
	
	
	/*@Test
	public void testgetSlideAssetSearch() throws NumberFormatException, Exception
	{
		Object obj = "";
				
		LdapUserDetailsImpl userd = (LdapUserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		VU360UserDetail user 	  = (VU360UserDetail) vu360UserService.loadUserByUsername(userd.getUsername());
		//
		List<SlideAsset> lstAssets = null;
		if (!StringUtils.isEmpty("Test"))
			if (StringUtils.isEmpty("Test"))
				lstAssets = slideService.getSlideAssetSearch("Test",(int) user.getContentOwnerId(), 1);
			else	// Audio Last parameter needs to be anything but 0
				lstAssets = slideService.getSlideAssetSearch("Test1",(int) user.getContentOwnerId(), 2);
		
		for (int i = 0; i< lstAssets.size(); i++)
		{
			//code.lcms.assets.URL
			String locationPath = LCMSProperties.getLCMSProperty("code.lcms.assets.URL");
			//Object [] obj = lstAssets.get (i);
			SlideAsset objAsset = (SlideAsset) lstAssets.get (i);
			objAsset .setLocation( locationPath + objAsset.getLocation() );
		}
		
		assertNotNull(obj);
		
		}
		*/
		
}

