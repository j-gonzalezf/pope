# Pope Project

_Java web application for the management and administration of training consultancies._

## Starting 🚀

These instructions will allow you to get a copy of the project running on your local machine for development and testing purposes.

### Requirements 📋

Software and tools you will need to run this project:
* Java 17 (_tested version_)
* Node v18.12.0 (_tested version_)
* yarn 1.22.19 (_tested version_)
* NetBeans (_Reccomended for backend developement_)
* VS Code (_Reccomended for frontend developement_)

### Local deployment 🔧

There are two ways to work in your local environment:

1. Spring-Boot app in port 8080 and React app in port 3000 (_Recommended for development_)
2. Both apps running in port 8080

Let's see how to work with each option.

_Note: both frontend and backend are configured to start on path /projectname. Moreover frontend subpaths follow this pattern: /projectname/#/subpath (due to deployment requirements it is necessary to use HashRouter instead of BrowserRouter)_

**1. Access frontend in port 3030 (_Reccomended for development_)**

* Install your maven project
    ```
    mvn clean install
    ```
* Start backend sever
    ```
    mvn spring-boot:run
    ```
* Install your frontend app (not necessary if mvn install was executed correctly)
    ```
    yarn install
    ```
* Start react app
    ```
    yarn start
    ```

Now you can test your app in http://localhost:3000/pope

**2. Access frontend in port 8080**

* Install your maven project
    ```
    mvn clean install
    ```
* Start the application
    ```
    mvn spring-boot:run
    ```
Thanks to the provided plugin configuration you can access your React app directly on http://localhost:8080/pope

## Deployment 📦


## Built with 🛠️

* Backend - [Spring-Boot](https://spring.io/projects/spring-boot)
* User Interface - [React](https://es.reactjs.org/)
* Project Management - [Maven](https://maven.apache.org/) & [yarn](https://yarnpkg.com/)
* Deployment - 

## Extra tools ⚙️

* Version Control - [GitHub](https://github.com/j-gonzalezf/pope)
* Issue Tracking - [Jira](https://j-gonzalezf.atlassian.net/jira/software/projects/SCRUM/boards/1)
* Continuous Integration - 
* Continous Inspection - [Snyk] (https://app.snyk.io/org/j-gonzalezf)

## Author ✒️

* **Jorge González**
