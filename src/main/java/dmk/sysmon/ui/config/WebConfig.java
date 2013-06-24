package dmk.sysmon.ui.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

import dmk.sysmon.common.client.converter.CustomJsonMessageConverter;
import dmk.sysmon.common.domain.SysEvent;
import dmk.sysmon.ui.aspect.EventTypeCounterAspect;
import dmk.sysmon.ui.aspect.LoggerAspect;
 
@Configuration
@EnableWebMvc
@ComponentScan(basePackages="dmk.sysmon")
@EnableAspectJAutoProxy
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
    
    @Bean(name="epService")
    public EPServiceProvider eventEngine(){
    	com.espertech.esper.client.Configuration cepConfig = new com.espertech.esper.client.Configuration();
		cepConfig.addEventType("SysEvent", SysEvent.class.getName());
		EPServiceProvider epService = EPServiceProviderManager.getProvider(
				"myCEPEngine", cepConfig);
		return epService;
    }
    
    @Bean
    public AnnotationMethodHandlerAdapter annotationMethodHandlerAdapter(){
    	AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
    	final CustomJsonMessageConverter mc = messageConverter();
    	HttpMessageConverter<?>[] messageConverters = new HttpMessageConverter<?>[1];
    	messageConverters[0] = mc;
    	adapter.setMessageConverters(messageConverters);
    	return adapter;
    }
    
    @Bean 
    public CustomJsonMessageConverter messageConverter(){
    	return new CustomJsonMessageConverter();
    }
    
    @Bean
    public EventTypeCounterAspect ingestAspect(){
    	return new EventTypeCounterAspect();
    }
    
    /**
     * same as <aspectj-autoproxy/>
     * notice the static keyword in the method to work 
     * nice as a BeanPostProcessor
     * @return
     */
//    @Bean
//    public static AnnotationAwareAspectJAutoProxyCreator aspectj(){
//    	AnnotationAwareAspectJAutoProxyCreator proxy = new AnnotationAwareAspectJAutoProxyCreator();
//    	proxy.setProxyTargetClass(true);
//    	proxy.setInterceptorNames(new String[]{ "loggerAspect" });
//    	proxy.setIncludePatterns(Arrays.asList(""));
//    	return proxy;
//    }
}