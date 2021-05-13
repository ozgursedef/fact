# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

FACT Tool consists of 3 separate Java 8 Maven projects. One of these projects, configuration_optimizer, uses the [GEKKO python3 framework](https://gekko.readthedocs.io/en/latest/). [AWS Command Line Interface (CLI)](https://aws.amazon.com/cli/) is used for executing lambda functions.

To compile the projects, use the command below at the root project folder, where the pom.xml file takes place:

`mvn clean; mvn install -DskipTests`

> Our overall approach involves 3 stages: 
1. Performance Analyzer
reads lambda function configurations through a file and executes the function under various memory settings. It calculates power/linear regression results for the the next step
3. Configuration Optimizer
reads power/linear regression results and calculates the optimum memory for the function.
5. Function Profiler 
reads optimum memory of the function and executes the function regarding the optimum memory for 100 times. This stage validates GEKKO results if predicted memory latecy is under LIMIT variable.

### 1) Performance Analyzer
In the fact/performance_analyzer/src/main/java/com/vestel/iot/App.java class there exist the following variables that can be altered for specific configuration options.  
`static final int internalLoop = 10;`  
`static final int MINMEMORY = 320;`  
`static final int MAXMEMORY = 3008;`  
`static final int STEP = 64;`  

Use the commands below for compilation:

`cd performance_analyzer`  
`mvn clean; mvn install -DskipTests`

This step is for retrieving function parameters from the lambdaConfiguration.json file that is located at the folder fact/(root). These parameters include the name of the lambda service and payload, if any available, e.g.,

`"service": {
  "name":"example_service_name",  
  "payload":"{\"example\":\"payload\"}"
}`

This stage prints the results exported to the performance_model.txt file for the predicted values. It prints out the  regression analysis results to the r_results.txt file.

> To run and debug:  
Use VsCode Studio run and debug feature in the fact/performance_analyzer/src/main/java/com/vestel/iot/App.java class

### 2) Configuration Optimizer

Python3 GEKKO library is used to calculate non-linear programming model results.

Use the commands below for compilation:

`cd configuration_optimizer`  
`mvn clean; mvn install -DskipTests`

This step is reading function regression results through r_results.txt file from the fact/(root). It prints optimization results to o_results.txt file.

> To run and debug:   
Use VsCode Studio run and debug feature in the fact/configuration_optimizer/app/src/main/java/com/vestel/iot/App.java class

> To run standalone:  
`python3 configuration_optimizer/app/scripts/pr.py -a a -b b -min m -index i -limit l`

### 3) Function Profiler

Use the commands below for compilation:

`cd function_profiler`  
`mvn clean; mvn install -DskipTests`

This step is reading function optimization results through o_results.txt file from the fact/(root). It prints optimization results to plot_data.txt file.

> To run and debug:  
Use VsCode Studio run adn debug feature in the fact/function_profiler/app/src/main/java/app/App.java


