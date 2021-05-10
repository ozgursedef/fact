package com.vestel.iot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager {

    List<Service> list = new ArrayList<>();
    int counter = 0;

    public List<Service> read(){
        
        try (FileReader reader = new FileReader("r_results.txt")) {
            BufferedReader br = new BufferedReader(reader);
            String st;
            while ((st = br.readLine()) != null){
                System.out.println(counter++ + " - " + parse(st));
                list.add(parse(st));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 

        return list;
    }

    private Service parse(String s) {

        Service service = new Service();
        service.a = Double.parseDouble(s.split(",")[0]);
        service.b = Double.parseDouble(s.split(",")[1]);
        service.min = Integer.parseInt(s.split(",")[2]);
        service.index = Integer.parseInt(s.split(",")[3]);
        service.limit = Integer.parseInt(s.split(",")[4]);
        return service;
    }
}
