/*
 * Copyright (c) 2012 Lela.com
 */

$().ready(function($) {

    var examplescarousel = carousel('examples-carousel', 4000, 400, '', true);


    // Show tutorial if cookie is set.
    if($.cookie('show-tutorial') == '1') {
        $.cookie('show-tutorial', 0, { path: '/', expires: 7 });
        initTutorial(true);
    }

    if($.cookie('lela-event-participated') && $.cookie('lela-event-participated') == '1') {
        $('#event-text').show();
        $('#event-text img').hover(function() {
            $(this).attr('src',$(this).attr('over'));
        }, function() {
            $(this).attr('src',$(this).attr('out'));
        });
    }

});