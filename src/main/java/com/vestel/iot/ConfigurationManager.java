package com.vestel.iot;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigurationManager {

    List<Service> list = new ArrayList<>();

    public List<Service> read() throws org.json.simple.parser.ParseException {
            
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("lambdaConfiguration.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONArray serviceList = (JSONArray) obj;
            for (Object object : serviceList) {
              parse((JSONObject) object);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
	}

    private Object parse(JSONObject services) {

        Service service = new Service();
        JSONObject jsonObj = (JSONObject) services.get("service");
         
        String name = (String) jsonObj.get("name");    
        service.name = name;
         
        String payload = (String) jsonObj.get("payload");  
        service.payload = payload;
        list.add(service);
        return null;
    }
}
