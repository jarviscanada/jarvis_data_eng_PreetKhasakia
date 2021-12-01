#!/bin/sh

#Arguments
cmd=$1
db_username=$2
db_password=$3

#Start docker
sudo systemctl status docker || systemctl start docker

#Checking container status and case
docker container inspect jrvs-psql
container_status=$?

case $cmd in
create)
	#Chcecking if container exists
	if [$container_status -eq 0]; then
		echo 'Container already exists'
		exit 1
	fi
	
	#Checking arguments
	if [$# -ne 3]; then
		echo 'Create requires username and password'
		exit 1
	fi

	#Create docker
	
	docker volume create pgdata
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

	
	
	exit$?
	;;
	
	
	start|stop)
	
	#Check if container does not exist
	if [ $container_status -ne 0]; then
		echo 'Container does not exist'
		exit 1
	fi
	
	#Start/Stop the container
	docker container $cmd jrvs-psql
	exit $?
	;;	
  
	*)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac 	

