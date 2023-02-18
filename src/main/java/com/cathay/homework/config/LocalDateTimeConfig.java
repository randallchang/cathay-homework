package com.cathay.homework.config;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class LocalDateTimeConfig {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        return builder -> {
            builder.serializerByType(
                LocalDateTime.class,
                new LocalDateTimeSerializer());
            builder.deserializerByType(
                LocalDateTime.class,
                new LocalDateTimeDeserializer());
        };
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(
                LocalDateTime localDateTime,
                JsonGenerator jsonGenerator,
                SerializerProvider serializers) throws IOException {

            if (localDateTime != null) {
                jsonGenerator.writeString(localDateTime.format(FORMATTER));
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(
                JsonParser jsonParser,
                DeserializationContext deserializationContext) throws IOException {

            String localDateTime = jsonParser.getValueAsString();

            if (localDateTime != null) {
                return LocalDateTime.parse(jsonParser.getValueAsString(), FORMATTER);
            } else {
                return null;
            }
        }
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new LocalDateTimeConverter();
    }

    public static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(String timestampString) {

            return Instant.ofEpochMilli(Long.parseLong(timestampString))
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }
    }
}
