/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $('#facebook-submit').click(function(event) {
        var facebookUrl = $(this).attr("href");
        var userEmail = $('#userEmail').val();
        if (userEmail != "") {

            document.body.style.cursor = 'wait';
            $('html').css('cursor', 'wait');
            $('#submitButton').css('cursor', 'wait');

            event.preventDefault();
            event.stopPropagation();
            window.location = facebookUrl + "?userEmail=" + userEmail;
        }

        return false;
    });

    $('#facebook-reload').click(function() {
        var facebookUrl = $(this).attr("href");

        document.body.style.cursor = 'wait';
        $('html').css('cursor', 'wait');
        $('#submitButton').css('cursor', 'wait');

        event.preventDefault();
        event.stopPropagation();
        window.location = facebookUrl;

        return false;
    });

    $('li.secondary').hover(function() {
        $(this).addClass('active').find('div.secondary').show();
    }, function() {
        $(this).removeClass('active').find('div.secondary').hide();
    });

    $('.delete').click(function(e) {

        e.preventDefault();

        var id = $(this).attr('rlnm');
        var category = $(this).attr('ctrlnm');
        var method = $(this).attr('method');
        var url = $(this).attr('href');
        data = {};
        data.rlnm = $(this).attr('rlnm');
        var json = JSON.stringify(data);

        $.ajax({
            url: url,
            type: method,
            contentType: 'application/json',
            data : json,
            dataType : 'json',
            success: function( data ) {
              $('tr[rlnm='+id+']').remove();
            },
            error: function( data ) {
               $('tr[rlnm='+id+']').remove();
            }
        });

    });

    $('#admin-deactivate-user-button').click(function(event) {
        event.preventDefault();
        event.stopPropagation();

        $('#admin-deactivate-form').submit();

        return false;
    });

});