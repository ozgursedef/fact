# fact
FACT (FaaS Cost Minimization Tool)
Cost Minimization for Deploying Serverless Functions
Deployment of serverless functions are subject to costs that are calculated based on a number of factors. One of these factors is the amount of memory reserved on the deployed server. Reservation of excessive memory increases costs unnecessarily. On the other hand, increasing the amount of available memory decreases the function execution time, which is also a factor that contributes to cost. Moreover, insufficient memory can degrade the quality of service. We propose an automated approach for optimizing the amount of memory to be reserved for servers where functions are deployed. First, we measure the running time of a given function in various memory settings and derive a regression model. We define an objective function and a set of constraints based on this regression model and the configuration space. We obtain a nonlinear programming model, which is solved to determine the optimal memory setting for minimizing cost. We evaluate our approach with an industrial case study on the use of Amazon Web services in the context of Smart Home applications. We show that our approach is effective in accurately estimating the impact of memory settings on runtime performance and determining optimal settings leading to significant cost reductions.

Compile
mvn clean; mvn install -DskipTests

Run and Debug
Simple run the code with run button on the main function in Visual Studio Code
