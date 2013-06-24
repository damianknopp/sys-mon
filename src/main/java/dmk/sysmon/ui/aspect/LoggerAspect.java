package dmk.sysmon.ui.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dmknopp
 *
 */
@Aspect
public class LoggerAspect {
	private static final Logger logger = LoggerFactory
			.getLogger(LoggerAspect.class);

	/**
	 * 
	 * @param joinPoint
	 */
//	@After("execution(* dmk.sysmon.ui.endpoint.SysMonEndpoint.onMessage(..))")
	@After("execution(* dmk.sysmon.ui.controller.SysEventIngestController.post(..))")
	public void logAfter(JoinPoint joinPoint) {
		logger.debug("called" + joinPoint.getSignature().getName());
	}

}