package com.softech.ls360.lcms.contentbuilder.web.controller;

import com.softech.common.mail.MailAsyncManager;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.softech.ls360.lcms.contentbuilder.model.VU360User;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.GUIDGeneratorUtil;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;


/**
 * 
 * @author haider.ali
 *
 */

@Controller
public class ForgetCredentialController {

	private static String byUserName = "1";
	private static String byPassword = "2";
	private static Logger logger = LoggerFactory.getLogger(ForgetCredentialController.class);
	
	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	MailAsyncManager mailAsyncManager;
	
	@Resource(name="brandProperties")
	private Properties brandProperties;

	
	@RequestMapping(value = "credentialRetrievalMethod", method = RequestMethod.GET)
	public ModelAndView CredentialRetrievalMethod(ModelMap model) {
		return new ModelAndView("credentialRetrievalMethod", model);
	}

	
	@RequestMapping(value = "credentialRetrievalBy", method = RequestMethod.POST)
	public ModelAndView credentialRetrievalBy(HttpServletRequest request) {
		
		if(byPassword.equals(request.getParameter("methodType"))){
			return new ModelAndView("credentialRetrievalByPassword");
		}
		if(byUserName.equals(request.getParameter("methodType"))){
			return new ModelAndView("credentialRetrievalByUserName");
		}
		else{
			logger.error("No Credential Retrival Method defined.");
		}
		return null;
	}
	
	@RequestMapping(value = "credentialRetrievalByPassword", method = RequestMethod.POST)
	public ModelAndView passwordMethod(HttpServletRequest request) {
		
		String UsernameOrEmai = request.getParameter("radioUserName");
		
		if(!StringUtils.isEmpty(UsernameOrEmai) && UsernameOrEmai.equals("1") && (!StringUtils.isEmpty(request.getParameter("userName"))) ) 
		{

			String userName = StringUtils.trim(request.getParameter("userName"));
			VU360User vu360User = (VU360User) vu360UserService.getVU360User(userName);

			if(vu360User!=null){
				
				String host = LCMSProperties.getHost();
				String sendTo=vu360User.getEmailAddress();
				StringBuffer msgBody = new StringBuffer();
				msgBody.append(brandProperties.getProperty("credentialReterieval.mailbody.password.text"));
				
				String password = GUIDGeneratorUtil.generatePassword();
				
				String mbody  = StringUtils.replace(msgBody.toString(), "&lt;userName&gt;", vu360User.getUsername());
				mbody = StringUtils.replace(mbody, "&lt;passwordlist&gt;", "Password: " + password);
				mbody = StringUtils.replace(mbody, "&lt;loginurl&gt;", host + "/login.do");
				
				//update user in DB and AD
				VU360User vu360User1 = (VU360User) vu360UserService.changeVU360UserPassword(vu360User, password);

				//send mail
				sendMessage("Forget your password?", sendTo, mbody);
				
				return new ModelAndView("credentialRetrievalConfirmation");
			}
		}
		
		if(!StringUtils.isEmpty(UsernameOrEmai) && UsernameOrEmai.equals("2") && (!StringUtils.isEmpty(request.getParameter("userEmailAddress")))){
			
		
			String emails = StringUtils.trim(request.getParameter("userEmailAddress"));
			String fname= StringUtils.trim(request.getParameter("userFirstName"));
			String lname= StringUtils.trim(request.getParameter("userLastName"));
			
			List<VU360User> userList = vu360UserService.getVU360User(emails,fname,lname);
			
			if(userList!=null && userList.size()>0){
				StringBuffer usname = new StringBuffer();

				for (Iterator iterator = userList.iterator(); iterator.hasNext();) {
					VU360User vu360User2 = (VU360User) iterator.next();
					String password = GUIDGeneratorUtil.generatePassword();
					usname.append("Username: "+vu360User2.getUsername() + "<br>");
					usname.append("Password: "+password + "<br><br>");
					vu360User2.setPassword(password);
					
					//update AD user
					VU360User vu360User1 = (VU360User) vu360UserService.changeVU360UserPassword(vu360User2, password);
					

				}	
				String host = LCMSProperties.getHost();
				String sendTo = userList.get(0).getEmailAddress();
				StringBuffer msgBody = new StringBuffer();
				msgBody.append(brandProperties.getProperty("credentialReterieval.mailbody.password.text"));
				
				//String password = GUIDGeneratorUtil.generatePassword();
				
				String mbody  = StringUtils.replace(msgBody.toString(), "&lt;userName&gt;", userList.get(0).getFirstName());
				mbody = StringUtils.replace(mbody, "&lt;passwordlist&gt;", usname.toString());
				mbody = StringUtils.replace(mbody, "&lt;loginurl&gt;",  host + "/login.do");
	
				//send mail
				sendMessage("Forget your password?", sendTo, mbody );
				
				return new ModelAndView("credentialRetrievalConfirmation");
			}
		
			
		}else{
			logger.error("No Option selected for password retrival.");
		}
		
		
		return new ModelAndView("credentialRetrievalFailure");
		
	}


	@RequestMapping(value = "credentialRetrievalByUserName", method = RequestMethod.POST)
	
	public ModelAndView usernameMethod( HttpServletRequest request) {
		
		String emails = StringUtils.trim(request.getParameter("userEmailAddress"));
		String fname= StringUtils.trim(request.getParameter("userFirstName"));
		String lname= StringUtils.trim(request.getParameter("userLastName"));

		List<VU360User> userList = vu360UserService.getVU360User(emails,fname,lname);
		
		if(userList!=null && userList.size()>0){
			StringBuffer usname = new StringBuffer();

			for (Iterator iterator = userList.iterator(); iterator.hasNext();) {

				VU360User vu360User2 = (VU360User) iterator.next();
				usname.append("Username: "+vu360User2.getUsername() + "<br>");

				//update AD user
				//VU360User vu360User1 = (VU360User) vu360UserService.changeVU360UserPassword(vu360User2, password);
			}	
			String host = LCMSProperties.getHost();
			String sendTo = userList.get(0).getEmailAddress();
			StringBuffer msgBody = new StringBuffer();
			msgBody.append(brandProperties.getProperty("credentialReterieval.mailbody.username.text"));
			
			
			String mbody  = StringUtils.replace(msgBody.toString(), "&lt;userName&gt;", userList.get(0).getUsername());
			mbody = StringUtils.replace(mbody, "&lt;usernamelist&gt;", usname.toString());
			mbody = StringUtils.replace(mbody, "&lt;loginurl&gt;",  host + "/login.do");

			//send mail
			sendMessage("Forget your username?", sendTo, mbody );

		}else{
			
			return new ModelAndView("credentialRetrievalFailure");
		}
		
		return new ModelAndView("credentialRetrievalConfirmation");
		
	}

	
	private void sendMessage(String subject, String sendto, String body){

		mailAsyncManager.send(new String[] { (String) sendto }, null, "support@360training.com",
				"360training Member Service", subject,  "<br>"+ body.toString()+ "</div>");
	}	
	
}
