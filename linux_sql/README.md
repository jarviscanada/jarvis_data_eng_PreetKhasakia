# Linux Cluster Monitoring Agent

# Introduction
The Jarvis Linux Cluster agent is built for the purpose of managing and monitoring Linux cluster of nodes or servers running CentOS 7. The servers are connected via a switch which allows each node to communicate with each other through internal IPv4 addresses. The LCA team requires data collection of hardware specifications and memory usage (e.g. CPU/Memory) in real-time. 

The design of this project consists of a psql instance using docker which is used to gather all data and is provisioned on our local machines. There is a bash agent that is installed on every node that would collect server usage data and inserts into the psql instance. 

Script "host_info.sh" is used to collect hardware specs and be inserted into the database. "host_usage" would collect CPU and memory data and stored in the database.

Technologies used for this project include:
- bash
- git
- docker
- psql
- linux
- crontab
- google cloud platform
- centos 7

# Quick Start
1. Start a psql instance using psql_docker.sh
```
./scripts/psql_docker.sh start|stop|create [db_username][db_password]
```
```
./scripts/psql_docker.sh create db_username db_password
```
```
./scripts/psql_docker.sh start
```
2. Create tables using ddl.sql
```
psql -h localhost -U postgres -d host_agent -f ddl.sql
```
3. Insert hardware specs data into the DB using host_info.sh
```
bash scripts/host_info.sh localhost 5432 host_agent postgres password
```
4. Insert hardware usage data into the DB using host_usage.sh
```
bash scripts/host_usage.sh localhost 5432 host_agent postgres password
```
5. Crontab setup
```
crontab -e
```
```
* * * * * bash /<file path>/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

# Implementation
The implementation was done by creating a psql instance and provisioning it through bash commands that allow to create a docker container and start it. This also required the use of "psql_docker.sh" which contains starting the docker, checking for arguments, and creating the container. 

Then comes the implementation of "ddl.sql". Here, I used the SQL to create two tables for host_info and host_usage. host_info stores hardware specifications and host_usage stores CPU and memory usage. Followed by a command that connects to the host_agent. 

Finally, the creation of host_info.sh and host_usage.sh would allow me to create the tables needed to store all specifications. 

# Architecture 
![image](https://user-images.githubusercontent.com/44883269/140006829-4d58624a-bf21-4222-afc3-7b09573fc92c.png)

#Scripts
psql_docker.sh: This script is used to initialize and run a docker container that allows a user to connect the an instance by specifying the database name and password.
```
./scripts/psql_docker.sh start|stop|create [db_username][db_password]
```
host_info.sh: This script contains commands that retrieve hardware specifications about the host, saved in a variable and allows the user to create the host_info table to store values in the database.

```
bash scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

host_usage.sh: This script is used to retrive CPU and memory usage of the host through bash commands saved in variables, along with creating a table for host_usage to store values in the database.
```
bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```

crontab: crontab is used to automate the host_usage.sh.
```
bash> crontab -e

#add this to crontab
* * * * * bash /<file path>/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```
queries.sql: Script for the LCA manager to better manage the cluster and future resoueces.

# Database Modeling
- `host_info`

| Name  | Type | Description |
| ----- | ---- | ----------- |
| id    | PRIMARY KEY     | This is the host id |
| hostname      | VARCHAR      | The name of the host  |
| cpu_number      | INTEGER     | Amount of CPUs available on the host  |
| cpu_architecture      | VARCHAR      | 64-bit instruction set |
| cpu_model      |  VARCHAR    |   Model of the cpu  |
| cpu_mhz      | FLOAT     |  Clock cycle in megahertz|
| L2_cache      | VARCHAR     |Known as Level 2 Cache, memory bank built into the CPU chip|
| total_mem      | INTEGER     | The total memory available|
| "timestamp"      | TIMESTAMP     | Used to display the time and date in UTC|

- `host_usage`

| Name  | Type | Description |
| ----- | ---- | ----------- |
| host_id        |     SERIAL REFERENCES host_info(id)   |The is the host id|
| memory_free  | INTEGER   | Memory that is free on the system   |
|cpu_idle   | INTEGER   | Idle CPUs that are not running any tasks   |
|cpu_kernel   | INTEGER   | The main component of the Linix OS and core interface between computer hardware and software |
|disk_io   | INTEGER   | Input/output operations for a hard disk   |
|disk_available   | INTEGER   |  The space available for a disk on the host  |

# Test
Testing was done through linux terminal on the gcp instance firstly by connecting to the psql instance. 
psql_docker.sh: This script was tested using the appropriate arguments of start, stop and create followed by database username and password.

host_info.sh and host_usage.sh: These scripts were tested by a bash command that would connect to the host_agent using localhost, port and postgres.

ddl.sql: This script was tested when creating the database in postgres then applying the "SELECT" function in host_agent which would retrieve table data.

# Deployment
The application is deployed through the gcp instance running CentOS 7.

# Improvements
- Improve scripts for hardware specs and CPU usage.
- Reduce the amount of bash scripts.
- Improve docker script.
