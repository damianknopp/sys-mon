package dmk.sysmon.ui.aspect;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dmk.sysmon.common.domain.SysEvent;

/**
 * 
 * @author dmknopp
 * 
 */
@Aspect
public class EventTypeCounterAspect {
	private static final Logger logger = LoggerFactory
			.getLogger(EventTypeCounterAspect.class);
	
	private static AtomicLong numberOfSummaryEvents = new AtomicLong(0);
	private static AtomicLong numberOfViewEvents = new AtomicLong(0);
	private static AtomicLong numberOfCreatedEvents = new AtomicLong(0);
	
	/**
	 * 
	 * @param joinPoint
	 */
	@After("execution(* dmk.sysmon.ui.controller.SysEventIngestController.post(..))")
	public void logAfter(JoinPoint joinPoint) {
		final Object[] objs = joinPoint.getArgs();
		if (logger.isTraceEnabled()) {
			logger.trace("called " + joinPoint.getSignature().getName());
			logger.debug(Arrays.toString(objs));
		}


		for (int i = 0; i < objs.length; i++) {
			final SysEvent se = (SysEvent) objs[i];
			String eventType = se.getEventType() != null ? se
					.getEventType().toLowerCase() : "";
			long num = 0;
			switch (eventType) {
			case "summary":
				num = numberOfSummaryEvents.addAndGet(1);
				break;
			case "view":
				num = numberOfViewEvents.addAndGet(1);
				break;
			case "created":
				num = numberOfCreatedEvents.addAndGet(1);
				break;
			default:
				break;
			}
			final String msg = String.format("action:%d count:%s", num, eventType);
			logger.info(msg);
		}
	}

}