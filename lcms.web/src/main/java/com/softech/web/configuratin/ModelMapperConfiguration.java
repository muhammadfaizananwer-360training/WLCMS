package com.softech.web.configuratin;

import com.softech.common.parsing.TabularDataException;
import com.softech.ls360.lcms.contentbuilder.web.api.dto.ErrorDetailsAPIDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Abdul Wahid on 6/20/2016.
 */
@Configuration
public class ModelMapperConfiguration {

    @Autowired
    public void configureModelMapper(ModelMapper mapper){
        mapper.addMappings(new PropertyMap<TabularDataException.ExceptionDetail, ErrorDetailsAPIDTO>() {
            @Override
            protected void configure() {
                map().setErrorValue(source.getErrorText());
                map().setResourceName(source.getTableName());
            }
        });

    }
}
