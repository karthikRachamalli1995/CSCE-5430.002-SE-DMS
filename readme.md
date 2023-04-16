Read Me

* Install JAVA 11 by following using below link
  https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html
* Install Oracle database by following using below link
  https://github.com/oracle/vagrant-projects/blob/main/README.md#prerequisites
* Make sure that the oracle is hosted on "**jdbc:oracle:thin:@localhost:1521:xe**"
* If it is hosted on another URL, change the oracle database properties in application.yml
* Install Intellij IDE using below link  
  https://www.jetbrains.com/idea/
* Build the project using below command in the project after moving into project repo
  "**./gradlew build**"
* Navigate the "build/libs" folder, where JAR is generated and stored.
* Now, we can run the project using the below command
  **java -jar <name_given_to_JAR>.jar**
  