package de.vayd.sebastianbrunnert.authentication.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JacksonViewContextModule extends SimpleModule {

    public JacksonViewContextModule() {
        setSerializerModifier(new JacksonViewContextModifier());
    }

}
