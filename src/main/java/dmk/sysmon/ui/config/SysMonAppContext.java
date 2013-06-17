package dmk.sysmon.ui.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SysMonAppContext implements ApplicationContextAware{

	ApplicationContext appCtx;
	
	SysMonAppContext(){
		super();
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

}
