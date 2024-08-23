package com.github.gun2.server;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestDataMutationUtil {

    /**
     * 객체를 json string 형태로 반환
     * @param obj json string으로 변환할 객체
     * @return 변환된 json string
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
