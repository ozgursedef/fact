package app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.directory.model.ServiceException;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.LogType;

public class AwsCli {

    Service service = new Service();

    public AwsCli(Service service) {
        this.service = service;
    }

    void setMemory(double memorySize) throws IOException {
        String command = "aws lambda update-function-configuration --function-name " + service.name + " --memory-size "
                + (int) memorySize;
        Process proc = Runtime.getRuntime().exec(command);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    String invoke() throws IOException, InterruptedException {
        InvokeRequest invokeRequest = new InvokeRequest().withFunctionName(service.name)
                    .withPayload(service.payload)
                    .withLogType(LogType.Tail);
        
        InvokeResult invokeResult = null;
        try {
            AWSLambda awsLambda = AWSLambdaClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
                    .withRegion(Regions.EU_CENTRAL_1).build();
            invokeResult = awsLambda.invoke(invokeRequest);

        } catch (ServiceException e) {
            System.out.println(e);
        }
        // String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);
        // System.out.println(ans);
        try (Scanner s = new Scanner(
                new ByteArrayInputStream(Base64.getDecoder().decode(invokeResult.getLogResult())))) {
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.contains("Duration")) {
                    return line;
                }
            }
        }catch(NullPointerException e){
            System.out.println(e);
        }
        return "";
    }
}
