package com.softech.web.configuratin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softech.ls360.lcms.contentbuilder.utils.Custom360DateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abdul.wahid on 5/23/2016.
 */
@Configuration
public class MessageConvertorConfiguration  {

    private MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        Custom360DateFormat dateFormat = new Custom360DateFormat();
        dateFormat.setDateFormat(new SimpleDateFormat("MM/dd/yyyy"));
        dateFormat.setDateTimeFormat(new SimpleDateFormat("MM/dd/yyyy hh:mm a"));

        objectMapper.setDateFormat(dateFormat);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    @Autowired
    public void updateJacksonConvertor(RequestMappingHandlerAdapter handlerAdapter) {

        //remove default jakson convertor
        Iterator<HttpMessageConverter<?>> convertorsIterator = handlerAdapter.getMessageConverters().iterator();
        while (convertorsIterator.hasNext()) {
            HttpMessageConverter converter = convertorsIterator.next();
            if(converter instanceof AbstractJackson2HttpMessageConverter) {
                convertorsIterator.remove();
            }
        }

        handlerAdapter.getMessageConverters().add(customJackson2HttpMessageConverter());
    }

}
