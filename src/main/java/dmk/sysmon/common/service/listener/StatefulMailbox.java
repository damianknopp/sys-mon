package dmk.sysmon.common.service.listener;

import java.util.List;

public interface StatefulMailbox {

	public List<String> getMailboxMessages();
	public void clearMailbox();
}
