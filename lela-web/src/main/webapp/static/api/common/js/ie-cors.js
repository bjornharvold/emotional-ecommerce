/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
var Lela = Lela || {};
Lela.angular = Lela.angular || {};
if (!window.console) console = {log: function() {}};

(function() {
console.log('Loading IE CORS module');
Lela.angular.cors = angular.module('com.lela.ie.cors', [])
.config(['$provide', function($provide) {
    // Cross domain detection based on JQuery implementation
    var rurl = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/,
        ajaxLocation,
        ajaxLocParts;

    // #8138, IE may throw an exception when accessing
    // a field from window.location if document.domain has been set
    try {
        ajaxLocation = location.href;
    } catch( e ) {
        // Use the href attribute of an A element
        // since IE will modify it given document.location
        ajaxLocation = document.createElement( "a" );
        ajaxLocation.href = "";
        ajaxLocation = ajaxLocation.href;
    }

    // Segment location into parts
    ajaxLocParts = rurl.exec( ajaxLocation.toLowerCase() ) || [];

    function isCrossDomain(url) {
        // Determine if a cross-domain request is in order
        var parts = rurl.exec( url.toLowerCase() );
        var crossDomain = !!( parts &&
                ( parts[ 1 ] != ajaxLocParts[ 1 ] || parts[ 2 ] != ajaxLocParts[ 2 ] ||
                        ( parts[ 3 ] || ( parts[ 1 ] === "http:" ? 80 : 443 ) ) !=
                                ( ajaxLocParts[ 3 ] || ( ajaxLocParts[ 1 ] === "http:" ? 80 : 443 ) ) )
                );

        return crossDomain;
    }

    // Http Backend implementation based on Angular 1.0.2
    var URL_MATCH = /^([^:]+):\/\/(\w+:{0,1}\w*@)?([\w\.-]*)(:([0-9]+))?(\/[^\?#]*)?(\?([^#]*))?(#(.*))?$/;
    var noop = angular.noop;
    var forEach = angular.forEach;
    var isString = angular.isString;
    var lowercase = angular.lowercase;
    var XHR = window.XMLHttpRequest || function() {
        try { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); } catch (e1) {}
        try { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); } catch (e2) {}
        try { return new ActiveXObject("Msxml2.XMLHTTP"); } catch (e3) {}
        throw new Error("This browser does not support XMLHttpRequest.");
    };

    function createHttpBackend($browser, XHR, $browserDefer, callbacks, rawDocument, locationProtocol) {
        return function(method, url, post, callback, headers, timeout, withCredentials) {
            console.log("Invoking custom $httpBackend");
            $browser.$$incOutstandingRequestCount();
            url = url || $browser.url();

            if (lowercase(method) == 'jsonp') {
                var callbackId = '_' + (callbacks.counter++).toString(36);
                callbacks[callbackId] = function(data) {
                    callbacks[callbackId].data = data;
                };

                jsonpReq(url.replace('JSON_CALLBACK', 'angular.callbacks.' + callbackId),
                        function() {
                            if (callbacks[callbackId].data) {
                                completeRequest(callback, 200, callbacks[callbackId].data);
                            } else {
                                completeRequest(callback, -2);
                            }
                            delete callbacks[callbackId];
                        });
            } else if (window.XDomainRequest && isCrossDomain(url)) {
                /* Based on closed Github pull request https://github.com/angular/angular.js/pull/1047/files#L1R56 */
                console.log('Using IE 8/9 XDomainRequest');
                var xdr = new window.XDomainRequest();
                xdr.open(method.toLowerCase(), url);
                // Required to XDomainRequest works
                xdr.timeout = timeout;
                xdr.onprogress = function() {};
                xdr.ontimeout = function() {
                    completeRequest(callback, 408, 'Timeout', 'Content-Type: text/plain');
                    xdr.abort();
                };
                xdr.onload = function() {
                    completeRequest(callback, 200, xdr.responseText, 'Content-Type: ' + xdr.contentType);
                };
                xdr.onerror = function() {
                    completeRequest(callback, 500, 'Error', 'Content-Type: text/plain');
                    xdr.abort();
                };
                $browserDefer(function () {
                    xdr.send();
                }, 0); //fix IE bug that raises '$apply already in progress' on cached requests
                if (timeout > 0) {
                    $browserDefer(function() {
                        status = -1;
                        xdr.abort();
                    }, timeout);
                }
            } else {
                var xhr = new XHR();
                xhr.open(method, url, true);
                forEach(headers, function(value, key) {
                    if (value) xhr.setRequestHeader(key, value);
                });

                var status;

                // In IE6 and 7, this might be called synchronously when xhr.send below is called and the
                // response is in the cache. the promise api will ensure that to the app code the api is
                // always async
                xhr.onreadystatechange = function() {
                    if (xhr.readyState == 4) {
                        completeRequest(
                                callback, status || xhr.status, xhr.responseText, xhr.getAllResponseHeaders());
                    }
                };

                if (withCredentials) {
                    xhr.withCredentials = true;
                }

                xhr.send(post || '');

                if (timeout > 0) {
                    $browserDefer(function() {
                        status = -1;
                        xhr.abort();
                    }, timeout);
                }
            }

            function completeRequest(callback, status, response, headersString) {
                // URL_MATCH is defined in src/service/location.js
                var protocol = (url.match(URL_MATCH) || ['', locationProtocol])[1];

                // fix status code for file protocol (it's always 0)
                status = (protocol == 'file') ? (response ? 200 : 404) : status;

                // normalize IE bug (http://bugs.jquery.com/ticket/1450)
                status = status == 1223 ? 204 : status;

                callback(status, response, headersString);
                $browser.$$completeOutstandingRequest(noop);
            }
        };

        function jsonpReq(url, done) {
            // we can't use jQuery/jqLite here because jQuery does crazy shit with script elements, e.g.:
            // - fetches local scripts via XHR and evals them
            // - adds and immediately removes script elements from the document
            var script = rawDocument.createElement('script'),
                    doneWrapper = function() {
                        rawDocument.body.removeChild(script);
                        if (done) done();
                    };

            script.type = 'text/javascript';
            script.src = url;

            if (msie) {
                script.onreadystatechange = function() {
                    if (/loaded|complete/.test(script.readyState)) doneWrapper();
                };
            } else {
                script.onload = script.onerror = doneWrapper;
            }

            rawDocument.body.appendChild(script);
        }
    }

    $provide.factory('$httpBackend', ['$browser', '$window', '$document', function($browser, $window, $document) {
        console.log("Providing custom IE 8&9 CORS $httpBackend");
        return createHttpBackend($browser, XHR, $browser.defer, $window.angular.callbacks,
                $document[0], $window.location.protocol.replace(':', ''));
    }]);
}])
})();