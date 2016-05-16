package com.softech.ls360.lcms.contentbuilder.dataimport.test;

import com.softech.common.template.TemplateResolver;
import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import com.softech.ls360.lcms.contentbuilder.service.VU360UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.softech.ls360.lcms.contentbuilder.test.AbstractLcmsTest;
import java.util.HashMap;
import java.util.Map;

public class VelocityTemplateResolverTest extends AbstractLcmsTest {
    
     @Autowired
    TemplateResolver templateResolver;
    
    @Autowired
    private VU360UserService userService;

    
    
    @Test()
    public void parseString() {
        
        VU360UserDetail user = (VU360UserDetail) userService.loadUserByUsername("admin.manager@360training.com");
        Map model = new HashMap();
        String text = templateResolver.resolveTemplate(user, "here is user name = ${user.userDisplayName}", model);
        System.out.print(text);
    }
}
