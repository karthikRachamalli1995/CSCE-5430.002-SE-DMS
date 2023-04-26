**Read Me**

* Install Intellij IDE using the link -
  https://www.jetbrains.com/idea/
* Download & Install JAVA 11 by following using below link
  https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html
* Install Oracle database by following the tutorial in 
  https://github.com/oracle/vagrant-projects/blob/main/README.md#prerequisites
* Make sure that the oracle is hosted on "**jdbc:oracle:thin:@localhost:1521:xe**"
* If oracle is hosted on another URL, make sure to change the oracle database properties in application.yml
* Change the username and password in application.yml file to the local oracle database credentials, to make the app connect to the local oracle.
* Build the project using below command in the project after navigating into project repo using terminal using the command -
  "**./gradlew clean build**"
* Once after successful building the project, navigate to "build/libs" folder in the project, where JAR is generated and stored.
* Now, we can run the project using the  command
  **"java -jar dms-0.0.1-SNAPSHOT.jar"**
* Download and Install "Postman" REST client or any other REST client to access REST API endpoints the project using the link - https://www.postman.com/downloads/
* Once, we made sure that our server is up and running, we can access the REST API endpoints by following the documentation and demo video.
* Please find my postman collection in "resources/postman collection" folder, from here we can import the endpoints into the postman. we can follow the tutorial present in - https://youtube.com/clip/UgkxUyBDRO5Za8nX3HRdBecYAACByKgqTbX-
* Please find the Manual Testcases in "Results & Analysis" section of the documentation and please find the result screenshots in "resources/output" folder.

Increment 2-

* For increment 2 features, Please find my postman collection in "resources/postman collection" folder, from here we can import the endpoints into the postman. we can follow the tutorial present in - https://youtube.com/clip/UgkxUyBDRO5Za8nX3HRdBecYAACByKgqTbX-
* For increment 2 features, Please find the Manual Testcases in "Results & Analysis" section of the documentation and please find the result screenshots in "resources/output/increment 2" folder.
