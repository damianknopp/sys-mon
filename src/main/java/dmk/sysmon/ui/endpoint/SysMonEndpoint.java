package dmk.sysmon.ui.endpoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import dmk.sysmon.common.service.listener.StatefulSysEventListener;

/**
 * Bare bone JSR 356 websocket
 * 
 * Once deployed, javascript connection in FF looks like this.
 * 
 * @author dmknopp
 */
@WebListener
@ServerEndpoint(value = "/events")
public class SysMonEndpoint implements ServletContextListener {
	private static final Logger logger = LoggerFactory
			.getLogger(SysMonEndpoint.class);

//	@Autowired
//	@Qualifier("statefulSysEventListener")
	private StatefulSysEventListener statefulSysEventListener;
	private ServletContext servletContext;

	public SysMonEndpoint() {
		super();
		logger.info("newing up sysmon endpoint");
	}
	
	public void checkForListener(){
		if(this.statefulSysEventListener == null){
			WebApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
			logger.debug("appCtx " + appCtx);
			this.statefulSysEventListener = (StatefulSysEventListener)appCtx.getBean("statefulSysEventListener");
			logger.debug("listener = " + statefulSysEventListener);
		}
	}
	
	@OnMessage
	public String onMessage(String message, Session session)
			throws JsonGenerationException, JsonMappingException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("websocket " + session.getId() + " recieved " + message);
		}
		if(this.statefulSysEventListener == null){
			logger.debug("listener is null!");
		}
		checkForListener();
		final List<String> messages = this.statefulSysEventListener
				.getMailboxMessages();

		if (logger.isDebugEnabled()) {
			logger.debug(messages.size() + " messages");
		}
		this.statefulSysEventListener.clearMailbox();
		if (logger.isDebugEnabled()) {
			logger.debug("cleared mail box - " + messages.size() + " messages");
		}

		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(out, messages);
		final byte[] data = out.toByteArray();
		final String json = new String(data);

		if (logger.isDebugEnabled()) {
			logger.debug("json = " + json);
		}
		return json;
	}

	@OnOpen
	public void onOpen(Session session) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("endpoint connection opened " + session.getId());
		}
		session.getBasicRemote().sendText(
				"{ \"msg\": \"hello. connection acknowledged.\" }");

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
		logger.warn("websocket error for session " + session.getId()
				+ " message:" + t.getMessage());
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		servletContext = servletContextEvent.getServletContext();
		registerWebSocketEndpoint();
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void registerWebSocketEndpoint() {
		Object serverContainer = null;
		serverContainer = servletContext
				.getAttribute("javax.websocket.server.ServerContainer");

		try {
			logger.info("is a jsr 356 compliant server? " + serverContainer);
			logger.info("instance of "
					+ ServerContainer.class
					+ " "
					+ (serverContainer.getClass()
							.isAssignableFrom(ServerContainer.class)));

			// if (serverContainer == null) {
			// logger.warn("Cannot register "
			// + this.getClass()
			// +
			// " websocket endpoint, because this server does not appear to be JSR 356 compliant.  Moving on...");
			// return;
			// }

			logger.debug("registering websock " + this.getClass());
			((javax.websocket.server.ServerContainer) serverContainer)
					.addEndpoint(this.getClass());
		} catch (DeploymentException e) {
			e.printStackTrace();
		}
	}

}