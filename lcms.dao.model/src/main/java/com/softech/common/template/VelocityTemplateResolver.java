package com.softech.common.template;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolContext;
import org.apache.velocity.tools.ToolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author abdul.wahid
 */
@Component
public class VelocityTemplateResolver implements TemplateResolver {

    @Resource(name = "velocityEngine")
    private VelocityEngine velocityEngine;

    @Autowired
    MessageSource messageSource;

    @Override
    public String resolveTemplate(VU360UserDetail user, String templatePath, Map templateParams) {

        ToolManager toolManager = new ToolManager();
        toolManager.configure("email-templates/velocity/common/tools.xml");
        ToolContext toolContext = toolManager.createContext();
        VelocityContext velocityContext = new VelocityContext(templateParams, toolContext);
        
        templateParams.put("messageSource", messageSource);
        templateParams.put("user", user);
        templateParams.put("locale", Locale.US);
        templateParams.put("ctx", velocityContext);
        templateParams.put("parser", this);
        
        
        StringWriter resultWriter = new StringWriter();
        velocityEngine.mergeTemplate(templatePath, "UTF-8", velocityContext, resultWriter);
        String text = resultWriter.toString();
        return text;

    }
    
    public String evaluateText(VelocityContext velocityContext,String text) {
        StringWriter resultWriter = new StringWriter();
        velocityEngine.evaluate(velocityContext, resultWriter, text, text);
        String result = resultWriter.toString();
        return result;
    }

}
