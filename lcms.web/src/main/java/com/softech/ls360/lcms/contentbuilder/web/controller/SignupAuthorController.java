package com.softech.ls360.lcms.contentbuilder.web.controller;

import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.marketo.mktows.ArrayOfAttribute;
import com.marketo.mktows.Attribute;
import com.marketo.mktows.AuthenticationHeader;
import com.marketo.mktows.LeadRecord;
import com.marketo.mktows.MktMktowsApiService;
import com.marketo.mktows.MktowsContextHeader;
import com.marketo.mktows.MktowsPort;
import com.marketo.mktows.ObjectFactory;
import com.marketo.mktows.ParamsSyncLead;
import com.marketo.mktows.SuccessSyncLead;
import com.softech.common.mail.MailAsyncManager;
import com.softech.ls360.lcms.contentbuilder.model.SignUpAuthor;
import com.softech.ls360.lcms.contentbuilder.service.ISignUpAuthorService;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import com.softech.ls360.lcms.contentbuilder.utils.LCMSProperties;
import com.softech.ls360.lcms.contentbuilder.utils.WlcmsConstants;
import com.softech.vu360.lms.selfservice.webservices.orderservice.Address;
import com.softech.vu360.lms.selfservice.webservices.orderservice.AuthenticationCredential;
import com.softech.vu360.lms.selfservice.webservices.orderservice.Contact;
import com.softech.vu360.lms.selfservice.webservices.orderservice.Customer;
import com.softech.vu360.lms.selfservice.webservices.orderservice.Order;
import com.softech.vu360.lms.selfservice.webservices.orderservice.OrderCreatedRequest;
import com.softech.vu360.lms.selfservice.webservices.orderservice.OrderCreatedResponse;
import com.softech.vu360.lms.selfservice.webservices.orderservice.OrderLineItem;
import com.softech.vu360.lms.selfservice.webservices.orderservice.Product;

import org.springframework.web.servlet.ModelAndView;


/**
 * 
 * @author haider.ali
 * 
 */

@Controller
public class SignupAuthorController {

	private static Logger logger = LoggerFactory.getLogger(SignupAuthorController.class);
	public static final String EULA_SIGNED = "1";
	public static final String DONOT_USE_MY_EMAIL_AS_LOGIN_ID = "0";
	public static final String USE_MY_EMAIL_AS_LOGIN_ID = "1";

	@Autowired
	ISignUpAuthorService signUpAuthorService;

	@Autowired
	VU360UserService vu360UserService;

	@Autowired
	MailAsyncManager mailAsyncManager;
	
	 @Resource(name="webServiceTemplate")
	 private WebServiceTemplate webServiceTemplate;
	
