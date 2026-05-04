package com.githubx.usersms.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTests {

    @Test
    void objectMapperBeanConfigured() {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.objectMapper();
        assertNotNull(mapper, "ObjectMapper should not be null");
        // JavaTimeModule registered -> dates are not written as timestamps
        assertFalse(mapper.getSerializationConfig().isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));
        // unknown properties should not fail
        assertFalse(mapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }
}

