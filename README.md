sys-mon
====================

To build: 
./buildAndDeploy.sh

To run: 
cd ~/tomcat8 && ./bin/catalina.sh run

look in the logs for a successful start, ie no exceptions and 
INFO  org.springframework.web.servlet.DispatcherServlet - FrameworkServlet 'dispatcher': initialization started
INFO  org.springframework.web.servlet.DispatcherServlet - FrameworkServlet 'dispatcher': initialization completed in 32 ms
Jun 23, 2013 8:37:21 PM org.apache.catalina.startup.HostConfig deployDirectory
Jun 23, 2013 8:37:21 PM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["http-nio-8080"]
Jun 23, 2013 8:37:21 PM org.apache.coyote.AbstractProtocol start
INFO: Starting ProtocolHandler ["ajp-nio-8009"]
Jun 23, 2013 8:37:21 PM org.apache.catalina.startup.Catalina start
INFO: Server startup in 8171 ms


Visit: http://localhost:8080/sys-mon/index.jsp
select a refresh rate

Ingest data:
./post-event.sh view 10 100

You should see events come over on the browser

  

