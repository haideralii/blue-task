#Bilue Assignment

This repository contains a test suite for testing the login functionality of a mobile application using Appium and TestNG.

Prerequisites
To run the tests in this repository, you need to have the following prerequisites installed:

 - Appium
 - TestNG
 - Java Development Kit (JDK)
 - Android SDK
 - Maven

Make sure you have the above dependencies installed and properly configured before running the tests.

after installing all the required dependencies, you need to have appium server up and running and emulator open then 
you can use following command, that will execute tests 
      
      mvn test

This command will execute tests in android as well as iOS. As there is no .ipa application wo iOS tests willl get failed and you can view results in surefire report in target/index.html
