# Advanced Algorithmic Paris Subway Graph Project

Graph based application built to work with the Paris network for an algorithmic team project.

## Usage

### Prerequisites

#### Java

You must have Java 8 (or higher) installed on your machine to use this project.

You can check your java version with `java --version`.

If you don't have it already, you can get it by following those steps:

- Download a Java JDK binary [here](https://jdk.java.net/8/)
- Extract it and add jdk-8.x.x/bin to your PATH
- Create a JAVA_HOME variable set to the jdk directory path

#### Apache Maven

You must also have Maven 3 (or higher) installed on your machine to use this project.

You can check your maven version with `mvn -v`.

If you don't have it already, you can get it by following those steps:

- Download a Maven 3 binary [here](https://maven.apache.org/download.cgi)
- Extract it and add apache-maven-3.x.x/bin to your PATH

### Building/Rebuilding the project

```bash
mvn clean install
```

### Running the application

```bash
mvn exec:java -q
```
