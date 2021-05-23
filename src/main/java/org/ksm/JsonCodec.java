package org.ksm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Data
public class JsonCodec<T> {
    ObjectMapper mapper;
    CollectionType collectionType;

    public JsonCodec() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
    }

    public JsonCodec(Class<T> kLass) {
        mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        collectionType = typeFactory.constructCollectionType(List.class, kLass);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
    }


    public String objectToString(T obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public T jsonToObj(Reader json, Class<T> kClass) throws IOException {
        return mapper.readValue(json, kClass);
    }

    public T jsonToObj(String jsonStr) throws IOException {
        return mapper.readValue(jsonStr, collectionType);
    }

    public T jsonToObj(String json, Class<T> kClass) throws JsonProcessingException {
        return mapper.readValue(json, kClass);
    }


}
