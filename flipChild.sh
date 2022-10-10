#!/bin/bash

test=$(dodump "hanschild" -w "id='${childId}'" -n -notable test)

case "${test}" in
	test1)
		test="test2"
	;;
	test2)
		test="test1"
	;;
esac

echo "Update 'test' to '${test}'"
dodump -e "update hanschild set test='${test}' where id='${childId}'"

