package com.lela.commons.service;


public interface LelaMonitoringService {
	
	public static String MONITOR_TYPE_DB_ACCESS = "dbAccessMonitor";
	public static String MONITOR_TYPE_DB_CONNECTIONS = "dbConnectionsMonitor";
	public static String MONITOR_TYPE_DB_OPEN_CURSORS = "dbOpenCursorsMonitor";
	public static String MONITOR_TYPE_LOG_ACCESS = "logAccessMonitor";
	public static String MONITOR_TYPE_THREAD_ACCESS = "threadAccessMonitor";
	

	void recordDBStat();
	void recordThreadStat();
	void recordLogStat();
	
	//For tests
	void postToAmazon();
	void getServerStatus();
}
