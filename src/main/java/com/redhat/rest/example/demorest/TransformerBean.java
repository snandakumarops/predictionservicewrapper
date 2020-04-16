package com.redhat.rest.example.demorest;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

import java.util.*;

@PropertySource("classpath:categorization.properties")
public class TransformerBean {



    public String transformResponse(String income, String eventType, String lastOfferResponse) {
        String eventNoType = "";
        switch (eventType) {
            case "AIRLINES" :
                eventNoType = "1"; break;
            case "MERCHANDISE" :
                eventNoType = "2"; break;
            case "HOTEL" :
                eventNoType = "3"; break;
            case "ONLINE PURCHASE" :
                eventNoType = "4"; break;
            case "UTILITIES" :
                eventNoType = "5"; break;
            case "RESTAURANTS" :
                eventNoType = "6";break;
            case "OTHERS":
                eventNoType="7"; break;
        }

        System.out.println(income+" "+eventType+" "+lastOfferResponse);

        if(lastOfferResponse == null) {
            lastOfferResponse = "0";
        }

        String requestString = "{\"data\":{\"ndarray\":[["+income+", "+lastOfferResponse+", "+eventNoType+"]]}}";

        System.out.println(requestString);


        return requestString;

    }

    public String returnSegment(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray valMap = obj.getJSONObject("data").getJSONArray("ndarray");



        List<Double> valList = new ArrayList<>();
        valList.add((Double)valMap.getJSONArray(0).get(0));
        valList.add((Double)valMap.getJSONArray(0).get(1));
        valList.add((Double)valMap.getJSONArray(0).get(2));

        System.out.println(valList);
        Double maxVal = Collections.max(valList);
        Integer maxIdx = valList.indexOf(maxVal);

        if(maxIdx == 0){
            return "LOW";
        } else if(maxIdx == 1) {
            return "MEDIUM";
        } else if(maxIdx == 2) {
            return "HIGH";
        }

        return null;
    }




}
