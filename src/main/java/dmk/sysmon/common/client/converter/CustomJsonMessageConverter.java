package dmk.sysmon.common.client.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import dmk.sysmon.common.domain.SysEvent;

public class CustomJsonMessageConverter implements HttpMessageConverter<SysEvent> {
	protected Logger logger = LoggerFactory
			.getLogger(CustomJsonMessageConverter.class);

	public CustomJsonMessageConverter(){
		super();
		logger.debug("new custom message converter");
	}
	
    public List<MediaType> getSupportedMediaTypes() {
    	logger.debug("supported media types");
    	return Arrays.asList(new MediaType("application", "json"),
        		MediaType.APPLICATION_JSON);
    }
 
    public boolean supports(Class<? extends SysEvent> clazz) {
    	logger.debug("supports " + clazz);
        return SysEvent.class.equals(clazz);
    }
 
    public SysEvent read(Class<? extends SysEvent> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    	InputStream is = inputMessage.getBody();
    	JsonFactory factory = new JsonFactory();
    	ObjectMapper mapper = new ObjectMapper(factory);
    	final String json = IOUtils.toString(is);
    	
    	logger.debug("found string " + json);
    	if(logger.isTraceEnabled()){
    	}
    	
    	final JsonNode jn = mapper.readTree(json);
    	
    	final String et = jn.has(KEYS.EVENT_TYPE.getVal()) ? 
    			jn.get(KEYS.EVENT_TYPE.getVal()).getTextValue() : "";
    	final String se = jn.has(KEYS.SYS_EVENT.getVal()) ? 
    			jn.get(KEYS.SYS_EVENT.getVal()).getTextValue() : "";

    	return new SysEvent(et, se);
    }
 
	public void write(SysEvent se, MediaType arg1, HttpOutputMessage out)
			throws IOException, HttpMessageNotWritableException {
		logger.debug("writing se " + se);
    	JsonFactory factory = new JsonFactory();
    	ObjectMapper mapper = new ObjectMapper(factory);
    	mapper.writeValue(out.getBody(), se);
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType media) {
		final boolean b = SysEvent.class.equals(clazz) || SysEvent.class.isAssignableFrom(clazz);
		logger.debug("canRead " + clazz + " " + media + " " + b);
		return b;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType media) {
		logger.debug("canWrite " + clazz + " media " + media);
		if(logger.isTraceEnabled()){
			
		}
		boolean isJson = media.isCompatibleWith(MediaType.APPLICATION_JSON);
		boolean canWrite = isJson && (SysEvent.class.equals(clazz) || SysEvent.class.isAssignableFrom(clazz));
				//|| String.class.equals(clazz));
		logger.debug("canWrite " + canWrite);
		return canWrite;
	}

	private enum KEYS{
		EVENT_TYPE("eventType"), SYS_EVENT("sysEvent");
		
		private String value;
		
		private KEYS(String s){
			value = s;
		}
		
		public String getVal(){ return value; }
	}
}