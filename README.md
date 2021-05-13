# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

FACT Tool consist of 3 seperate Java8 Maven projects. In configuration_optimizer, it uses GEKKO python3 framework. In order to execute lambda functions, it uses configured aws cli tool.

To compile the projects use command below in the root project folder which pom.xml is exist.

`mvn clean; mvn install -DskipTests`

### Performance Analyzer
To compile;

`cd performance_analyzer`
`mvn clean; mvn install -DskipTests`

This step is reading function parameters through lambdaConfiguration.json file from the fact/(root). Simply write down your lambda services name and payload if is available;

`"service": {
  "name":"example_service_name",
  "payload":"{\"example\":\"payload\"}"
}`
