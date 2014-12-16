/* ////// BOOKMARKLET codes for all environments

 // DEV
 javascript:(function(){window._lelalist_env='dev';var jsCode=document.createElement('script');jsCode.setAttribute('src','http://dev.lela.com:8080/static/js/list/grab.js');document.body.appendChild(jsCode); }())

 // LATEST
 javascript:(function(){window._lelalist_env='latest';var jsCode=document.createElement('script');jsCode.setAttribute('src','http://latest.lela.com/static/js/list/grab.js');document.body.appendChild(jsCode); }())

 // QA
 javascript:(function(){window._lelalist_env='qa';var jsCode=document.createElement('script');jsCode.setAttribute('src','http://qa.lela.com/static/js/list/grab.js');document.body.appendChild(jsCode); }())

 // WWW
 javascript:(function(){window._lelalist_env='www';var jsCode=document.createElement('script');jsCode.setAttribute('src','http://www.lela.com/static/js/list/grab.js');document.body.appendChild(jsCode); }())

 */

if (!(jQuery = window.jQuery)) { // typeof jQuery=='undefined' works too
    var _lelalist_script = document.createElement('script');
    _lelalist_script.src = 'http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js';
    _lelalist_script.onload = load;
    document.body.appendChild(_lelalist_script);
}
else {
    load();
}

function load() {

    var _lelalist_baseUrl = 'http://www.lela.com';
    if(window._lelalist_env == 'dev') {
        _lelalist_baseUrl = 'http://dev.lela.com:8080';
    } else if(window._lelalist_env == 'latest') {
        _lelalist_baseUrl = 'http://latest.lela.com';
    } else if(window._lelalist_env == 'qa') {
        _lelalist_baseUrl = 'http://qa.lela.com';
    } else if(window._lelalist_env == 'www') {
        _lelalist_baseUrl = 'http://www.lela.com';
    }

    // Load required CSS
    var _lelalist_css = document.createElement("link");
    _lelalist_css.setAttribute('rel', 'stylesheet');
    _lelalist_css.setAttribute('type', 'text/css');
    _lelalist_css.setAttribute('href', _lelalist_baseUrl+'/static/styles/list/grab.css');
    document.getElementsByTagName('head')[0].appendChild(_lelalist_css);

    // Remove previous content
    _lelalist_unLoad();

    jQuery('body,html').animate({
        scrollTop: 0
    }, 800);

    // setup overlay
    jQuery('<div id="_lelalist_overlay" class="_lelalist_el" style="height: ' + jQuery(document).height() + 'px;"></div>').appendTo('body');

    // setup container
    jQuery('<div id="_lelalist_wrapper" class="_lelalist_el"><div id="_lelalist_header">Lela List Grab It! <a href="#" id="_lelalist_cancel" onclick="_lelalist_unLoad(); return false;">Cancel</a></div><div id="_lelalist_main"></div></div>').appendTo('body');

    var _lelalist_remoteUrl = window.location.href;

    jQuery('img').each(function () {
        if (jQuery(this).width() > 150) {

            // Get title from Alt tag or Filename if alt is empty
            var _lelalist_title = '';
            if(jQuery(this).attr('alt')) {
                _lelalist_title = jQuery(this).attr('alt');
            }

            jQuery('<a target="_blank" onclick="_lelalist_unLoad();" href="' + _lelalist_baseUrl + '/user/list?boardCode=Default&remoteWidth=' + jQuery(this).width() + '&remoteUrl=' + _lelalist_remoteUrl + '&remoteImg=' + this.src.replace('#', '') + '&remoteTitle=' + _lelalist_title + '" class="_lelalist_link"><span>Grab It!<em>' +  jQuery(this).width() + 'px x ' + jQuery(this).height() + 'px</em></span><img src="' + this.src + '" class="_lelalist_img" width="200" /></a>').appendTo('#_lelalist_main');
        }
    });

}

function _lelalist_unLoad() {
    jQuery('#_lelalist_wrapper, #_lelalist_overlay').fadeOut('fast', function() {
        jQuery('#_lelalist_wrapper, #_lelalist_overlay').remove();
    });
}