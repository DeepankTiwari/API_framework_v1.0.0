package com.deepank.api.testdata;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

public class JsonDataReader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T loadTestData(String filepath, Class<T> targetClass){

        try(InputStream is = JsonDataReader.class.getResourceAsStream(filepath)){
            if(is == null){
                throw new RuntimeException("Resource file not found " + filepath);
            }
            return OBJECT_MAPPER.readValue(is, targetClass);

        }catch (Exception e){
            throw new RuntimeException("Failed to parse the JSON file: " + filepath + "\nReason: " + e.getMessage());
        }
    }

}
