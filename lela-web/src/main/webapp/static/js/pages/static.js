/*
 * Copyright (c) 2012 Lela.com
 */

$().ready(function($) {

    // About us-page

    if($('.static-about').length > 0) {

        $('#btn-about').click(function() {
            $('.about-lela-top ul li a').removeClass('selected');
            $(this).addClass('selected');
            $('.about-page').hide();
            $('#page-about').fadeIn('fast');
        });

        $('#btn-whysignup').click(function() {
            $('.about-lela-top ul li a').removeClass('selected');
            $(this).addClass('selected');
            $('.about-page').hide();
            $('#page-whysignup').fadeIn('fast');
        });

        $('#btn-team').click(function() {
            $('.about-lela-top ul li a').removeClass('selected');
            $(this).addClass('selected');
            $('.about-page').hide();
            $('#page-team').fadeIn('fast');
        });

        $('#btn-directors').click(function() {
            $('.about-lela-top ul li a').removeClass('selected');
            $(this).addClass('selected');
            $('.about-page').hide();
            $('#page-directors').fadeIn('fast');
        });

        $('#btn-advisors').click(function() {
            $('.about-lela-top ul li a').removeClass('selected');
            $(this).addClass('selected');
            $('.about-page').hide();
            $('#page-advisors').fadeIn('fast');
        });

        switch(window.location.hash) {
            case '#aboutlela':
                $('#btn-about').click();
                break;
            case '#whysignup':
                $('#btn-whysignup').click();
                break;
            case '#team':
                $('#btn-team').click();
                break;
            case '#directors':
                $('#btn-directors').click();
                break;
            case '#advisors':
                $('#btn-advisors').click();
                break;
        }


    }

});