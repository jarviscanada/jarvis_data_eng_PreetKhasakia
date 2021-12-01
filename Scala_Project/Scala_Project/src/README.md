# Scala Mini Project

# Introduction
This project consists of a Scala-Spark program that takes CLI arguments 
of two hive tables, each with the same columns, and performs checks to 
confirm that both tables have the same content. The tables "src" and "backup"
have four columns "foo" type STRING, "bar" type LONG, "baz" type BIGDECIMAL, and "part" PARTITION
type STRING. 

If a "part" partition changes in the src table, that data needs to be copied in the 
backup table. This will overwrite backup's part partition.

Technologies used:
- Scala
- Maven
- IntelliJ
- Spark
- Hive
- Scalatest

# Implemention
The project uses Maven which includes all the dependencies needed to run the scala script.
Main dependencies:
- spark-core
- spark-hive
- spark-sql
- scalatest

Since the project requires CLI arguments, in IntelliJ, we can add user inputs from the 
"Edit Configuration..." tab. The script will start by creating a spark session to initialize spark. It will help in creating
a name for the app, builder pattern method, getorcreate method and HiveContext to enable access to Hive. The program takes the user input and creates a "src" and 
"backup" table if they do not exist.

The data is inserted using the spark.sql library for both tables and each data type is
also defined. 

The script in the end overwrite the partition part of backup table when a change 
occurs in the src table, with an <INSERT OVERWRITE> command.

# Testing
Majority of testing was done through IntelliJ just within the main script itself through multiple runs of the script.
I have attempted to test Hive table commands with HiveServer using a java class.