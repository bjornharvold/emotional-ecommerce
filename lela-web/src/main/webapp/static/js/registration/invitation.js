/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $('#launch-video').fancybox({
        'padding'		: 0,
        'autoScale'		: true
    });


    $('#password-hint').focus(function() {
       $(this).toggle();
       $('#password').toggle();
       $('#password').focus();
       $('#password').val('');
    });

    $('#password').blur(function() {
       $(this).toggle();
       $('#password-hint').toggle();
       $('#submit').fadeOut('slow');
       $(this).parent().find('span').removeClass('check');
       $(this).parent().find('span').addClass('uncheck');
    });

    $('#password').keyup(function() {

       if($(this).val().length < 4) {
            $('#submit').fadeOut('slow');
            $(this).parent().find('span').removeClass('check');
            $(this).parent().find('span').addClass('uncheck');
       } else {
            $('#submit').fadeIn('slow');
            $(this).parent().find('span').removeClass('uncheck');
            $(this).parent().find('span').addClass('check');
       }
    });

    $('#submit').click(function() {
        $('#wrapper').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.8 }});
        $('#reg-form').submit();
        return false;
    });


    
});