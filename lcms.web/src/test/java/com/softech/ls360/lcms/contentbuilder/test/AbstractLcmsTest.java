package com.softech.ls360.lcms.contentbuilder.test;
import org.hibernate.AssertionFailure;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.StringUtils;

import com.softech.ls360.lcms.contentbuilder.model.VU360User;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml","file:src/main/webapp/WEB-INF/spring-security.xml","file:src/main/webapp/WEB-INF/spring-ws.xml" })
@ActiveProfiles(profiles = "test")
public abstract class AbstractLcmsTest extends AbstractTestExecutionListener {

	@Autowired
	protected VU360UserService userService;
	
	@Value("${test.username:admin.manager@360training.com}")
	private String userName;
	
	private VU360User user;
	protected VU360User getUser() {
		if(user == null) {
			user = userService.getVU360User(userName);
			assertNotNull(userName + " user is not defined", user);
		}

		return user;
	}
	
	protected void assertNotEmpty(String msg, String str) {
		if(StringUtils.isEmpty(str)) {
			throw new AssertionFailure(msg);
		}
	}
}
				