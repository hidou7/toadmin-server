package com.hidou7.toadmin.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        objectMapper.registerModule(simpleModule);
    }

    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("json序列化出错", e);
        }
    }

    public static <T> T parseObject(String json, Class<T> tClass) {
        if(json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, tClass);
        } catch (Exception e) {
            throw new RuntimeException("json反序列化出错", e);
        }
    }

    public static <E> List<E> parseList(String json, Class<E> eClass) {
        if(json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (Exception e) {
            throw new RuntimeException("json反序列化出错", e);
        }
    }

    public static <E> Set<E> parseSet(String json, Class<E> eClass) {
        if(json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(Set.class, eClass));
        } catch (Exception e) {
            throw new RuntimeException("json反序列化出错", e);
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> kClass, Class<V> vClass) {
        if(json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (Exception e) {
            throw new RuntimeException("json反序列化出错", e);
        }
    }

    public static <T> T nativeRead(String json, TypeReference<T> type) {
        if(json == null){
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("json反序列化出错", e);
        }
    }
    
    public static ObjectMapper getObjectMapper(){
        return objectMapper;
    }
}
