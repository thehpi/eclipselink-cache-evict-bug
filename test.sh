#!/bin/bash

export application="evict-bug"

if [ -z "${id}" ]
then
	echo "ERROR: the environment variable 'id' should be set" >&2
	exit 1
fi

if [ -z "${childId}" ]
then
	echo "ERROR: the environment variable 'childId' should be set" >&2
	exit 1
fi

echo "Get Hans"
./getHans.sh

./flipChild.sh

echo "Get Hans, 'test' should be the same"
result=$(./getHans.sh)
echo "${result}"
v1=$(echo "${result}" | jq -r '.test')

echo "Evict child: $(./evictChild.sh)"

echo "Get Hans, 'test' should be changed"
result=$(./getHans.sh)
echo "${result}"
v2=$(echo "${result}" | jq -r '.test')

if [ "$v1" = "$v2" ]
then
	echo "This is the actual problem: expected different values" >&2
	echo v1:$v1
	echo v2:$v2
else
	echo "YAAY, it works! Is the problem solved?"
	exit
fi

echo "Evict hans: $(./evictHans.sh)"

echo "Get Hans, now test should be changed"
result=$(./getHans.sh)
echo "${result}"
v3=$(echo "${result}" | jq -r '.test')

if [ "$v1" = "$v3" ]
then
	echo "WTF!: after parent evict things should be ok, this is a new problem??" >&2
	echo v1:$v1
	echo v2:$v2
else
	echo "INDEED, now it works (but the problem still exists)!"
	exit
fi

