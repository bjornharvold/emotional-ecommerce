/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */
"use strict";

var Lela = Lela || {};

/* App Module */
angular.module('com.lela.productgrid', ['com.lela.productgrid.controllers', 'com.lela.productgrid.filters', 'com.lela.util'])
.constant('widgetType', 'grid')
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.otherwise({ templateUrl: Lela.baseUrl + '/static/api/productgrid/partials/item-list.html?_lelacd=' + Lela.lelacd + '&_rnd=' + Math.floor(Math.random()*10000000000), controller: 'Lela.ProductGrid' });
}])
.run(['$rootElement', function($rootElement) {
    // Make sure the root element has the ng-view attribute set
    var element = angular.element($rootElement);
    if (!element.attr("ng-view")) {
        element.attr("ng-view", "");
    }
}]);

/* Controllers Module */
angular.module('com.lela.productgrid.controllers', ['com.lela.util'])
        .controller('Lela.ProductGrid', ['$scope', '$rootScope', 'baseUrl', 'appOptions', 'lelaHttp', 'lelaAnalytics', function($scope, $rootScope, baseUrl, appOptions, lelaHttp, lelaAnalytics) {

    console.log('productgrid.controller');
    lelaAnalytics.trackEvent('app started', appOptions.applicationId);

    if (!appOptions) {
        alert("Initialization options were not defined");
        return;
    }

    var url = baseUrl + '/api/grid/' + appOptions.applicationId + '?affiliateId=' + appOptions.affiliateId + '&applicationId=' + appOptions.applicationId + '&_lelacd=' + Lela.lelacd;
    if (appOptions.maxItems) {
        url += '&size=' + appOptions.maxItems;
    }

    console.log("Calling server for productgrid JSON");
    var xhr = Lela.$.ajax(url,
            lelaHttp.formatRequest({
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    console.log("Retrieved productgrid JSON");
                    $rootScope.grid = data;
                    $scope.grid = data;

                    // Determine if this has items or relevant items
                    if (data.relevantItems) {
                        $scope.items = data.relevantItems;
                        $scope.isRelevant = true;
                    } else {
                        $scope.items = data.items;
                        $scope.isRelevant = false;
                    }

                    // Set our mouseover images
                    $scope.images = {};
                    if ($scope.items) {
                        for (var i=0, length=$scope.items.length; i<length; i++) {
                            if ($scope.items[i].clrs && $scope.items[i].clrs[0] && $scope.items[i].clrs[0].sz) {
                                $scope.images[$scope.items[i].rlnm] = $scope.items[i].clrs[0].sz.medium;
                            }
                        }
                    }

                    // Force the template to update with the data
                    $rootScope.$apply();

                    // Set up google analytics
                    appOptions.analyticsEnabled = data.analyticsEnabled;
                    lelaAnalytics.initGA();

                    lelaAnalytics.trackEvent('data loaded', appOptions.applicationId);
                    if (appOptions.gridCreated) {
                        appOptions.gridCreated(appOptions);
                    }
                },
                error: function() {
                    console.log("Error retrieving productgrid JSON");
                    lelaAnalytics.trackEvent('data error', appOptions.applicationId);
                    alert('Could not load grid definition');
                }
            })
    );

    $scope.baseUrl = baseUrl;

    $scope.setImage = function(rlnm, imageUrl) {
        $scope.images[rlnm] = imageUrl;
    }

    $scope.productName = function(item) {
        // <c:when test="${fn:startsWith(item.nm, item.subAttributes['Brand']) eq true}">${item.nm}</c:when>
        // <c:otherwise>${item.subAttributes['Brand']} ${item.nm}</c:otherwise>

        if (item.attrmp['Brand'] && item.attrmp['Brand'].vl) {
            var re = new RegExp("^" + item.attrmp['Brand'].vl);
            if (item.nm.search(re) === -1) {
                return item.attrmp['Brand'].vl + ' ' + item.nm;
            }
        }

        return item.nm
    }

    $scope.itemDetailUrl = function(item) {
        return lelaHttp.serverUrl('/' + item.srlnm + '/p?rlnm=' + item.rlnm + '&dx=4&tl=0&pg=0&sz=12');
    }

    $scope.merchantUrl = function(item) {
        return lelaHttp.serverUrl('/merchant/redirect?merchantId=' + item.attrmp.MerchantId.vl + '&itemId=' + item.rlnm);
    }

    $scope.categoryUrl = function() {
        return lelaHttp.serverUrl('/api/grid/' + appOptions.applicationId + '/seeall?affiliateId=' + appOptions.affiliateId);
    }

    $scope.relevancyImgUrl = function(num) {
        if(num == undefined) {
            return lelaHttp.serverUrl('/static/images/icons/rating/relevancy_meter_q.png');
            alert('a');
        }
        else {
            return lelaHttp.serverUrl('/static/images/icons/rating/relevancy_meter_'+num+'.png');
            alert('b');
        }
    }

}]);

/* Filters Module */
angular.module('com.lela.productgrid.filters', []).
        filter('truncate', function() {
            return function(text, length, ending) {
                if (isNaN(length))
                    length = 25;

                if (ending === undefined)
                    ending = "...";

                if (!text || text.length <= length || text.length - ending.length <= length) {
                    return text;
                }
                else {
                    return String(text).substring(0, length-ending.length) + ending;
                }
            }
        });
