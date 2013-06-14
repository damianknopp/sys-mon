#!/bin/bash

if [ -z "$1" ]
then
	echo "Usage: `basename $0` <number of messages to send>"
	exit $E_NOARGS
fi

END=$1

for((i=0; i<$END; i++))
do
	DATA="{\"eventType\": \"view\", \"sysEvent\": \"e${i}\" }"
	echo "curl -X POST http://localhost:8080/sys-mon/api/sys-event -d '$DATA' -H \"Accept: application/json\" -H \"Content-Type: application/json\""
	curl -X POST http://localhost:8080/sys-mon/api/sys-event -d "$DATA" -H "Accept: application/json" -H "Content-Type: application/json"
done
