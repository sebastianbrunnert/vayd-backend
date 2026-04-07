package de.vayd.sebastianbrunnert.authentication.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

public class JacksonViewContextModifier extends BeanSerializerModifier {
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = beanProperties.get(i);

            // Wrap each property writer
            beanProperties.set(i, new BeanPropertyWriter(writer) {
                @Override
                public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
                        throws Exception {

                    JacksonViewContext.setCurrentView(prov.getActiveView());
                    try {
                        super.serializeAsField(bean, gen, prov);
                    } finally {
                        JacksonViewContext.clear();
                    }
                }
            });
        }
        return beanProperties;
    }
}
