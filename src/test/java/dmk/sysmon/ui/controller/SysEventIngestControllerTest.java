package dmk.sysmon.ui.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dmk.sysmon.common.client.converter.CustomJsonMessageConverter;
import dmk.sysmon.common.domain.SysEvent;

/**
 * Integration test, have to deploy to tomcat8, then test
 * @author dmknopp
 *
 */
public class SysEventIngestControllerTest {
	protected static Logger logger = LoggerFactory
			.getLogger(SysEventIngestControllerTest.class);

	
	RestTemplate rt = null;
	
	@Before
	public void setup() {
		rt = new RestTemplate();
		
		final StringHttpMessageConverter stringMesgConverter = new StringHttpMessageConverter();
		stringMesgConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
		final List<HttpMessageConverter<?>> mc = new ArrayList<>();
		mc.add(stringMesgConverter);
		mc.add(new CustomJsonMessageConverter());
		rt.setMessageConverters(mc);

	}

	@After
	public void after() {

	}

	@Test
	public void testPost() {
		SysEvent seReq = new SysEvent("view", "e99");
		final String e1 = String.format("{\"eventType\": \"%s\", \"sysEvent\": \"%s\" }",
				seReq.getEventType(), seReq.getSysEvent());
		
		final String url= "http://localhost:8080/sys-mon/api/sys-event";
		final HttpHeaders reqHeaders = new HttpHeaders();
		reqHeaders.add("Accept", "application/json");
		reqHeaders.add("Content-Type", "application/json");
		final HttpEntity<?> entity = new HttpEntity<Object>(e1, reqHeaders);
		final ResponseEntity<SysEvent> re = rt.exchange(url, HttpMethod.POST, entity, SysEvent.class);
		final SysEvent seResp = re.getBody();
		assertNotNull(seResp);
		logger.debug(seResp.toString());
		
		assertNotNull(seResp.getEventType());
		assertNotNull(seResp.getSysEvent());
		assertEquals(seResp.getEventType(), "view");
		assertEquals(seResp.getSysEvent(), "e99");
		
//		final SysEvent se = rt.postForObject(url, e1, SysEvent.class);
	}
}
