package dmk.sysmon.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value="/index", method = RequestMethod.GET, produces = "*/*")
    public String index(ModelMap model) {
    	if(logger.isDebugEnabled()){
    		logger.debug("in index controller");
    	}
    	return "index";
    }
    
    @RequestMapping(value="/sys-event", method = RequestMethod.POST)
    public @ResponseBody SysEvent post( @RequestBody final  SysEvent event) {
    	if(logger.isTraceEnabled()){
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