# FACT (FaaS Cost Minimization Tool)

**FACT** tool aims at minimizing the cost of deployed serverless functions from the application provider perspective. Detailed information regarding the project can be obtained at the [Wiki page](https://github.com/ozgursedef/fact/wiki)

FACT Tool consist of 3 seperate Java8 Maven projects. In configuration_optimizer, it uses GEKKO python3 framework. In order to execute lambda functions, it uses configured aws cli tool.

To compile the projects use command below in the root project folder which pom.xml is exist.

`mvn clean; mvn install -DskipTests`