	 @Resource(name="brandProperties")
	 private Properties brandProperties;
	 
	 
	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "signUpAuthor", method = RequestMethod.GET)
	public ModelAndView initSignupfrom(ModelMap model) {
		
		model.addAttribute("eulaText", brandProperties.get("wlcms.signUp.field.eulaText"));
		return new ModelAndView("signupAuthor", model);
	}
	
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "signUpAuthorSave", method = { RequestMethod.POST})
	public ModelAndView signUpAuthorSave(HttpServletRequest request) {

		logger.debug("SignupAuthorController::save - Start");
		
		String firstname = request.getParameter("firstName") ;
		String lastName = request.getParameter("lastName") ;
		String email	 = 	request.getParameter("email");		
		String useMyEmailTF =  request.getParameter("useMyEmailTF") ;
		String useOtherEmail = request.getParameter("useOtherEmail") ;
		String passwrod = request.getParameter("passwrod") ;		
		String username = "";
		String urlSource = request.getParameter("src");


		SignUpAuthor signUpAuthor = new SignUpAuthor();
		String sErrorMsg = null;

		try {
			
			signUpAuthor.setFirstName(firstname);
			signUpAuthor.setLastName(lastName);
			signUpAuthor.setEmail(email);
			signUpAuthor.setUseMyemailTF(Integer.parseInt(useMyEmailTF));
			
			username = StringUtils.isEmpty(useOtherEmail) ? email : useOtherEmail;
						
			signUpAuthor.setLoginName(username);
			
			signUpAuthor.setPasswrod(passwrod);
			
			signUpAuthor.setActivationCode(RandomStringUtils.random(32, 0, 20, true, true, "bj81G5RDED3DC6142kasok".toCharArray()));
			signUpAuthor.setUrlSource(urlSource==null?"":urlSource);
			signUpAuthorService.save(signUpAuthor);
			
			activateUser(signUpAuthor.getActivationCode());

			//Send a composed mail, should be in property file.
			logger.info("Send mail to User .....\n");
			String host = LCMSProperties.getHost();
			StringBuffer msgBody = new StringBuffer();
			msgBody.append("<b>Dear "+ signUpAuthor.getFirstName() +",</b>");
			msgBody.append("<br><br>Welcome to the 360Authoring family. <br><br>As a 360Author, your topics of interest are endless. Take your knowledge and transform it into your very own course.");
			msgBody.append("<br><br><font color=\"#cccccc\">-------------------------------------------------------------------------------------------------------------------------------------------------------</font>");
			msgBody.append("<br>Login Information:</b>");
			msgBody.append("<br><br>Start creating great elearning courses with the login information below:");
			msgBody.append("<br><br><b>Username:</b> ").append( signUpAuthor.getLoginName() );
			msgBody.append("<br><b>Password:</b> ").append( signUpAuthor.getPasswrod() );
			msgBody.append("<br><a href=\"" + host + "\">Click here to login and get started</a>");
			msgBody.append("<br><br>The 360training Authoring team.");
			
			mailAsyncManager.send(new String[] { signUpAuthor.getEmail() }, null, "support@360training.com",
					"360training Member Service",  "Your 360Author account",  "<br>"+ msgBody.toString()+ "</div>");
			
			ModelAndView view = new ModelAndView("postSignupAuthorForLogin");
			view.addObject("uname", username);
			view.addObject("upd", passwrod);
			String CASServerBaseUrl = LCMSProperties.getLCMSProperty("lcms.cas.BaseUrl");
			String wlcmsServerBaseUrl = LCMSProperties.getLCMSProperty("lcms.environment.Host");
			view.addObject("CASServerBaseUrl", CASServerBaseUrl);
			view.addObject("wlcmsServerBaseUrl", wlcmsServerBaseUrl);

			return view;

		} catch (Exception ex) {
			logger.debug(ex.toString());
			ex.printStackTrace();
		}
		return new ModelAndView(sErrorMsg);
	}
	

	@RequestMapping(value = "signUpAuthorExists", method = { RequestMethod.POST})
	public @ResponseBody String signUpAuthorExists(HttpServletRequest request) {
		
		boolean isExist=false;
		String userName = request.getParameter("email");
		String useMyEmailTF = request.getParameter("useMyEmailTF");
		
		if(!StringUtils.isEmpty(userName) && !StringUtils.containsWhitespace(userName))
			
			//do not check is radio having 0 value; 
			if(!StringUtils.isEmpty(useMyEmailTF) && useMyEmailTF.equals(DONOT_USE_MY_EMAIL_AS_LOGIN_ID))
				return "true";
					
			
			isExist = vu360UserService.VU360UserAlreadyExist(userName);

		if(!isExist)
			return new String("true");
		return new String("false");
			
	}

	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "signUpAuthorActivate", method = { RequestMethod.GET})
	public ModelAndView signUpAuthorActivate(HttpServletRequest request) {
		
		String activationCode = request.getParameter("e");
		if(activateUser(activationCode)){
			
			String redirectLink = "redirect:/login?"+ WlcmsConstants.VALIDATION_LINK_SUCCESS_MESSAGE_KEY + "=" + WlcmsConstants. VALIDATION_LINK_SUCCESS_CODE;
			return new ModelAndView(redirectLink);
		}
		return new ModelAndView("signupAuthorFailure");	
		
	}
	
	
	private String CallMarketoServiceForInsertorUpdateUser(String firstName,String lastName,String email) {
        System.out.println("Executing syncLead");
        StringWriter sw =new StringWriter();
        SuccessSyncLead result = null;
        try {
            URL marketoSoapEndPoint = new URL(LCMSProperties.getLCMSProperty("marketo.wsdl.url"));
            String marketoUserId = LCMSProperties.getLCMSProperty("marketo.user");
            String marketoSecretKey = LCMSProperties.getLCMSProperty("marketo.secretkey");
             
            QName serviceName = new QName(LCMSProperties.getLCMSProperty("marketo.qname.url"), LCMSProperties.getLCMSProperty("marketo.servicename"));
            MktMktowsApiService service = new MktMktowsApiService(marketoSoapEndPoint, serviceName);
            MktowsPort port = service.getMktowsApiSoapPort();
             
            // Create Signature
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String text = df.format(new Date());
            String requestTimestamp = text.substring(0, 22) + ":" + text.substring(22);           
            String encryptString = requestTimestamp + marketoUserId ;
             
            SecretKeySpec secretKey = new SecretKeySpec(marketoSecretKey.getBytes(), LCMSProperties.getLCMSProperty("marketo.secretkey.method"));
            Mac mac = Mac.getInstance(LCMSProperties.getLCMSProperty("marketo.secretkey.method"));
            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(encryptString.getBytes());
            char[] hexChars = Hex.encodeHex(rawHmac);
            String signature = new String(hexChars); 
             
            // Set Authentication Header
            AuthenticationHeader header = new AuthenticationHeader();
            header.setMktowsUserId(marketoUserId);
            header.setRequestTimestamp(requestTimestamp);
            header.setRequestSignature(signature);
             
            // Create Request
            ParamsSyncLead request = new ParamsSyncLead();
            LeadRecord key = new LeadRecord();
             
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBElement<String> emailAddress = objectFactory.createLeadRecordEmail(email);
            key.setEmail(emailAddress);
            request.setLeadRecord(key);
             
            Attribute attr1 = new Attribute();
            attr1.setAttrName("FirstName");
            attr1.setAttrValue(firstName);
             
            Attribute attr2 = new Attribute();
            attr2.setAttrName("LastName");
            attr2.setAttrValue(lastName);




			ArrayOfAttribute aoa = new ArrayOfAttribute();
            aoa.getAttributes().add(attr1);
            aoa.getAttributes().add(attr2);


            QName qname = new QName(LCMSProperties.getLCMSProperty("marketo.qname.url"),LCMSProperties.getLCMSProperty("marketo.attributelistname") );
            JAXBElement<ArrayOfAttribute> attrList = new JAXBElement(qname, ArrayOfAttribute.class, aoa);
            key.setLeadAttributeList(attrList);
             
            MktowsContextHeader headerContext = new MktowsContextHeader();
           //headerContext.setTargetWorkspace("default");
             
            result = port.syncLead(request, header, headerContext);
 
            JAXBContext context = JAXBContext.newInstance(SuccessSyncLead.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            Result outPutXml = null;
            m.marshal(result, sw);
            
            
            System.out.print("**********************Done!");
            sw.toString();
             
        }
        catch(javax.xml.ws.soap.SOAPFaultException e) {
        	System.out.print("**********************SOAP Exception");
        	System.out.print(e.getLocalizedMessage());
            
        }catch(NullPointerException ex){
        	System.out.print("**********************NULL Exception");
        	ex.getCause();
        }catch(Exception ex){
        	System.out.print("**********************Exception");
        	ex.getCause();
        }finally{
        	return sw.toString();
        }
    }
	
	private boolean activateUser(String activationCode){
		
		OrderCreatedResponse response=null;
		boolean validatUser = false;
		//String activationCode = request.getParameter("e");
		SignUpAuthor signUpAuthor = new SignUpAuthor();

		if(!StringUtils.isEmpty(activationCode)){
			
			signUpAuthor.setActivationCode(activationCode);
			
			//get user against activation code.
			signUpAuthor = signUpAuthorService.getSignUpUserByActivationCode(signUpAuthor);

			if (signUpAuthor.getId()!=null) {

				String userName=null;
				Integer tf =  signUpAuthor.getUseMyemailTF();
				if(tf != null && tf.intValue() == Integer.parseInt(USE_MY_EMAIL_AS_LOGIN_ID)){
					userName = signUpAuthor.getEmail();
				}
				if(tf != null && tf.intValue() == Integer.parseInt(DONOT_USE_MY_EMAIL_AS_LOGIN_ID)){
					userName = signUpAuthor.getLoginName();
				}
				
				if( !StringUtils.isEmpty(userName) 
							//&& userName.length() >= 8 //This check should not be here.  
							&& signUpAuthor.getPasswrod().length() >= 8
							&& (!StringUtils.containsWhitespace(userName))  ){
					
					validatUser = true;
						
				}
						
				
				//check is user exists b4.
				if(validatUser){
						
					Boolean isExist = vu360UserService.VU360UserAlreadyExist(userName);

						if(!isExist){
							//creating soap request
							StringBuilder companyName = new StringBuilder(signUpAuthor.getLastName()).append(",").append(signUpAuthor.getFirstName()).append("-");
							long n3 = Math.round(Math.random()*10000);
							companyName = companyName.append(String.format("%04d", n3));
							
							OrderCreatedRequest req = new OrderCreatedRequest();
							req.setTransactionGUID(RandomStringUtils.random(32, 0, 20, true, true, "bj81G5RDED3DC6142kasok".toCharArray()));
							req.setEventDate(getXmlDate());
							Order order = new Order();
							order.setDistributorId("21701");
							order.setOrderId("589034");
							order.setSystem("VU360-LCMS");
							order.setOrderDate(getXmlDate());
							Customer cust = new Customer();
							cust.setCompanyName(companyName.toString());
							cust.setCustomerId("-1");
							cust.setCustomerName(companyName.toString());
							Contact contact = new Contact();
							Address billingAddress = new Address();
							billingAddress.setAddressLine1("");
							billingAddress.setAddressLine2("");
							billingAddress.setAddressLine3("");
							billingAddress.setCity("");
							billingAddress.setState("");
							billingAddress.setZipCode("");
							billingAddress.setCountry("");
							Address shipingAddress = new Address();
							shipingAddress.setAddressLine1("");
							shipingAddress.setAddressLine2("");
							shipingAddress.setAddressLine3("");
							shipingAddress.setCity("");
							shipingAddress.setState("");
							shipingAddress.setZipCode("");
							shipingAddress.setCountry("");
							Address primyAddrss = new Address();
							primyAddrss.setAddressLine1("");
							primyAddrss.setAddressLine2("");
							primyAddrss.setAddressLine3("");
							primyAddrss.setCity("");
							primyAddrss.setState("");
							primyAddrss.setZipCode("");
							primyAddrss.setCountry("");
							contact.setShippingAddress(shipingAddress);
							contact.setBillingAddress(primyAddrss);
							contact.setFirstName(signUpAuthor.getFirstName());
							contact.setLastName(signUpAuthor.getLastName());
							contact.setEmailAddress(signUpAuthor.getEmail());
							contact.setPrimaryPhone("");
							AuthenticationCredential authCredential = new AuthenticationCredential();
							authCredential.setUsername(userName);
							authCredential.setPassword(signUpAuthor.getPasswrod());
							contact.setAuthenticationCredential(authCredential);
							cust.setPrimaryContact(contact);
							cust.setPrimaryAddress(billingAddress);
							OrderLineItem orderLineItem = new OrderLineItem();
							orderLineItem.setGroupguid("-1");
							orderLineItem.setGuid("002");
							orderLineItem.setQuantity(BigInteger.ONE);
							orderLineItem.setLineitemguid("-1");
							orderLineItem.setTermOfService(BigInteger.ZERO);
							Product product = new Product();
							product.setCode("002");
							product.setType("CORE");
							product.setCategory("LMS Learning Management Software and Upgrades");
							product.setName("Author LMS Software Learning Management Core");
							product.setDescription("description");
							product.setOrganizationId("Bugfront");
							req.getProducts().add(product);
							order.setCustomer(cust);
							order.getLineItem().add(orderLineItem);
							req.setOrder(order);
							
							//send request for author.
							response = (OrderCreatedResponse) 
									webServiceTemplate.marshalSendAndReceive(req);
							
							vu360UserService.setAuthorBitInActiveDirectory(userName);
							
						}
						else{
							logger.debug(" **************** User Already exists. **************** " +userName);
						}
				}else{
					logger.debug(" **************** Unable to validate user (credential, length, spaces) **************** ");
				}
			}
			else{
				logger.debug(" **************** Unable to Find User against activation code **************** " +activationCode);
			}
		}
		
		
		if(response!=null){
			
			logger.debug(".................... Auther Created successfully ....................");
			logger.debug("TransactionResult:" + response.getTransactionResult());
			logger.debug("CustomerGUID: " + response.getCustomerGUID());
			logger.debug("TransactionGUID:" + response.getTransactionGUID());
			
			if("SUCCESS".equals(response.getTransactionResult())){
				String result = CallMarketoServiceForInsertorUpdateUser(signUpAuthor.getFirstName(),signUpAuthor.getLastName(),signUpAuthor.getEmail());
				signUpAuthorService.activateAuthor(signUpAuthor.getId(),result);
				signUpAuthorService.insertDefaultyRoyaltySettings(signUpAuthor);
				return true;
			}
		
		}
		return false;
	}
	
	/**
	 * get xmlCurrentDate()
	 * @return
	 */
	private static XMLGregorianCalendar getXmlDate(){
		
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Timestamp(System.currentTimeMillis()));
		XMLGregorianCalendar currtDate=null;;
		try {
			currtDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}

		return currtDate;

	}
}

