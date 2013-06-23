package dmk.sysmon.common.service.listener;

import java.util.List;

public interface StatefulMailbox<T> {

	public List<T> getMailboxMessages();
	public void clearMailbox();
}
