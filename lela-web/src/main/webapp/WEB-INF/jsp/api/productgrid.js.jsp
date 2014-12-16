/*! LAB.js (LABjs :: Loading And Blocking JavaScript)
 v2.0.3 (c) Kyle Simpson
 MIT License
 */
(function(o){var K=o.$LAB,y="UseLocalXHR",z="AlwaysPreserveOrder",u="AllowDuplicates",A="CacheBust",B="BasePath",C=/^[^?#]*\//.exec(location.href)[0],D=/^\w+\:\/\/\/?[^\/]+/.exec(C)[0],i=document.head||document.getElementsByTagName("head"),L=(o.opera&&Object.prototype.toString.call(o.opera)=="[object Opera]")||("MozAppearance"in document.documentElement.style),q=document.createElement("script"),E=typeof q.preload=="boolean",r=E||(q.readyState&&q.readyState=="uninitialized"),F=!r&&q.async===true,M=!r&&!F&&!L;function G(a){return Object.prototype.toString.call(a)=="[object Function]"}function H(a){return Object.prototype.toString.call(a)=="[object Array]"}function N(a,c){var b=/^\w+\:\/\//;if(/^\/\/\/?/.test(a)){a=location.protocol+a}else if(!b.test(a)&&a.charAt(0)!="/"){a=(c||"")+a}return b.test(a)?a:((a.charAt(0)=="/"?D:C)+a)}function s(a,c){for(var b in a){if(a.hasOwnProperty(b)){c[b]=a[b]}}return c}function O(a){var c=false;for(var b=0;b<a.dependencies.length;b++){if(a.dependencies[b].ready&&a.dependencies[b].exec_trigger){c=true;a.dependencies[b].exec_trigger();a.dependencies[b].exec_trigger=null}}return c}function t(a,c,b,d){a.onload=a.onreadystatechange=function(){if((a.readyState&&a.readyState!="complete"&&a.readyState!="loaded")||c[b])return;a.onload=a.onreadystatechange=null;d()}}function I(a){a.ready=a.finished=true;for(var c=0;c<a.finished_listeners.length;c++){a.finished_listeners[c]()}a.ready_listeners=[];a.finished_listeners=[]}function P(d,f,e,g,h){setTimeout(function(){var a,c=f.real_src,b;if("item"in i){if(!i[0]){setTimeout(arguments.callee,25);return}i=i[0]}a=document.createElement("script");if(f.type)a.type=f.type;if(f.charset)a.charset=f.charset;if(h){if(r){e.elem=a;if(E){a.preload=true;a.onpreload=g}else{a.onreadystatechange=function(){if(a.readyState=="loaded")g()}}a.src=c}else if(h&&c.indexOf(D)==0&&d[y]){b=new XMLHttpRequest();b.onreadystatechange=function(){if(b.readyState==4){b.onreadystatechange=function(){};e.text=b.responseText+"\n//@ sourceURL="+c;g()}};b.open("GET",c);b.send()}else{a.type="text/cache-script";t(a,e,"ready",function(){i.removeChild(a);g()});a.src=c;i.insertBefore(a,i.firstChild)}}else if(F){a.async=false;t(a,e,"finished",g);a.src=c;i.insertBefore(a,i.firstChild)}else{t(a,e,"finished",g);a.src=c;i.insertBefore(a,i.firstChild)}},0)}function J(){var l={},Q=r||M,n=[],p={},m;l[y]=true;l[z]=false;l[u]=false;l[A]=false;l[B]="";function R(a,c,b){var d;function f(){if(d!=null){d=null;I(b)}}if(p[c.src].finished)return;if(!a[u])p[c.src].finished=true;d=b.elem||document.createElement("script");if(c.type)d.type=c.type;if(c.charset)d.charset=c.charset;t(d,b,"finished",f);if(b.elem){b.elem=null}else if(b.text){d.onload=d.onreadystatechange=null;d.text=b.text}else{d.src=c.real_src}i.insertBefore(d,i.firstChild);if(b.text){f()}}function S(c,b,d,f){var e,g,h=function(){b.ready_cb(b,function(){R(c,b,e)})},j=function(){b.finished_cb(b,d)};b.src=N(b.src,c[B]);b.real_src=b.src+(c[A]?((/\?.*$/.test(b.src)?"&_":"?_")+~~(Math.random()*1E9)+"="):"");if(!p[b.src])p[b.src]={items:[],finished:false};g=p[b.src].items;if(c[u]||g.length==0){e=g[g.length]={ready:false,finished:false,ready_listeners:[h],finished_listeners:[j]};P(c,b,e,((f)?function(){e.ready=true;for(var a=0;a<e.ready_listeners.length;a++){e.ready_listeners[a]()}e.ready_listeners=[]}:function(){I(e)}),f)}else{e=g[0];if(e.finished){j()}else{e.finished_listeners.push(j)}}}function v(){var e,g=s(l,{}),h=[],j=0,w=false,k;function T(a,c){a.ready=true;a.exec_trigger=c;x()}function U(a,c){a.ready=a.finished=true;a.exec_trigger=null;for(var b=0;b<c.dependencies.length;b++){if(!c.dependencies[b].finished)return}c.finished=true;x()}function x(){while(j<h.length){if(G(h[j])){try{h[j++]()}catch(err){}continue}else if(!h[j].finished){if(O(h[j]))continue;break}j++}if(j==h.length){w=false;k=false}}function V(){if(!k||!k.dependencies){h.push(k={dependencies:[],finished:true})}}e={script:function(){for(var f=0;f<arguments.length;f++){(function(a,c){var b;if(!H(a)){c=[a]}for(var d=0;d<c.length;d++){V();a=c[d];if(G(a))a=a();if(!a)continue;if(H(a)){b=[].slice.call(a);b.unshift(d,1);[].splice.apply(c,b);d--;continue}if(typeof a=="string")a={src:a};a=s(a,{ready:false,ready_cb:T,finished:false,finished_cb:U});k.finished=false;k.dependencies.push(a);S(g,a,k,(Q&&w));w=true;if(g[z])e.wait()}})(arguments[f],arguments[f])}return e},wait:function(){if(arguments.length>0){for(var a=0;a<arguments.length;a++){h.push(arguments[a])}k=h[h.length-1]}else k=false;x();return e}};return{script:e.script,wait:e.wait,setOptions:function(a){s(a,g);return e}}}m={setGlobalDefaults:function(a){s(a,l);return m},setOptions:function(){return v().setOptions.apply(null,arguments)},script:function(){return v().script.apply(null,arguments)},wait:function(){return v().wait.apply(null,arguments)},queueScript:function(){n[n.length]={type:"script",args:[].slice.call(arguments)};return m},queueWait:function(){n[n.length]={type:"wait",args:[].slice.call(arguments)};return m},runQueue:function(){var a=m,c=n.length,b=c,d;for(;--b>=0;){d=n.shift();a=a[d.type].apply(null,d.args)}return a},noConflict:function(){o.$LAB=K;return m},sandbox:function(){return J()}};return m}o.$LAB=J();(function(a,c,b){if(document.readyState==null&&document[a]){document.readyState="loading";document[a](c,b=function(){document.removeEventListener(c,b,false);document.readyState="complete"},false)}})("addEventListener","DOMContentLoaded")})(this);

/*
 * LELA PRODUCT GRID LOADER v1.0
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

// dummy debug console for ie7/8
if (!window.console) console = {log: function() {}};

var Lela = Lela || {};
Lela.lelacd = '${userCode}';
Lela.affiliateId = '${affiliate.rlnm}';
Lela.applicationId = '${application.rlnm}';
Lela.baseUrl = '${baseUrl}';

Lela.LAB = $LAB;

Lela.createGrid = function(options) {

    if (!options) {
        alert("Product grid options must be defined");
    }

    // Default options if not specified
    options.affiliateId = Lela.affiliateId;
    options.applicationId = Lela.applicationId;
    options.analyticsEnabled = false;
    options.googleTracker = Lela.googleTracker;

    // If a theme stylesheet is defined, load it now
    if (options && options.theme) {
        // Load the base quiz stylesheets
        Lela.loadCss(Lela.baseUrl + '/static/api/productgrid/themes/' + options.theme + '.css');
    }

    Lela.LAB.wait(function() {
        console.log("Initializing Angular at: " + options.targetSelector);
        var element = Lela.$(options.targetSelector);

        // If the element doesn't have ng-view attribute, add it
        if (!element.attr("ng-view")) {
            element.attr("ng-view", "");
        }

        if (element) {
            angular.element(element.get(0)).ready(function() {
                // Bootstrap the application
                var gridId = 'grid-' + Math.floor(Math.random()*100000);
                var gridApp = angular.module(gridId, ['com.lela.productgrid']);
                gridApp.value('baseUrl', Lela.baseUrl);
                gridApp.value('appOptions', options);

                angular.bootstrap(element, [gridId]);
            });
        } else {
            alert("Could not find specified target grid container: " + options.targetSelector);
        }
    });

    return Lela.productGrid;
};

Lela.loadCss = function(url) {
    Lela.LAB.wait(function() {
        var css = Lela.$('<link>');
        css.attr({
            type: 'text/css',
            rel: 'stylesheet',
            href: url
        });

        Lela.$('head').append(css);
    });
};

Lela.requireJQuery = function() {
    if (typeof jQuery == 'undefined') return true;
    var tokens = jQuery().jquery.split('.');
    if (Number(tokens[0] + '.' + tokens[1]) < 1.6) return true;

    return false;
};

(function(global) {
    Lela.crossDomain = window.location.hostname.search(/lela\.com$/) == -1;
    if (console) console.log("Using lela-url: " + Lela.baseUrl + ", cross domain: " + Lela.crossDomain);

    Lela.LAB = $LAB.wait();

    if (Lela.requireJQuery()) {
        if (console) console.log("Loading jQuery");
        Lela.LAB.script(Lela.baseUrl + "/static/js/jquery-1.7.1.min.js").wait(function() {
            // If not on lela.com, unload our version of jquery from $
            Lela.$ = jQuery.noConflict();

            // Disable naughty JS and template caching
            Lela.$.ajaxSetup({
                cache: false
            });
        })
    } else {
        Lela.$ = jQuery;

        // Disable naughty JS and template caching
        Lela.$.ajaxSetup({
            cache: false
        });
    }

    if (typeof _gaq == 'undefined') {
        if (console) console.log('Google Analytics has not been loaded');
        global._gaq = [];
        Lela.googleTracker = '';
        Lela.LAB.script(('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js');
    } else {
        if (console) console.log('Google Analytics already loaded');
        Lela.googleTracker = "b.";
    }

    // Load the base quiz stylesheets
    Lela.loadCss(Lela.baseUrl + '/static/api/productgrid/themes/productgrid.css');

    Lela.LAB.script(Lela.baseUrl + "/static/js/angular/angular-1.0.1.js")
            .script(Lela.baseUrl + "/static/js/plugins/xdr.js")
            .script(Lela.baseUrl + "/static/api/common/js/lela-util.js")
            .script(Lela.baseUrl + "/static/api/productgrid/js/productgrid-api.js");
})(this);