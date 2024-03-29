package com.vestel.iot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

/**
 * Cost Minimization for Deploying Serverless Functions
 *
 */
public class App {

    static int memoryCount = 0;
    static final int internalLoop = 10;
    static final int MINMEMORY = 320;
    static final int MAXMEMORY = 3008;
    static final int STEP = 64;
    static LinkedList<Double> x = new LinkedList<>();
    static LinkedList<Double> y = new LinkedList<>();
    static LinkedList<Double> predict = new LinkedList<>();
    static LinkedList<Double> logx = new LinkedList<>();
    static LinkedList<Double> logy = new LinkedList<>();
    static double[] measuredY = new double[internalLoop];
    static boolean isLinear;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        long start = DateTime.now().getMillis();
        String response;
        LinearRegression lr= null;
        double a,b =0;
        StringBuilder sb = new StringBuilder();

        memoryCount = (MAXMEMORY - (MINMEMORY - STEP)) / STEP;
        ConfigurationManager configManager = new ConfigurationManager();
        for (Service service : configManager.read()) {

            CloudProvider cp = new AwsCli(service);
            for (double i = MINMEMORY; i <= MAXMEMORY; i += STEP) {
                x.add(i);
                cp.setMemory(i);
                System.out.println("\nMEMORY: " + i + " MB\n");
                cp.invoke();// coldstart
                for (int k = 0; k < internalLoop; k++) {
                    response = cp.invoke();
                    measuredY[k] = getExecutionTime(response);
                    System.out.println(k + ": " + measuredY[k]);
                    Thread.sleep(1000);
                }
                y.add(mean(measuredY));
                System.out.printf("MEAN: %.2f ms\n", mean(measuredY));
            }
            transform();
            PowerRegression pr = new PowerRegression(logx, logy);
            System.out.println(pr.toString());
            if(pr.R2() < 0.75){
                lr = new LinearRegression(x, y);
                if(lr.R2() > pr.R2()){
                    isLinear = true;
                }
            }

            if(!isLinear){
                System.out.println("\nPower Regression: ");
                System.out.println(pr.toString());
                predict(pr.intercept(), pr.slope());
                b = pr.slope();
                a = pr.intercept();
            }else{
                System.out.println("\nLinear Regression: ");
                System.out.println(lr.toString());
                lpredict(lr.slope(),lr.intercept());
                a = lr.slope();
                b = lr.intercept();
            }
            sb.append(String.format("%.2f,%.2f,%s", a, b, isLinear ? "l" : "p"));
            
            System.out.println("\n-------------RESULTS----------------");
            printResults();
    
            System.out.println("\nSession Time: " + ((DateTime.now().getMillis() - start) / 60000) + " dk");
        }
        FileWriter myWriter = new FileWriter("r_results.txt", true);
        myWriter.write(sb.toString());
        myWriter.write("\n");
        myWriter.close();
    }

    private static void printResults() throws IOException {
        StringBuilder sb = new StringBuilder();
        System.out.println("Memory       Measured       Predicted\n");
        sb.append("Memory       Predicted\n");
        for (int i = 0; i < memoryCount; i++) {
            System.out.printf("%d          %.2f         %.2f%n", x.get(i).intValue(), y.get(i), predict.get(i));
            sb.append(String.format("%d          %.2f%n",x.get(i).intValue(), predict.get(i)));
        }
        FileWriter myWriter = new FileWriter("performance_model_s1.txt");
        myWriter.append(sb.toString());
        myWriter.close();

    }

    private static void predict(double a, double b) {

        for (int i = 0; i < memoryCount; i++) {
            if (b * -1 > 0) {
                predict.add(a * 1 / Math.pow(x.get(i), -b));
            } else {
                predict.add(a * Math.pow(x.get(i), b));
            }
        }
    }

    private static void lpredict(double a, double b) {

        for (int i = 0; i < memoryCount; i++) {
            predict.add(a * x.get(i) + b);
        }
    }

    private static double getExecutionTime(String response) {

        return Double.parseDouble(response.split(" ")[3]);
    }

    private static double mean(double[] measured_y) {

        double sum = 0;
        for (double y : measured_y) {
            sum += y;
        }
        return sum / internalLoop;
    }

    private static void transform() {

        for (int i = 0; i < memoryCount; i++) {
            logx.add(Math.log(x.get(i)));
            logy.add(Math.log(y.get(i)));
        }
    }
}