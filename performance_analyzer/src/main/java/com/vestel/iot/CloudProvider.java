package com.vestel.iot;

import java.io.IOException;

public interface CloudProvider {

    void setMemory(double memory) throws IOException;
    String invoke() throws IOException, InterruptedException; 
}
