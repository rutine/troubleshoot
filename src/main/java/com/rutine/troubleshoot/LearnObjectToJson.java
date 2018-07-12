package com.rutine.troubleshoot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LearnObjectToJson {

    public static void test() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(new Object()));
    }
}
