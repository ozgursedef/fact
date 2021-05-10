package app;

import java.io.FileWriter;
import java.io.IOException;

/**
 * FACT Tool
 * Configuration Optimizer
 */
public class App 
{
    static double[] data = new double[100];
    
    public static void main( String[] args ) throws IOException, InterruptedException
    {
        ConfigurationManager configManager = new ConfigurationManager();
        for (Service s : configManager.read()) {
            AwsCli aws = new AwsCli(s);
            validate(aws, s);
        }
    }

    private static void validate(AwsCli aws, Service s) throws IOException, InterruptedException {

        String response = null;
        int sum = 0;
        aws.setMemory(s.memory);
        aws.invoke(); //cold start
        for (int i = 0; i < 100; i++) {
            response = aws.invoke();
            data[i] = getExecutionTime(response);
            sum += data[i];
            System.out.print(".");
            Thread.sleep(1000);
        }
        writePlotData(data);
       
        System.out.println(sum / 100);
    }

    private static double getExecutionTime(String response) {

        return Double.parseDouble(response.split(" ")[3]);
    }

    private static void writePlotData(double[] a) {

        try {
            FileWriter w = new FileWriter("plotdata.txt");
            StringBuilder sb = new StringBuilder();
            for (double d : a) {
                sb.append(d);
                sb.append("\n");
            }
            w.write(sb.toString());
            w.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
