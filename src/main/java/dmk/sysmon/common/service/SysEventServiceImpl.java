package dmk.sysmon.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;

import dmk.sysmon.common.domain.SysEvent;

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
	
	public SysEventServiceImpl() {
		super();
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
