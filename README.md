# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

This repository includes 3 separate Java 8 Maven projects. One of these projects, configuration_optimizer, uses the [GEKKO python3 framework](https://gekko.readthedocs.io/en/latest/). [AWS Command Line Interface (CLI)](https://aws.amazon.com/cli/) is used for executing lambda functions. The tool can be installed using the command below at the root project folder, where the pom.xml file takes place:

`mvn clean; mvn install -DskipTests`

The usage of the tool for the [overall approach](https://github.com/ozgursedef/fact/wiki) involves 3 stages: 
1. Performance Analyzer
reads lambda function configurations from a file and executes the function under various memory settings. It calculates power/linear regression results for the next stage.
3. Configuration Optimizer
reads power/linear regression results and calculates the optimum amount of memory to be allocated for the function.
5. Function Profiler 
executes the function for 100 times, where the allocated memory is determined by the previous stage. This stage verifies if the allocation is enough for keeping the latency of the function under a specified *LIMIT*.

### 1) Performance Analyzer

In the fact/performance_analyzer/src/main/java/com/vestel/iot/App.java class there exist the following variables that can be altered for specific configuration options.  
`static final int internalLoop = 10;`  
`static final int MINMEMORY = 320;`  
`static final int MAXMEMORY = 3008;`  
`static final int STEP = 64;`  

Use the commands below for compilation:

`cd performance_analyzer`  
`mvn clean; mvn install -DskipTests`

This step is for retrieving function parameters from the *lambdaConfiguration.json* file that is located at the folder *fact/(root)*. These parameters include the name of the lambda service and payload, if any available, e.g.,

`"service": {
  "name":"example_service_name",  
  "payload":"{\"example\":\"payload\"}"
}`

This stage prints the results exported to the *performance_model.txt* file for the predicted values. It prints out the regression analysis results to the *r_results.txt* file.

To run and debug:  
Use VsCode Studio run and debug feature in the fact/performance_analyzer/src/main/java/com/vestel/iot/App.java class

### 2) Configuration Optimizer

Python3 GEKKO library is used to calculate non-linear programming model results.

Use the commands below for compilation:

`cd configuration_optimizer`  
`mvn clean; mvn install -DskipTests`

This step is for reading function regression results from the *r_results.txt* file that is located at the folder *fact/(root)*. It prints optimization results to the *o_results.txt* file.

To run and debug:   
Use VsCode Studio run and debug feature in the fact/configuration_optimizer/app/src/main/java/com/vestel/iot/App.java class

To run standalone:  
`python3 configuration_optimizer/app/scripts/pr.py -a a -b b -min m -index i -limit l`

### 3) Function Profiler

Use the commands below for compilation:

`cd function_profiler`  
`mvn clean; mvn install -DskipTests`

This step is for reading function optimization results from the *o_results.txt* file that is located at the folder *fact/(root)*. It prints optimization results to the *plot_data.txt* file.

To run and debug:  
Use VsCode Studio run adn debug feature in the fact/function_profiler/app/src/main/java/app/App.java


