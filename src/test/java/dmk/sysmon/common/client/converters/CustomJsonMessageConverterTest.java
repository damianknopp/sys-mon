package dmk.sysmon.common.client.converters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Image;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import dmk.sysmon.common.client.converter.CustomJsonMessageConverter;
import dmk.sysmon.common.domain.SysEvent;

public class CustomJsonMessageConverterTest {
	CustomJsonMessageConverter jc = null;
	
	@Before public void setup(){
		jc = new CustomJsonMessageConverter();
	}
	
	@After public void after(){ jc = null; }
	
	@Test public void read1(){
		assertTrue(jc.canRead(SysEvent.class, MediaType.APPLICATION_JSON));
	}
	
	@Test public void read2(){
		assertFalse(jc.canRead(Image.class, MediaType.APPLICATION_JSON));
	}
	
	@Test public void write1(){
		assertTrue(jc.canWrite(SysEvent.class, MediaType.APPLICATION_JSON));
	}
	
	@Test public void write2(){
		assertTrue(jc.canWrite(SysEvent.class, MediaType.APPLICATION_JSON));
	}
}