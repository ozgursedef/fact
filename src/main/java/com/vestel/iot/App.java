package com.vestel.iot;

import java.io.FileWriter;
import java.io.IOException;

import org.joda.time.DateTime;
import org.json.simple.parser.ParseException;

/**
 * Cost Minimization for Deploying Serverless Functions
 *
 */
public class App {

    static final int memoryCount = 12;
    static final int timeCount = 3;
    static final int validationCount = 100;
    static double[] x = { 256, 512, 768, 1024, 1280, 1472, 1728, 2048, 2240, 2496, 2752, 3008 };
    static double[] y = new double[memoryCount];
    static double[] measured_y = new double[timeCount];
    static double[] predict = new double[memoryCount];
    static double[] logx = new double[memoryCount];
    static double[] logy = new double[memoryCount];
    static double[] validated_y = new double[validationCount];

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        long start = DateTime.now().getMillis();
        String response;

        ConfigurationManager configManager = new ConfigurationManager();
        for (Service service : configManager.read()) {

            AwsCli aws = new AwsCli(service);
            for (int i = 0; i < memoryCount; i++) {
                aws.setMemory(x[i]);
                System.out.printf("\nMEMORY: " + (int) x[i] + " MB\n");
                aws.invoke();// coldstart
                for (int k = 0; k < timeCount; k++) {
                    response = aws.invoke();
                    measured_y[k] = getExecutionTime(response);
                    System.out.println(k + ": " + measured_y[k]);
                    Thread.sleep(1000);
                }
                y[i] = mean(measured_y);
                System.out.printf("MEAN: %.2f ms\n", y[i]);
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
        System.out.println("\nSelected Memory: " + (int) x[index] + " MB");
        int memory = (int) x[index];
        aws.setMemory(memory);
        for (int i = 0; i < validationCount; i++) {
            response = aws.invoke();
            validated_y[i] = getExecutionTime(response);
            sum += validated_y[i];
            System.out.print(".");
            Thread.sleep(1000);
        }
        writePlotData(validated_y);
        System.out.println("\n        Predicted        Validated\n");
        System.out.printf("          %.2f          %.2f\n", predict[index], sum / validationCount);
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
            System.out.printf("          %.2f          %.2f\n", y[i], predict[i]);
        }
    }

    private static void predict(double a, double b) {

        for (int i = 0; i < memoryCount; i++) {
            if (b * -1 > 0) {
                predict[i] = a * 1 / Math.pow(x[i], -b);
            } else {
                predict[i] = a * Math.pow(x[i], b);
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
        return sum / timeCount;
    }

    private static void transform() {

        for (int i = 0; i < memoryCount; i++) {
            logx[i] = Math.log(x[i]);
            logy[i] = Math.log(y[i]);
        }
    }
}