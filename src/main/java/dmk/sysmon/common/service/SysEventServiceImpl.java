package dmk.sysmon.common.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;

import dmk.sysmon.common.domain.SysEvent;
import dmk.sysmon.common.service.listener.StatefulSysEventListener;

/**
 * 
 * @author dmknopp
 *
 */
@Service
public class SysEventServiceImpl implements SysEventService {
	protected static Logger logger = LoggerFactory
			.getLogger(SysEventServiceImpl.class);

	@Autowired
	private EPServiceProvider epService;
	
	@Autowired
	private StatefulSysEventListener<SysEvent> statefulEchoListener;
	
	public SysEventServiceImpl(){
		super();
	}
	
    @PostConstruct
	public void registerListener(){
		EPAdministrator esperAdmin = epService.getEPAdministrator();
		final String epl = String
				.format("select * from SysEvent.win:time_batch(2 sec)");
		EPStatement eplStatement = esperAdmin.createEPL(epl);
		eplStatement.addListener(statefulEchoListener);
	}

	public SysEventService ingest(final SysEvent e){
		if(logger.isDebugEnabled()){
			logger.debug("ingesting event " + e.toString());
		}
		final EPRuntime esperRuntime = epService.getEPRuntime();
		esperRuntime.sendEvent(e);
		return this;
	}
}
