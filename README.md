# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

FACT Tool consists of 3 separate Java 8 Maven projects. One of these projects, configuration_optimizer, uses the [GEKKO python3 framework](https://gekko.readthedocs.io/en/latest/). [AWS Command Line Interface (CLI)](https://aws.amazon.com/cli/) is used for executing lambda functions.

To compile the projects, use the command below at the root project folder, where the pom.xml file takes place:

`mvn clean; mvn install -DskipTests`

> Our overall approach involves 3 stages: 
1. Performance Analyzer
reads lambda function configrations through a file and executes the function under various memory settings. It calculates power/linear regression results for the the next step
3. Configuration Optimizer
reads power/linear regression results and calculates the optimum memory for the function.
5. Function Profiler 
reads optimum memory of the function and executes the function regarding the optimum memory for 100 times. This stage validates GEKKO results if predicted memory latecy is under LIMIT variable.

### 1) Performance Analyzer
In the fact/performance_analyzer/src/main/java/com/vestel/iot/App.java class there are following variables are needed to change according to your needs.  
`static final int internalLoop = 10;`  
`static final int MINMEMORY = 320;`  
`static final int MAXMEMORY = 3008;`  
`static final int STEP = 64;`  

To compile;

`cd performance_analyzer`  
`mvn clean; mvn install -DskipTests`

This step is reading function parameters through lambdaConfiguration.json file from the fact/(root). Simply write down your lambda services name and payload if is available;

`"service": {
  "name":"example_service_name",  
  "payload":"{\"example\":\"payload\"}"
}`

This stage prints the results performance_model.txt for the predicted values. For the regression analysis results, it print out to the r_results.txt file.


