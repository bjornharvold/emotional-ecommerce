var Crawler = require("simplecrawler").Crawler;
var fs = require('fs');
var BufferedWriter = require ("buffered-writer");

var cr = new Crawler("dev.lela.com");

cr.initialPath = "/";
cr.initialPort = 8080;
cr.initialProtocol = "http";
cr.interval = 10;
cr.maxConcurrency = 10;

console.log("Starting local crawler..");
new BufferedWriter ("crawler_results.txt", { encoding: "utf8", append: true }).on ("error", function (error){ console.log (error); }).newLine().write("NEW CRAWL - "+Date.now()).close(); 

var lastUrl = "/";

cr.on("fetchstart",function(queueItem, response) {
	process.stdout.write("Fetching " + queueItem.url+" (last: "+lastUrl+"\r");
});

cr.on("fetcherror",function(queueItem, response) {
	if(response.statusCode == "404") {
		response.on("data",function(chunk) {
			console.log("\nFound 404: "+queueItem.url+" referred by "+chunk.Referer);
			new BufferedWriter ("crawler_results.txt", { encoding: "utf8", append: true }).on ("error", function (error){ console.log (error); }).newLine().write(queueItem.url+";404;"+chunk.Referer).close(); 
		});
	}
	else if(response.statusCode == "500") {
		response.on("data",function(chunk) {
			console.log("\nFound 500: "+queueItem.url+" referred by "+chunk.Referer);
			new BufferedWriter ("crawler_results.txt", { encoding: "utf8", append: true }).on ("error", function (error){ console.log (error); }).newLine().write(queueItem.url+";500;"+chunk.Referer).close(); 
		});
	}
	lastUrl = queueItem.url;
});

cr.on("fetchcomplete",function(queueItem, responseBuffer, response) {
	//process.stdout.write('.');
	lastUrl = queueItem.url;
});

cr.on("complete",function() {
	console.log("Crawl complete");
});

cr.start();
