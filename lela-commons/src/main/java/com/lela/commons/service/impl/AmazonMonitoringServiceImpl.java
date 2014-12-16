package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.lela.commons.monitoring.mongo.Connections;
import com.lela.commons.monitoring.mongo.Cursors;
import com.lela.commons.monitoring.mongo.MonitorableServerStats;
import com.lela.commons.service.LelaMonitoringService;
import com.lela.commons.service.MongoAdminService;

@Service("lelaMonitoringService")
public class AmazonMonitoringServiceImpl implements LelaMonitoringService {
	
	private final static Logger log = LoggerFactory.getLogger(AmazonMonitoringServiceImpl.class);
	
	private String RECORD_METHOD_AVERAGE = "recordMethodAverage";
	private String RECORD_METHOD_WATERMARK = "recordMethodWatermark";
	private String RECORD_METHOD_LOWPOINT = "recordMethodLowpoint";
	private String RECORD_METHOD_CUMULATIVE = "recordMethodCumulative";
	
	private final AmazonCloudWatch amazonCloudWatch;
	private final MongoAdminService mongoAdminService;
	
	@Value("${monitoring.status:true}")
	private String monitoringStatus;
	
	@Value("${monitoring.namespace:lelaMonitoring}")
	private String monitoringNamespace;
	
	@Value("${monitoring.interval.seconds:60}")
	private String monitoringIntervalSeconds;
	
	private Map<Interval, Long> dbAccessMonitoringHash = new HashMap<Interval, Long>();
		
	private Map<Interval, Long> dbConnectionsMonitoringHash = new HashMap<Interval, Long>();
	
	private Map<Interval, Long> dbOpenCursorsMonitoringHash = new HashMap<Interval, Long>();
	
	private Map<Interval, Long> logMonitoringHash = new HashMap<Interval, Long>();
	
	private Map<Interval, Long> threadMonitoringHash = new HashMap<Interval, Long>();

	
	@Autowired
	public AmazonMonitoringServiceImpl(AmazonCloudWatch amazonCloudWatch, MongoAdminService mongoAdminService){
		this.amazonCloudWatch = amazonCloudWatch;
		this.mongoAdminService = mongoAdminService;
	}
	
	
	/**
	 * Use this method to record access to a db
	 */
	@Override 
	public void recordDBStat(){
		this.incrementStatOnMap(dbAccessMonitoringHash);
	}
	
	@Override
	public void recordThreadStat(){
		this.incrementStatOnMap(threadMonitoringHash);
	}
	
	@Override
	public void recordLogStat(){
		this.incrementStatOnMap(logMonitoringHash);
	}


	@Override 
	@Scheduled(fixedRate=60000) //Ping server every  mins to gather stats
	public void getServerStatus(){
		log.debug("Pinging mongo for stats...");
		if ("true".equalsIgnoreCase(monitoringStatus)){
			MonitorableServerStats  monitorableServerStats = mongoAdminService.getServerStatus();
			Connections connections = monitorableServerStats.getConections();
			if (connections != null){ 
				Integer availableConnections = connections.getAvailable();
				if (availableConnections != null){
					this.recordAverageStatOnMap(dbConnectionsMonitoringHash, availableConnections.longValue(), RECORD_METHOD_LOWPOINT);
				}
			}
			
			Cursors cursors = monitorableServerStats.getCursors();
			if (cursors != null){
				if (cursors.getTotalOpen() != null) {
					this.recordAverageStatOnMap(dbOpenCursorsMonitoringHash, cursors.getTotalOpen().longValue(), RECORD_METHOD_WATERMARK);
				}
			}
		} else {
			log.debug("Monitoring status is false. Not pinging mongo for stats");
		}
	}
	
	
	@Override
	//Post to amazon every 5 minutes
	@Scheduled(fixedRate=300000) 
	public void postToAmazon(){ 
		log.debug("Posting monitoring stats to amazon");
		if ("true".equalsIgnoreCase(monitoringStatus)){
			post(dbAccessMonitoringHash, LelaMonitoringService.MONITOR_TYPE_DB_ACCESS);
			post(dbConnectionsMonitoringHash, LelaMonitoringService.MONITOR_TYPE_DB_CONNECTIONS); 
			post(dbOpenCursorsMonitoringHash, LelaMonitoringService.MONITOR_TYPE_DB_OPEN_CURSORS);
			
			//Yet to implement
			post(logMonitoringHash, LelaMonitoringService.MONITOR_TYPE_LOG_ACCESS);
			post(threadMonitoringHash, LelaMonitoringService.MONITOR_TYPE_THREAD_ACCESS);
		} else {
			log.debug("Monitoring status is false. Not posting to amazon");
		}
	}

