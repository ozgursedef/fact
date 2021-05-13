# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

FACT Tool consist of 3 seperate Java8 Maven projects.  
In configuration_optimizer, it uses GEKKO python3 framework.  
In order to execute lambda functions. Tool needs configured aws cli tool.  

To compile the projects use command below in the root project folder which pom.xml is exist.

`mvn clean; mvn install -DskipTests`

> Our overall approach involves 3 stages: 
1. Performance Analyzer
reads lambda function configrations through a file and executes the function under various memory settings. It calculates power/linear regression results for the the next step
3. Configuration Optimizer
reads power/linear regression results and calculates the optimum memory for the function.
5. Function Profiler 
reads optimum memory of the function and executes the function regarding the optimum memory for 100 times. This stage validates GEKKO results if predicted memory latecy is under LIMIT variable.

### 1) Performance Analyzer
To compile;

`cd performance_analyzer`  
`mvn clean; mvn install -DskipTests`

This step is reading function parameters through lambdaConfiguration.json file from the fact/(root). Simply write down your lambda services name and payload if is available;

`"service": {
  "name":"example_service_name",  
  "payload":"{\"example\":\"payload\"}"
}`

This stage prints the results performance_model.txt for the predicted values. For the regression analysis results, it print out to the r_results.txt file.
