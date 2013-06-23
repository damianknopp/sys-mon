#!/bin/bash

if [ [-z "$1"] && [-z "$2"] && [-z "$3"] ]
then
	echo "Usage: `basename $0` <[create|view|summary]> <start count> <end count>"
	exit $E_NOARGS
fi

END=$3
START=$2
STYPE=$1

for((i=$START; i<=$END; i++))
do
	DATA="{\"eventType\": \"${STYPE}\", \"sysEvent\": \"e${i}\" }"
	echo "curl -X POST http://localhost:8080/sys-mon/api/sys-event -d '$DATA' -H \"Accept: application/json\" -H \"Content-Type: application/json\""
	curl -X POST http://localhost:8080/sys-mon/api/sys-event -d "$DATA" -H "Accept: application/json" -H "Content-Type: application/json"
done
