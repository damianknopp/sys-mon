#!/bin/bash
#mvn -Dmaven.test.skip=true clean install && cp target/sys-mon.war ~/tomcat8.dev/webapps/.
cake build && mvn clean install && cp target/sys-mon.war ~/tomcat8.dev/webapps/.
