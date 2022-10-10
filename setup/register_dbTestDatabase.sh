#!/bin/bash

nr=${1:-5}

serverName=db
portNumber=5432

export CONNECTION_STRING="user=database:password=database:databaseName=database:serverName=${serverName}:portNumber=${portNumber}"

cat <<EOF|grep -v "^#" | asadmin --port=${nr}4848
create-jdbc-connection-pool --datasourceclassname org.postgresql.xa.PGXADataSource --restype javax.sql.XADataSource --property ${CONNECTION_STRING} dbTestDatabase
create-jdbc-resource --connectionpoolid dbTestDatabase jdbc/dbTestDatabase
EOF
