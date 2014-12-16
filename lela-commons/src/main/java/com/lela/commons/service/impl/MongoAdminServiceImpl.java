package com.lela.commons.service.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoAdmin;
import org.springframework.data.mongodb.monitor.ConnectionMetrics;
import org.springframework.stereotype.Service;

import com.lela.commons.monitoring.mongo.Connections;
import com.lela.commons.monitoring.mongo.Cursors;
import com.lela.commons.monitoring.mongo.MonitorableServerStats;
import com.lela.commons.service.MongoAdminService;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.Mongo;

@Service("mongoAdminService")
public class MongoAdminServiceImpl implements MongoAdminService {

	private final ConnectionMetrics connectionMetrics;
	
	ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	public MongoAdminServiceImpl(ConnectionMetrics connectionMetrics){
		this.connectionMetrics = connectionMetrics;
	}
	@Override
	public MonitorableServerStats getServerStatus() { 
		MonitorableServerStats mss = new MonitorableServerStats(); 
		
		CommandResult cr = connectionMetrics.getServerStatus();
		if (cr != null){
			Object o = cr.get("connections");
			if (o != null & (o instanceof BasicDBObject)){
				BasicDBObject bdo = (BasicDBObject)o;
				Integer current = (Integer)bdo.get("current");
				Integer available = (Integer)bdo.get("available");
				Connections connections = new Connections();
				connections.setAvailable(available); 
				connections.setCurrent(current);
				mss.setConections(connections);
			}
			o = cr.get("cursors");
			if (o != null & (o instanceof BasicDBObject)){
				BasicDBObject bdo = (BasicDBObject)o;
				Integer totalOpen = (Integer)bdo.get("totalOpen");
				Integer clientCursors_size = (Integer)bdo.get("clientCursors_size");
				Integer timedOut = (Integer)bdo.get("timedOut");
				Cursors cursors = new Cursors();
				cursors.setTotalOpen(totalOpen);
				cursors.setClientCursors_size(clientCursors_size);
				cursors.setTimedOut(timedOut);				
				mss.setCursors(cursors);
			}
		}
		
		return mss;
	}

}
