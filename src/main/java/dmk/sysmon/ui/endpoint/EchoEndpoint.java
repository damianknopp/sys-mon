package dmk.sysmon.ui.endpoint;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dmk.sysmon.common.service.listener.StatefulSysEventListener;

/**
 * Bare bone JSR 356 websocket
 * 
 * Once deployed, javascript connection in FF looks like this.
 * 
 * <code>
		var target = "ws://localhost:8080/tyrus-ws/echo"
		var ws = new WebSocket(target);
		console.log(ws) 
		
		ws.onmessage = function(event){
		    console.log('Received: ' + event.data)
		}
		ws.onopen = function(){
		    console.log("opened connection");
		    ws.send("hello echo!")
		}
		ws.onclose = function(){
		    console.log("closed connection")
		}
		
	once connected,
		ws.send("yeah boiie")
	and finally,
		ws.close()
 * </code>
 * @author dmknopp
 * 
 *
 */
@WebListener
@ServerEndpoint(value = "/echo")
public class EchoEndpoint implements ServletContextListener {
	private static final Logger logger = LoggerFactory
			.getLogger(EchoEndpoint.class);
	
	private ServletContext servletContext;
	
	public EchoEndpoint() {
		super();
		logger.info("newing up echo endpoint");
	}

	@OnMessage
	public String onMessage(String message, Session session) {
		if (logger.isDebugEnabled()) {
			logger.debug("endpoint recieved message = " + message);
		}
		return message;
	}

	@OnOpen
	public void onOpen(Session session)
			throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("endpoint connection opened " + session.getId());
		}
		session.getBasicRemote().sendText("{ \"msg\": \"hello. connection acknowledged.\" }");
		
		
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) throws IOException {
		// prepare the endpoint for closing.
		if (logger.isDebugEnabled()) {
			logger.debug("endpoint connection closed "
					+ reason.getReasonPhrase() + " " + session.getId());
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		logger.warn("websocket error for session " + session.getId() + " message:" + t.getMessage());
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext = servletContextEvent.getServletContext();
		registerWebSocketEndpoint();
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	public void registerWebSocketEndpoint(){
		Object serverContainer = null;
		serverContainer = servletContext.getAttribute("javax.websocket.server.ServerContainer");

		try {
			logger.info("is a jsr 356 compliant server? " + serverContainer);
			logger.info("instance of " + ServerContainer.class + " "
					+ (serverContainer.getClass().isAssignableFrom(ServerContainer.class)));

//			if (serverContainer == null) {
//				logger.warn("Cannot register "
//						+ this.getClass()
//						+ " websocket endpoint, because this server does not appear to be JSR 356 compliant.  Moving on...");
//				return;
//			}
			
			logger.debug("registering websock " + this.getClass());
			((javax.websocket.server.ServerContainer) serverContainer).addEndpoint(this.getClass());
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

}