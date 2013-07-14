#!/bin/bash

echo "./post-event.sh create 10 20 && sleep 3 && ./post-event.sh view 10 15"
./post-event.sh create 10 20 && sleep 3 && ./post-event.sh view 10 15

