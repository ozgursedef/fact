package com.vestel.iot;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        ConfigurationManager configManager = new ConfigurationManager();
        for (Service service : configManager.read()) {
            Optimization opt = new Optimization(service.a, service.b, service.min, false);
        }
        
    }
}
