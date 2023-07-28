package com.kafka.demo.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.demo.model.store.GarbageRepository;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
    public static final String OBJECT_MAPPER = "objectMapper";
    public static final String TYPE_REFERENCE = "typeReference";

    private final String encoding = StandardCharsets.UTF_8.name();
    private ObjectMapper mapper;
    private TypeReference<T> typeReference;

    private final GarbageRepository garbageRepository;

    public JsonDeserializer(GarbageRepository garbageRepository) {
        this.garbageRepository = garbageRepository;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> configs, boolean isKey) {
        mapper = (ObjectMapper) configs.get(OBJECT_MAPPER);
        if (mapper == null) {
            throw new IllegalArgumentException("config property OBJECT_MAPPER was not set");
        }
        typeReference = (TypeReference<T>) configs.get(TYPE_REFERENCE);
        if (typeReference == null) {
            throw new IllegalArgumentException("config property TYPE_REFERENCE was not set");
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            } else {
                var valueAsString = new String(data, encoding);
                return mapper.readValue(valueAsString, typeReference);
            }
        } catch (Exception e) {
             return null;
//            throw new SerializationException("Error when deserializing byte[] to StringValue", e);
        } finally {
            garbageRepository.add(data);
        }
    }
}
//{"sender": "Console","content": "Hello bro!"}