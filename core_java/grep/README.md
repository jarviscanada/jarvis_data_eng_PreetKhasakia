# Introduction
The design of the java grep app is to mimic Linux 'grep' command that is used to search and retrieve matching strings from files.
The app uses Maven standard directory layout to follow industry standard. The JavaGrep interface includes methods, as well as, getters and setters
that are needed for the main method that consists of the working code. The main method is the actual implementation of the java grep app
that is used to capture, read, write and execute commands for a given file as an input. Finally, the app is then dockerized to run with a docker container.
Technologies:
- Java
- Maven
- IntelliJ
- Lambda and Stream API
- Docker

# Quick Start
Running the java grep app:
1. Run javaGrepImp script on IntelliJ.
2. Compile and package the java code.
```
mvn clean compile package
```
3. Inspect compiled bytecode/.class files.
```
tree target
```
4. Inspect jar file.
```
jar -tf target/grep-1.0-SNAPSHOT.jar
```
5. Launch a JVM and run the app with Classpath and class files or with jar file.
```
#Approach 2: Jar file
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* ./data ./out/grep.txt
```
# Implementation
The implementation of the grep app starts by adding the correct maven dependencies/properties/plugins needed
to configure the project within IntelliJ. Followed by, a 'JavaGrep.java' interface script which contains methods such as
- process(): top level search workflow
- listFiles(): to traverse through a directory and return all files
- containsPattern(): check if a line contains the regex pattern (passed by user)
- readLines(): to read the given file and return its lines
- writeToFile(): used to write lines to the file

Next, the 'JavaGrepImp.java' script contains the main method and the working code for the above methods mentioned
for the app to run. 

The 'JavaGrepLambdaImp.java' script contains readLines() and listFiles() by using streams.
## Pseudocode
```
function process() 
matchedLines = []
files[] = listFiles(getRootPath())
for(file: files):
    for(line: readLines(file)):
        if(containsPattern(line))
            matchedLines.add(line)
writeToFile(matchedLines)
```

## Performance Issue
When running the grep app, we can also add additional JVM options to allocate how much memory we need to allocate 
to heap. For that, we have the options -Xms5m and -Xmx5m to specify minimum and maximum heap size respectively.
When you run the app with this command, you will see an OutOfMemoryError and to fix that, you can increase the 
maximum heap size.

# Test
For the grep app, I tested the methods in the scripts by using Eclipse IDE and using a test file and directory
to see weather each method performs its task on a given file or directory as an argument.

# Deployment
The grep app was deployed using docker.

# Improvement
- Improve the listFiles method
- Understanding more about Streams and Lambda
- Try to use case statements instead of if statements
