package com.softech.common.template;

import com.softech.ls360.lcms.contentbuilder.model.VU360UserDetail;
import java.util.Map;

/**
 *
 * @author abdul.wahid
 */
public interface TemplateResolver {
    String resolveTemplate(VU360UserDetail user, String templatePath,Map templateParams);
}
