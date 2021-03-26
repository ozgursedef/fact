package com.vestel.iot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

/**
 * Cost Minimization for Deploying Serverless Functions
 *
 */
public class App {

    static int memoryCount = 0;
    static final int internalLoop = 10;
    static final int validationCount = 100;
    static final int MINMEMORY = 128;
    static final int MAXMEMORY = 1024;
    static final int STEP = 64;
    static LinkedList<Double> x = new LinkedList<>();
    static LinkedList<Double> y = new LinkedList<>();
    static LinkedList<Double> predict = new LinkedList<>();
    static LinkedList<Double> logx = new LinkedList<>();
    static LinkedList<Double> logy = new LinkedList<>();
    static double[] validatedY = new double[validationCount];
    static double[] measuredY;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        long start = DateTime.now().getMillis();
        String response;
        memoryCount = (MAXMEMORY - STEP) / STEP;
        measuredY = new double[memoryCount];
        ConfigurationManager configManager = new ConfigurationManager();
        for (Service service : configManager.read()) {

            AwsCli aws = new AwsCli(service);
            for (double i = MINMEMORY; i <= MAXMEMORY; i += STEP) {
                x.add(i);
                aws.setMemory(i);
                System.out.println("\nMEMORY: " + i + " MB\n");
                aws.invoke();// coldstart
                for (int k = 0; k < internalLoop; k++) {
                    response = aws.invoke();
                    measuredY[k] = getExecutionTime(response);
                    System.out.println(k + ": " + measuredY[k]);
                    Thread.sleep(1000);
                }
                y.add(mean(measuredY));
                System.out.printf("MEAN: %.2f ms\n", mean(measuredY));
            }
            transform();
            PowerRegression regression = new PowerRegression(logx, logy);
            System.out.println("\nPower Regression: ");
            System.out.println(regression.toString());
            predict(regression.intercept(), regression.slope());

            System.out.println("\n-------------RESULTS----------------");
            printResults();

            System.out.println("\n-------------VALIDATION----------------");
            validate(aws);

            System.out.println("\nSession Time: " + ((DateTime.now().getMillis() - start) / 60000) + " dk");

        }
    }

    private static void validate(AwsCli aws) throws IOException, InterruptedException {

        String response = "";
        double sum = 0.0;
        int index = getMemoryIndex();
        System.out.println("\nSelected Memory: " + x.get(index) + " MB");
        aws.setMemory(x.get(index));
        for (int i = 0; i < validationCount; i++) {
            response = aws.invoke();
            validatedY[i] = getExecutionTime(response);
            sum += validatedY[i];
            System.out.print(".");
            Thread.sleep(1000);
        }
        writePlotData(validatedY);
        System.out.println("\n        Predicted        Validated\n");
        System.out.printf("          %.2f          %.2f\n", predict.get(index), sum / validationCount);
    }

    private static void writePlotData(double[] validated_y) {

        try {
            FileWriter myWriter = new FileWriter("plotdata.txt");
            StringBuilder stringBuilder = new StringBuilder();
            for (double d : validated_y) {
                stringBuilder.append(d);
                stringBuilder.append("\n");
            }
            myWriter.write(stringBuilder.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static int getMemoryIndex() {

        return (int) (Math.random() * (10 - 1 + 1) + 1);
    }

    private static void printResults() {

        System.out.println("        Measured        Predicted\n");
        for (int i = 0; i < memoryCount; i++) {
            System.out.printf("          %.2f          %.2f\n", y.get(i), predict.get(i));
        }
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