package com.vestel.iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Optimization {

    public Optimization(Service s, boolean isLinear) throws IOException {
       double a = s.a;
       double b = s.b;
       int m = s.min;
       int l = s.limit;
       int i = s.index;
        if (isLinear) {
            invoke("lr.py", a, b, m, l, i);
        } else {
            invoke("pr.py", a, b, m, l, i);
        }
    }

    private void invoke(String script, double a, double b, int m, int l, int i) throws IOException {
        String command = "python3 configuration_optimizer/app/scripts/" + script + " -a " + a + " -b " + b + " -min "
                + m + " -index " + i + " -limit " + l;
        Process proc = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
