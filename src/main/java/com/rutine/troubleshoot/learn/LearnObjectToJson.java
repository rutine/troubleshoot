package com.rutine.troubleshoot.learn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LearnObjectToJson {

    public static void main(String[] args) throws Exception {
        LearnObjectToJson.test();
    }

    public static void test() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(new Object()));
    }
}