	private void post(Map<Interval, Long> map,String metricName) {
		//Make failsafe
		try {
			PutMetricDataRequest r = new PutMetricDataRequest();
			r.setNamespace(monitoringNamespace);
			if (map.size() > 0){
				List<MetricDatum> metricDataList = new ArrayList<MetricDatum>();
				for (Interval interval : map.keySet()) {
					long intervalStart = interval.getStartMillis();
					
					MetricDatum md = new MetricDatum();
					md.setMetricName(metricName); 
					md.setValue(new Long(map.get(interval)).doubleValue());
					md.setTimestamp(new Date(intervalStart));
					log.debug("Posting metric: " + md.toString());
					metricDataList.add(md);
					//map.remove(interval);
				}
				r.setMetricData(metricDataList);
				amazonCloudWatch.putMetricData(r);
				
				//Clear out the map
				map.clear();
			}
		} catch (Exception e){
			log.warn("Error posting to Amazon: ", e);
		}
	}

	/**
	 * Increments the count in the map of this metric by one
	 * @param map
	 */
	private void incrementStatOnMap(Map<Interval, Long> map) { 
		long now = new DateTime().getMillis();
		Long intervalSeconds = Long.parseLong(monitoringIntervalSeconds);

		Interval currentInterval = null;
		//Check which interval the current time falls within
		for (Interval interval : map.keySet()) {
			if (interval.contains(now)) {
				currentInterval = interval;
				break;
			}
		}
		if (currentInterval != null){
			Long count = map.get(currentInterval);
			map.put(currentInterval, count==null?0:count + 1);
		} else {
			//Create a new interval
			Interval interval = new Interval(now, now + intervalSeconds * 1000);
			map.put(interval, 1L);
		}
	}
	
	/**
	 * Reords the valueToIncrement passed as per the record method passed in for that interval
	 * @param map
	 * @param valueToIncrement
	 */
	private void recordAverageStatOnMap(Map<Interval, Long> map, Long valueToIncrement, String recordMethod) { 
		long now = new DateTime().getMillis();
		Long intervalSeconds = Long.parseLong(monitoringIntervalSeconds);

		Interval currentInterval = null;
		//Check which interval the current time falls within
		for (Interval interval : map.keySet()) {
			if (interval.contains(now)) {
				currentInterval = interval;
				break;
			}
		}
		if (currentInterval != null){
			Long count = map.get(currentInterval);
			double fin = 0.0;
			if (count != null){
				if (recordMethod.equals(RECORD_METHOD_AVERAGE)){
					fin =  (count.doubleValue() + (valueToIncrement.doubleValue()))/2.0;
				} else if (recordMethod.equals(RECORD_METHOD_CUMULATIVE)){
					fin =  (count.doubleValue() + (valueToIncrement.doubleValue()));
				} else if (recordMethod.equals(RECORD_METHOD_WATERMARK)){
					fin = (valueToIncrement > count? valueToIncrement: count);
				} else if (recordMethod.equals(RECORD_METHOD_LOWPOINT)) {
					fin = (valueToIncrement > count? count: valueToIncrement);
				}
			}
			map.put(currentInterval, new Double(fin).longValue());
		} else {
			//Create a new interval
			Interval interval = new Interval(now, now + intervalSeconds * 1000);
			map.put(interval, valueToIncrement);
		}
	}

	
}
