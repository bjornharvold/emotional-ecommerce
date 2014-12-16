package com.lela.commons.service;

import com.lela.commons.monitoring.mongo.MonitorableServerStats;

public interface MongoAdminService {

	MonitorableServerStats getServerStatus();
}
