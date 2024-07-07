#!/usr/bin/env bash

SCRIPT_HOME=$(cd $(dirname $0) && pwd)
cd $SCRIPT_HOME

cd ./base
../mvnw clean install
cd ..

cd ./api
../mvnw quarkus:dev
cd ..

