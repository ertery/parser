#!/bin/bash

usage()
{
cat << EOF
usage: $0 options

It's script that starts spring boot application from jar file that synchronize data in db.

OPTIONS:
   -h      Show this message
   -f      file name
   -o      operation type [load] or [sync]
   -d      run docker compose if it's not
EOF
}

while getopts “ho:f:d” OPTION
do
     case $OPTION in
         h)
             usage
             exit 1
             ;;
         f)
             FILE_NAME=${OPTARG}
             ;;
         o)
             OPERATION=${OPTARG}
              ;;
         d)
             DOCKER_COMPOSE_UP=1
             ;;
         ?)
             usage
             exit
             ;;
     esac
done

SERVICE=$(docker-compose ps --services --filter "status=running" | grep postgres)

if [[ ! -z ${DOCKER_COMPOSE_UP} ]] && [[ ${DOCKER_COMPOSE_UP} -ge 0 ]] && [[ -z ${SERVICE} ]];
    then
        ECHO "docker-compose up"
        docker-compose -f docker-compose.yaml up -d
        sleep 3
fi

java -jar parser-1.0.0.jar $OPERATION $FILE_NAME

