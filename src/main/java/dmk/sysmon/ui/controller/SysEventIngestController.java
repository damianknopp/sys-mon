package dmk.sysmon.ui.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import dmk.sysmon.common.client.converter.CustomJsonMessageConverter;
import dmk.sysmon.common.domain.SysEvent;
import dmk.sysmon.common.service.SysEventService;

@Controller
@RequestMapping("/")
public class SysEventIngestController {
	protected static Logger logger = LoggerFactory
			.getLogger(SysEventIngestController.class);

	@Autowired
	SysEventService eventService;

	public SysEventIngestController() {
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = "*/*")
	public String index(ModelMap model) {
		if (logger.isDebugEnabled()) {
			logger.debug("in index controller");
		}
		return "index";
	}

	@RequestMapping(value = "/raw1", method = RequestMethod.GET)
	public ResponseEntity<String> raw1() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "application/json");
		return new ResponseEntity<String>("{ \"msg\": \"hello\"}", responseHeaders,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/raw2", method = RequestMethod.GET)
	public void raw2(final HttpServletResponse response) throws HttpMessageNotWritableException, IOException {
		ServletServerHttpResponse serverResponse = new ServletServerHttpResponse(response);
		HttpHeaders responseHeaders = serverResponse.getHeaders();
		responseHeaders.set("Content-Type", "application/json");
		CustomJsonMessageConverter msgConverter = new CustomJsonMessageConverter();
		SysEvent se = new SysEvent("ab" ,"c");
		msgConverter.write(se, MediaType.APPLICATION_JSON, serverResponse);
	}
	
	@RequestMapping(value = "/sys-event", method = RequestMethod.POST)
	public @ResponseBody
	SysEvent post(@RequestBody final SysEvent event) {
		if (logger.isTraceEnabled()) {
			logger.debug(event.toString());
		}
		this.eventService.ingest(event);
		return event;
	}

	public SysEventService getEventService() {
		return eventService;
	}

	public void setEventService(SysEventService eventService) {
		this.eventService = eventService;
	}
}