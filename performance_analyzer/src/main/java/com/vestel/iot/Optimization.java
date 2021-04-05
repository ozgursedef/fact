package com.vestel.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Optimization {
    
    public Optimization(int a, int b, boolean isLinear) throws IOException{
        if(isLinear){
            invoke("linear.py",a,b);
        }else{
            invoke("non-linear",a,b);
        }
    }

    private void invoke(String script, int a, int b) throws IOException {
        String command = "python3 scripts/" + script + " -a " + a + " -b " + b;
        Process proc = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
