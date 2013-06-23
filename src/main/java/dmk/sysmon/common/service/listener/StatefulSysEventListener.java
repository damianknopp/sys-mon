package dmk.sysmon.common.service.listener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

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
public class StatefulSysEventListener<T> implements UpdateListener, StatefulMailbox<T> {
	protected Logger logger = LoggerFactory
			.getLogger(StatefulSysEventListener.class);

	protected final LinkedList<T> mailbox;
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

		final WriteLock lock = rwl.writeLock();
		try {
			lock.lock();
			for (final EventBean eb : newData) {
				final T se = (T)eb.getUnderlying();
//				final String msg = String.format(
//						"{ \"msg\": { \"type\": \"%s\", \"val\": \"%s\" } }", se.getEventType(),
//						se.getSysEvent());
//				final String msg = se.toJson();
				if(logger.isTraceEnabled()){
					logger.debug(se.toString());
				}
				mailbox.push(se);
				
				if(logger.isDebugEnabled()){
					logger.debug(mailbox.toString());
				}
			}
		}finally{
			if(lock != null){
				lock.unlock();
			}
		}
	}

	/**
	 * 
	 */
	public List<T> getMailboxMessages() {
		if(logger.isTraceEnabled()){
			logger.trace("getMailboxMessages");
		}
		final List<T> mbCopy = new LinkedList<>();
		final ReadLock lock = rwl.readLock();
		try{
			lock.lock();
			logger.trace("mailbox has "+ mailbox.size());
			mbCopy.addAll(mailbox);
			logger.trace("mb copy has "+ mbCopy.size());
		}finally{
			if(lock != null){
				lock.unlock();
			}
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