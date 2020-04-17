package com.redhat.rest.example.demorest;

import com.google.gson.Gson;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.PropertySource;

import java.util.*;

@PropertySource("classpath:categorization.properties")
public class TransformerBean {



    public String returnSegment(Exchange exchange) {
        System.out.println(exchange.getIn().getBody());
        JSONObject obj = new JSONObject(exchange.getIn().getBody(String.class));
        JSONArray valMap = obj.getJSONObject("data").getJSONArray("ndarray");

        String segment=null;


        List<Double> valList = new ArrayList<>();
        valList.add((Double)valMap.getJSONArray(0).get(0));
        valList.add((Double)valMap.getJSONArray(0).get(1));
        valList.add((Double)valMap.getJSONArray(0).get(2));

        System.out.println(valList);
        Double maxVal = Collections.max(valList);
        Integer maxIdx = valList.indexOf(maxVal);

        if(maxIdx == 0){
            segment = "LOW";
        } else if(maxIdx == 1) {
            segment =  "MEDIUM";
        } else if(maxIdx == 2) {
            segment = "HIGH";
        }

        Customer customer = (Customer)exchange.getProperty("customer");
        customer.setPrediction(segment);

        return new Gson().toJson(customer);
    }

    public String lookUpCustId(Exchange exchange) {

        String body = exchange.getIn().getBody(String.class);


        Map mapVal = new Gson().fromJson(body,LinkedHashMap.class);
        System.out.println(mapVal.keySet());

        Object valueSet = mapVal.get("value");
        System.out.println(valueSet.getClass());
        ArrayList list = (ArrayList) valueSet;
        Map customerMap= null;
        Customer customer  =new Customer();
        for(Object object:list) {
            customerMap = (Map) object;
            System.out.println(exchange.getProperties().get("custId"));
            if(customerMap.get("custId").equals(exchange.getProperties().get("custId"))) {
                customer.setCustId((String) exchange.getProperties().get("custId"));
                customer.setCustomerClass((String) customerMap.get("customerClass"));
                customer.setQualifiedPurchases((String) customerMap.get("txnType"));
                customer.setAge(String.valueOf((Double) customerMap.get("age")));
                if(null == customerMap.get("lastOfferAcceptance")) {
                    customer.setLastOfferResponse("0");
                }else {
                    customer.setLastOfferResponse(String.valueOf((Double) customerMap.get("lastOfferAcceptance")));
                }
                customer.setIncome(String.valueOf((Double) customerMap.get("income")));

            }
        }

        String eventNoType = "";
        switch ((String)exchange.getProperties().get("eventType")) {
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

        String requestString = "{\"data\":{\"ndarray\":[["+customer.getIncome()+", "+customer.getLastOfferResponse()+", "+eventNoType+"]]}}";

        System.out.println(requestString);

        exchange.setProperty("customer",customer);

       return requestString;



    }




}
