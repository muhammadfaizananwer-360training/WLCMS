package com.softech.ls360.lcms.contentbuilder.model;

import java.io.Serializable;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.enhanced.TableGenerator;

public class PrimaryKeyGenerator extends TableGenerator {

    @Override
    public synchronized Serializable generate(SessionImplementor session, Object obj) {
        return (Long) super.generate(session, obj) + 1;
    }
}