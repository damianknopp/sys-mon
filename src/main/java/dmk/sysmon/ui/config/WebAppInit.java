package dmk.sysmon.ui.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInit implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WebConfig.class);
		// Manage the lifecycle of the root application context
		container.addListener(new ContextLoaderListener(rootContext));

//    	AnnotationAwareAspectJAutoProxyCreator proxy = new AnnotationAwareAspectJAutoProxyCreator();
//    	proxy.setProxyTargetClass(true);

    	// Create the dispatcher servlet's Spring application context
//		AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
//		dispatcherContext.register(DispatcherConfig.class);

		DispatcherServlet dispatcherServlet = new DispatcherServlet(rootContext);
		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic dispatcher = container.addServlet(
				"dispatcher", dispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/api/*");
	}

}