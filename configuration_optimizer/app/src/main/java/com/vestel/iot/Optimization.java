package com.vestel.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Optimization {
    
    public Optimization(double a, double b, int m, boolean isLinear) throws IOException{
        if(isLinear){
            invoke("lr.py",a,b,m);
        }else{
            invoke("pr.py",a,b,m);
        }
    }

    private void invoke(String script, double a, double b, int m) throws IOException {
        String command = "python3 configuration_optimizer/app/scripts/" + script + " -a " + a + " -b " + b + " -min " + m;
        Process proc = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
