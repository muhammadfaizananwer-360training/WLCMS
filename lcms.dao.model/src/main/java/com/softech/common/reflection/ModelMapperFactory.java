
package com.softech.common.reflection;

import com.softech.ls360.lcms.contentbuilder.model.*;
import org.modelmapper.Condition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;

/**
 *
 * @author Sheikh Abdul Wahid
 */

@Configuration
public class ModelMapperFactory {
    
    @Bean
    public ModelMapper createDefaultMapper() {
       ModelMapper mapper = new ModelMapper();
       mapper.getConfiguration().setFieldMatchingEnabled(true);
       mapper.getConfiguration().setPropertyCondition(new Condition(){
           @Override
           public boolean applies(MappingContext context) {
               return context.getSource() != null;
           }
       });
       
       //*****add Mappings
        mapper.addMappings(new PropertyMap<CourseVO, CourseDTO>() {
            @Override
            protected void configure() {
                skip(destination.getContentownerId());
                skip(destination.getCreateUserId());
                skip(destination.getGuid());
                skip(destination.getCourseStatus());
                skip(destination.getCode());
                skip(destination.getCourseType());
            }
        });

       mapper.addMappings(new PropertyMap<SyncClassDTO, SynchronousClass>() {
           @Override
           protected void configure() {
               skip(destination.getCourse());
           }
       });
       
       mapper.addMappings(new PropertyMap<SynchronousClass, SyncClassDTO>() {
           @Override
           protected void configure() {
               skip(destination.getCourse());
           }
       });

        mapper.addMappings(new PropertyMap<SyncSessionDTO, SynchronousSession>() {
            @Override
            protected void configure() {
                skip(destination.getSyncClass());
            }
        });

        mapper.addMappings(new PropertyMap<SynchronousSession, SyncSessionDTO>() {
            @Override
            protected void configure() {
                skip(destination.getSyncClass());
            }
        });
        
        mapper.addMappings(new PropertyMap<CourseProviderDTO, CourseProvider>() {
            @Override
            protected void configure() {
                skip(destination.getCourse());
            }
        });
        
        mapper.addMappings(new PropertyMap<CourseProvider, CourseProviderDTO>() {
            @Override
            protected void configure() {
                skip(destination.getCourse());
            }
        });
       
       return mapper;
    } 
}
