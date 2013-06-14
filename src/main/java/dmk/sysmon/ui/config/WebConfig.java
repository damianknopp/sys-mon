package dmk.sysmon.ui.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import dmk.sysmon.common.domain.SysEvent;
import dmk.sysmon.common.service.listener.EchoSysEventListener;
 
@Configuration
@EnableWebMvc
@ComponentScan(basePackages="dmk.sysmon")
public class WebConfig {
 
	/**
	 * 
	 * @return
	 */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver =
                    new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(2);
        return resolver;
    }
    
    @Bean
    public ContentNegotiatingViewResolver contentViewResolver(){
    	ContentNegotiatingViewResolver vr = new ContentNegotiatingViewResolver();
    	
    	Map<String, String> medias = new HashMap<>();
    	medias.put("htm", "text/html");
    	medias.put("html", "text/html");
    	medias.put("json", "application/json");
    	vr.setMediaTypes(medias);
    	
    	View jview = new MappingJacksonJsonView();
    	List<View> defaultViews = Arrays.asList(jview);
    	vr.setDefaultViews(defaultViews);
    	
    	vr.setIgnoreAcceptHeader(false);
    	
    	vr.setOrder(1);
//        InternalResourceViewResolver iresolver =
//                new InternalResourceViewResolver();
//        iresolver.setPrefix("/WEB-INF/view/");
//        iresolver.setSuffix(".jsp");
//	    List<ViewResolver> viewResolvers = Arrays.asList((ViewResolver)iresolver);
//    	vr.setViewResolvers(viewResolvers);
    	return vr;
    }
    
    @Bean(name="eventEngine")
    public EPServiceProvider eventEngine(){
    	com.espertech.esper.client.Configuration cepConfig = new com.espertech.esper.client.Configuration();
		cepConfig.addEventType("SysEvent", SysEvent.class.getName());
		EPServiceProvider epService = EPServiceProviderManager.getProvider(
				"myCEPEngine", cepConfig);
		EPAdministrator esperAdmin = epService.getEPAdministrator();
		final String epl = String
				.format("select * from SysEvent.win:time_batch(2 sec)");
		EPStatement eplStatement = esperAdmin.createEPL(epl);
		eplStatement.addListener(new EchoSysEventListener());
		return epService;
    }
}