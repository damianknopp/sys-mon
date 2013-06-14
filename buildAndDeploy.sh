#!/bin/bash
mvn clean install && cp target/sys-mon.war ~/tomcat8.dev/webapps/.
