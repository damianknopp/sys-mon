package dmk.sysmon.common.service.listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * simple stateful listener, store in a mail box when the listener fires
 * 
 * @author dmknopp
 */
@Component("statefulSysEventListener")
public class StatefulSysEventListener implements UpdateListener, StatefulMailbox {
	protected Logger logger = LoggerFactory
			.getLogger(StatefulSysEventListener.class);

	protected final LinkedList<String> mailbox;
	protected final ReentrantReadWriteLock rwl;

	/**
	 * 
	 */
	public StatefulSysEventListener() {
		super();
		mailbox = new LinkedList<>();
		rwl = new ReentrantReadWriteLock();
	}

	/**
	 * echo to logger on event fire
	 */
	public void update(final EventBean[] newData, final EventBean[] oldData) {
		if (logger.isInfoEnabled()) {
			logger.info(String.format("Num events recieved (%d)",
					newData.length));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("1st event received: " + newData[0].getUnderlying());
		}

		try {
			rwl.writeLock().lock();
			for (final EventBean eb : newData) {
				final String msg = String.format(
						" \"msg\": { \"%s\": \"%s\" } ", eb.getEventType(),
						eb.getUnderlying());
				logger.debug(msg);
				if(logger.isTraceEnabled()){
				}
				mailbox.push(msg);
			}
		}finally{
			rwl.writeLock().unlock();
		}
	}

	/**
	 * 
	 */
	public List<String> getMailboxMessages() {
		if(logger.isTraceEnabled()){
			logger.trace("getMailboxMessages");
		}
		final List<String> mbCopy = new LinkedList<>();
		rwl.readLock().lock();
		try{
			Collections.copy(mbCopy, mailbox);
		}finally{
			rwl.readLock().unlock();
		}
		return mbCopy;
	}

	/**
	 * 
	 */
	public void clearMailbox() {
		if(logger.isDebugEnabled()){
			logger.debug("clear mail box");
		}
		rwl.writeLock().lock();
		try{
			mailbox.clear();
		}finally{
			rwl.writeLock().unlock();
		}
	}
}