$().ready(function($) {

    $('#terms-agree').jqTransCheckBox();

    $('.l-terms').fancybox({
        'padding'			: 0,
        'transitionIn'		: 'fade',
        'transitionOut'		: 'fade'
    });

        $('#heart').hover(function() {
            $(this).attr('src',$(this).attr('over'));
        }, function() {
            $(this).attr('src',$(this).attr('out'));
        });


    $('#terms-agree').click(function() {
        if ($('#terms-agree:checked').val() !== undefined) {
            $('#terms-lk-disabled').css('display','block');
            $('#terms-lk').hide();
            $.cookie('lela-event-agree', 0, { expires: 60, path: '/' });
        } else {
            $('#terms-lk-disabled').hide();
            $('#terms-lk').css('display','block');
            $.cookie('lela-event-agree', 1, { expires: 60, path: '/' });
        }
    });

    $('#terms-lk').click(function() {

        if ($('#terms-agree:checked').val() !== undefined) {

            data = {};
            var json = JSON.stringify(data);

            $.ajax({
                url: $(this).attr('href'),
                type: 'POST',
                data: json,
                contentType: 'application/json',
                dataType : 'json',
                beforeSend: function( xhr ) {
                    // set cookie
                    $.cookie('show-tutorial', 1, { expires: 3600, path: '/' });
                },
                success: function( data ) {
                    $.cookie('lela-event-participated', 1, { expires: 60, path: '/' });
                    window.location = '/quiz'
                }
            });

        } else {
            alert('you need to agree to the terms first!');
        }

        return false;
    });

});