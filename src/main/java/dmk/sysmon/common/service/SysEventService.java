package dmk.sysmon.common.service;

import dmk.sysmon.common.domain.SysEvent;

public interface SysEventService {

	public SysEventService ingest(final SysEvent e);
}
