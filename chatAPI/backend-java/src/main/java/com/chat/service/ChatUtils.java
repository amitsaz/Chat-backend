package com.chat.service;

import com.chat.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asazawal on 10/21/17.
 * Some utility methods for :
 * 1.Generating  cache key
 * 2.Load static data for Image and Videos.
 */
public class ChatUtils {

    private static Map<String, String> imgMetaData = loadimgData();
    private static Map<String, String> videoMetaData = loadVideoData();
    private static ObjectMapper mapper = new ObjectMapper();


    public static String getMessageKey(String sender, String receiver) {

        StringBuffer key = new StringBuffer();
        String[] input = {sender.trim(), receiver.trim()};

        Arrays.sort(input);

        key.append(input[0]).append(":").append(input[1]);

        return key.toString();


    }



    public static String getMetaData(String message_type) throws JsonProcessingException {

        if (message_type.equalsIgnoreCase(Message.MESSAGE_TYPE.IMAGE.getType()))
            return mapper.writeValueAsString(imgMetaData);
        else if (message_type.equalsIgnoreCase(Message.MESSAGE_TYPE.VIDEO.getType()))
            return mapper.writeValueAsString(videoMetaData);
        else
            return null;


    }

    private static Map<String, String> loadimgData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("scr", "https://www.somedomain.com");
        map.put("height", "45px");
        map.put("width", "50px");
        return map;


    }

    private static Map<String, String> loadVideoData() {
        Map<String, String> map = new HashMap<>();
        map.put("scr", "https://www.videodomain.com");
        map.put("tags", "comedy");
        map.put("length", "100 mins");
        return map;


    }
}
