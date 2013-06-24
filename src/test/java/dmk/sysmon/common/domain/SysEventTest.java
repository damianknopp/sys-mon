package dmk.sysmon.common.domain;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SysEventTest {
	protected static Logger logger = LoggerFactory
			.getLogger(SysEventTest.class);
	
	SysEvent se1 = null;
	SysEvent se2 = null;
	SysEvent se3 = null;

	@Before
	public void setup(){
		se1 = new SysEvent("view", "page1");
		se2 = new SysEvent("view", "page2");
		se3 = new SysEvent("summary", "page1");
	}
	@Test
	public void toJsonTest() {
		SysEvent se = new SysEvent("e1", "s2");
		String json = se.toJson();
		logger.debug(json);
		assertNotNull(json);
		assertTrue(json.contains("e1"));
		assertTrue(json.contains("s2"));
	}
	
	@Test
	public void eqTest(){
		final SysEvent se1 = new SysEvent("view", "page1");
		final SysEvent se2 = new SysEvent("view", "page1");
		assertTrue(se1.equals(se2));
		assertTrue(se2.equals(se1));
		assertThat(se1, is(se1));
	}
	
	@Test
	public void notEqTest(){
		assertFalse(se2.equals(se1));

		assertThat(se1, is(se1));
		assertThat(se1, is(not(se2)));
		assertThat(se1, is(not(se3)));
	}
	
	@Test
	public void collectionsHas(){
		final List<SysEvent> list = Arrays.asList(se1, se2, se3);
		assertThat(list.size(), is(3));
		assertThat(list, hasItem(se1));
		assertThat(list, hasItem(se2));
		assertThat(list, hasItem(se3));
	}

	@Test
	public void collections(){
		final LinkedList<SysEvent> list = new LinkedList<SysEvent>();
		list.add(se1);
		list.add(se2);
		list.add(se3);

		assertThat(list.size(), is(3));
		final LinkedList<SysEvent> copy = new LinkedList<SysEvent>();
		assertThat(copy.size(), is(0));
		copy.addAll(list);
		assertThat(copy.size(), is(3));

		if(logger.isDebugEnabled()){
			logger.debug("list = " + list.toString());
			logger.debug("copy = " + copy.toString());

		}
		assertThat(list, hasItem(new SysEvent("view", "page1")));
		assertThat(list, hasItem(se2));
		assertThat(list, hasItem(se3));
		assertThat(copy, hasItem(se1));
		assertThat(copy, hasItem(se2));
		assertThat(copy, hasItem(se3));

		list.clear();
		assertThat(copy, hasItem(new SysEvent("view", "page1")));
		assertThat(copy, hasItem(se2));
		assertThat(copy, hasItem(se3));
		assertThat(list, not(hasItem(new SysEvent("view", "page1"))));
		assertThat(list, not(hasItem(se2)));
		assertThat(list, not(hasItem(se3)));

	}
}
