package com.know.knowboot.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Keep large snowflake IDs safe in JavaScript clients while preserving ordinary
 * timestamps and counters as numbers.
 */
@Configuration
public class JacksonConfig {

    private static final long JS_MAX_SAFE_INTEGER = 9007199254740991L;
    private static final JsonSerializer<Long> JS_SAFE_LONG_SERIALIZER = new JsonSerializer<Long>() {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null && (value > JS_MAX_SAFE_INTEGER || value < -JS_MAX_SAFE_INTEGER)) {
                gen.writeString(value.toString());
                return;
            }
            gen.writeNumber(value);
        }
    };

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer javascriptSafeLongCustomizer() {
        return builder -> {
            builder.serializerByType(Long.class, JS_SAFE_LONG_SERIALIZER);
            builder.serializerByType(Long.TYPE, JS_SAFE_LONG_SERIALIZER);
        };
    }
}