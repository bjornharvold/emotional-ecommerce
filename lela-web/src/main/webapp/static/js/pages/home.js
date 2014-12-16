/*
 * Copyright (c) 2012. Lela.com.
 */

$().ready(function($) {

    initSubscribe();

    window.autoCat = true;
    if($('#home-quotes').length > 0) {
        var quoteCarousel = carousel('home-quotes', 5000, 800, '', false);
    }

    var bgCarousel = carousel('copy-marquee', 10000, 400, 'marquee-controls', false, false, true);

    $('.cat-c-l').mouseenter(function() {
        var img = $(this).find('img');
        img.attr('src', img.attr('src').replace('.png', '_over.png'));
    }).mouseout(function() {
        var img = $(this).find('img');
        img.attr('src', img.attr('src').replace('_over.png', '.png'));
    });

    if($('#email').length > 0) {
        $('#email').focus(function() {
            if($(this).val() == 'Enter your e-mail address') {
                $(this).val('');
            }
        });
    }

    /*
    if($('.copy-afterquiz').length > 0) {
        if($('#myFavesCount').val() > 0) $('#goToLelaList').show();
        else $('#startLelaList').show();
    }
    */

    if($('#banner-marquee').length > 0) {
        var bannerMarquee = carousel('banner-marquee', 10000, 400, '', true, false, true);
    }

});

function initSubscribe() {

    $('#email').focus(function () {
        $(this).val('');
        $('#mc-error').fadeOut('fast');
        $(this).css('color', '#333');
    });

    $('#mc-subscribe').submit(function() {

        data = {};
        data.email = $('#email').val();
        data.listId = mcListId;

        // validate email address on client.
        if(validateEmail($('#email').val()) == false){

            $('#mc-error').fadeIn('fast');

        } else {

            var json = JSON.stringify(data);

            $.ajax({
                url: $(this).attr('action'),
                type: 'POST',
                contentType: 'application/json',
                data : json,
                dataType : 'json',
                beforeSend: function( xhr ) {
                    $('#mc-success').hide();
                    $('#mc-error').hide();
                },
                success: function( data ) {
                    $('#email').val('');
                    $('#row-email').val('');
                    $('#mc-success').fadeIn('fast');

                }
            });
        }



        return false;
    });
}