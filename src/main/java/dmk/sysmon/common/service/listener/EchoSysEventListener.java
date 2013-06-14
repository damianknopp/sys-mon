package dmk.sysmon.common.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * simple echo when the listener fires
 * @author dmknopp
 */
public class EchoSysEventListener implements UpdateListener {
	protected Logger logger = LoggerFactory.getLogger(EchoSysEventListener.class);

	/**
	 * echo to logger on event fire
	 */
	public void update(EventBean[] newData, EventBean[] oldData) {
		if(logger.isInfoEnabled()){
			logger.info(String.format("Num events recieved (%d)", newData.length));
		}
		if(logger.isDebugEnabled()){
			logger.debug("1st event received: " + newData[0].getUnderlying());
		}
	}
}