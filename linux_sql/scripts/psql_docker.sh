#!/bin/sh

su centos

cmd=$1
db_username=$2
db_password=$3

sudo systemctl status docker || systemctl start docker

docker container inspect jrvs-psql
container_status=$?

case $cmd in
create)

if [$container_status -eq 0]; then
	echo 'Container already exists'
	exit 1
fi

if [$# -ne 3]; then
	echo 'Create requires username and password'
	exit 1
fi

if [["$exists" == 2]]; then
	echo 'Container exists!'
else
	
	docker pull postgres
	docker volume create pgdata
	export PGPASSWORD='password'
	docker run --name jrvs-psql -e POSTGRES_PASSWORD=$PGPASSWORD -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:9.6-alpine

	#docker container ls -a -f name=jrvs-psql
	docker ps -f name=jrvs-psql
	sudo yum install -y postgresql
	psql -h localhost -U postgres -d postgres -W 
fi
exit$?
;;

start|stop)

if [ $container_status -eq 1]; then
	echo 'Container does not exist'
	exit 1
fi

docker container $cmd jrvs-psql
exit $?
;;	
  
*)
echo 'Illegal command'
echo 'Commands: start|stop|create'
exit 1
;;
esac 	

