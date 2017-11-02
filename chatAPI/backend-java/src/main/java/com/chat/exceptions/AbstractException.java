package com.chat.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asazawal on 10/21/17.
 */
public abstract class AbstractException {

    public Object getDefaultMessage(int code, String message) throws JsonProcessingException {

        Map map = new HashMap();
        map.put("code",code);
        map.put("message",message);
        return  new ObjectMapper().writeValueAsString(map);

    }
}
