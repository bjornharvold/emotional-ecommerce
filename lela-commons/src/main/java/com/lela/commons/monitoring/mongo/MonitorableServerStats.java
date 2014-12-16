package com.lela.commons.monitoring.mongo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * 
 * 
 * The mongo server returns the following stats (sample):
 * 
 * 		{
			   "serverUsed":"localhost/127.0.0.1:27017",
			   "host":"new-host-2.home",
			   "version":"2.2.0",
			   "process":"mongod",
			   "pid":456,
			   "uptime":319035.0,
			   "uptimeMillis":319035298,
			   "uptimeEstimate":119708.0,
			   "localTime":{
			      "$date":"2012-12-21T15:12:31.481Z"
			   },
			   "locks":{
			      ".":{
			         "timeLockedMicros":{
			            "R":7592067,
			            "W":30801245
			         },
			         "timeAcquiringMicros":{
			            "R":4016717,
			            "W":692494
			         }
			      },
			      "admin":{
			         "timeLockedMicros":{

			         },
			         "timeAcquiringMicros":{

			         }
			      },
			      "local":{
			         "timeLockedMicros":{
			            "r":164277,
			            "w":0
			         },
			         "timeAcquiringMicros":{
			            "r":29282,
			            "w":0
			         }
			      },
			      "leladb":{
			         "timeLockedMicros":{
			            "r":173106263,
			            "w":12467614
			         },
			         "timeAcquiringMicros":{
			            "r":12255355,
			            "w":3746361
			         }
			      }
			   },
			   "globalLock":{
			      "totalTime":319035298000,
			      "lockTime":30801245,
			      "currentQueue":{
			         "total":0,
			         "readers":0,
			         "writers":0
			      },
			      "activeClients":{
			         "total":0,
			         "readers":0,
			         "writers":0
			      }
			   },
			   "mem":{
			      "bits":64,
			      "resident":255,
			      "virtual":4452,
			      "supported":true,
			      "mapped":976,
			      "mappedWithJournal":1952
			   },
			   "connections":{
			      "current":14,
			      "available":190
			   },
			   "extra_info":{
			      "note":"fields vary by platform",
			      "page_faults":412412
			   },
			   "indexCounters":{
			      "btree":{
			         "accesses":345451,
			         "hits":345451,
			         "misses":0,
			         "resets":0,
			         "missRatio":0.0
			      }
			   },
			   "backgroundFlushing":{
			      "flushes":2175,
			      "total_ms":2107905,
			      "average_ms":969.1517241379311,
			      "last_ms":27,
			      "last_finished":{
			         "$date":"2012-12-21T15:12:26.263Z"
			      }
			   },
			   "cursors":{
			      "totalOpen":0,
			      "clientCursors_size":0,
			      "timedOut":0
			   },
			   "network":{
			      "bytesIn":29197670,
			      "bytesOut":1.4868253399E10,
			      "numRequests":109115
			   },
			   "opcounters":{
			      "insert":18896,
			      "query":41878,
			      "update":4153,
			      "delete":8122,
			      "getmore":4216,
			      "command":35677
			   },
			   "asserts":{
			      "regular":0,
			      "warning":0,
			      "msg":0,
			      "user":16,
			      "rollovers":0
			   },
			   "writeBacksQueued":false,
			   "dur":{
			      "commits":29,
			      "journaledMB":0.0,
			      "writeToDataFilesMB":0.0,
			      "compression":0.0,
			      "commitsInWriteLock":0,
			      "earlyCommits":0,
			      "timeMs":{
			         "dt":3030,
			         "prepLogBuffer":0,
			         "writeToJournal":0,
			         "writeToDataFiles":0,
			         "remapPrivateView":0
			      }
			   },
			   "recordStats":{
			      "accessesNotInMemory":487,
			      "pageFaultExceptionsThrown":325,
			      "leladb":{
			         "accessesNotInMemory":487,
			         "pageFaultExceptionsThrown":325
			      },
			      "local":{
			         "accessesNotInMemory":0,
			         "pageFaultExceptionsThrown":0
			      }
			   },
			   "ok":1.0
			}
			
			
			
			Of these, a few that are measurable over time are recorded by this class
 * @author pankajtandon
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonitorableServerStats {

	private Connections connections;
	private Cursors cursors;
	
	public Connections getConections() {
		return connections;
	}
	public void setConections(Connections connections) {
		this.connections = connections;
	}
	public Cursors getCursors() {
		return cursors;
	}
	public void setCursors(Cursors cursors) {
		this.cursors = cursors;
	}
	
}
