/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

/* Util Module */
angular.module('com.lela.util', [])
.run(['$rootScope', 'baseUrl', 'appOptions', function($rootScope, baseUrl, appOptions) {
    // Set random number to be used to query templates for this app session to ensure they
    // aren't cached and changes are missed
    appOptions.random = Math.floor(Math.random()*10000000000);
}])

/** Lela Config
 *
 window.LelaConfig = {
 user: {
 firstName: 'First Name',
 lastName: 'Last Name',
 email: 'email@address.com',
 lelacd: '21234-12341234-12341234123'
 }
 };
 * */
.factory('lelaConfig', [function() {
    function hasConfig() { return window.LelaConfig ? true : false; }
    function hasUser() { return window.LelaConfig && window.LelaConfig.user; }
    function getUser() { return hasUser() ? window.LelaConfig.user : null; }

    var service = {
        hasConfig: hasConfig,
        hasUser: hasUser,
        getUser: getUser
    };
    return service;
}])

/** AngularJS $http and $resource do not handle cross-domain requests in IE correctly **/
.factory('lelaHttp', ['$rootScope', 'widgetType', 'baseUrl', 'appOptions', function($rootScope, widgetType, baseUrl, appOptions) {
    var service =
    {
        formatRequest: function(options) {
            if (Lela.crossDomain) {
                console.log("Sending cross domain Ajax request");
                options.crossDomain = true;
                options.xhrFields = { withCredentials: true };
            }

            return options;
        },

        serverUrl: function(url) {
            if (url.indexOf('http') === -1) {
                url = baseUrl + url;
            }

            var separator = url.indexOf('?') === -1 ? '?' : "&";

            // Add the lelacd and affiliate id
            url += separator + '_lelacd=' + Lela.lelacd + '&_aid=' + appOptions.affiliateId;
            separator = '&';

            if (appOptions.analyticsEnabled) {
                url += separator +  'utm_source=' + appOptions.affiliateId + '-grid_api&utm_medium=affiliate&utm_campaign=' + appOptions.affiliateId + ":" + appOptions.applicationId;
                separator = '&';
            }

            return url;
        }
    };

    return service;
}])

.factory('lelaUI', ['$rootElement', 'baseUrl', function($rootElement, baseUrl){
    function blockRoot() {
        block($rootElement);
    }

    function block(element) {
        Lela.$(element).block({
            message:  '<img src="' + Lela.baseUrl + '/static/images/icons/lela_loader.gif" alt="loading"/>',
            css: {
                border: 'none',
                padding: '15px',
                backgroundColor: 'none',
                opacity: 1
            },
            overlayCSS:  {
                backgroundColor: '#fff',
                opacity:         0.8
            }
        });
    }

    function unblockRoot() {
        unblock($rootElement);
    }

    function unblock(element) {
        Lela.$(element).unblock();
    }

    var service = {
        blockRoot: blockRoot,
        block: block,
        unblockRoot: unblockRoot,
        unblock: unblock
    };

    return service;
}])

.factory('lelaCookie', ['$rootElement', 'baseUrl', function($rootElement, baseUrl){
    function getCookie(name) {
        return (r = new RegExp('(?:^|; )' + name + '=([^;]*)').exec(document.cookie)) ? (r[1]) : null;
    }

    function setCookie(name, value, days) {
        var dt=new Date();
        dt.setDate(dt.getDate() + days);
        var c= value + '; domain=.' + window.location.hostname + '; path=/; expires=' + dt.toUTCString();
        document.cookie= name + '=' + c;
    };

    var service = {
        setCookie: setCookie,
        getCookie: getCookie
    };

    return service;
}])

.factory('lelaAnalytics', ['$rootScope', 'widgetType', 'baseUrl', 'appOptions', function($rootScope, widgetType, baseUrl, appOptions) {
    var type = widgetType || 'unknown';
    var service = {
        initGA: function() {
            if (appOptions.analyticsEnabled) {
                console.log("Analytics enabled");
                _gaq.push([appOptions.googleTracker + '_setAccount', 'UA-26275943-1']);
            } else {
                console.log("Analytics disabled");
            }
        },

        trackEvent: function(action, label) {
            if(appOptions.analyticsEnabled) {
                var cat = appOptions.affiliateId + type;
                var value = +1;

                if (console) console.log('Analytics: ' + cat + ' - ' + action + ' - ' + label + ' - ' + value);
                _gaq.push([appOptions.googleTracker + '_trackEvent', cat, action, label, value]);
            }
        }
    };

    return service;
}]);