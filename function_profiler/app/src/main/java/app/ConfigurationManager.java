package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigurationManager {

    JSONParser jsonParser = new JSONParser();
    List<Service> list = new ArrayList<>();
    Service s = new Service();
    int counter = 0;

    public List<Service> read() {

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try (FileReader reader = new FileReader("lambdaConfiguration.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray serviceList = (JSONArray) obj;
            for (Object object : serviceList) {
                parseService((JSONObject) object);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        try (FileReader reader = new FileReader("o_results.txt")) {
            BufferedReader br = new BufferedReader(reader);
            String st;
            while ((st = br.readLine()) != null) {
                parseOpt(st);
                list.add(s);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return list;
    }

    private void parseOpt(String str) {

        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
           s.memory = Integer.parseInt(matcher.group(1));
        }
    }

    private void parseService(JSONObject services) {

        JSONObject jsonObj = (JSONObject) services.get("service");

        String name = (String) jsonObj.get("name");
        s.name = name;

        String payload = (String) jsonObj.get("payload");
        s.payload = payload;
    }
}
