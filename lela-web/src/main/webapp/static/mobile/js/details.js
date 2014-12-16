function updateStoreCounts() {
    $('#online-count').html($('#buy-tbl-online tbody tr').length);
    $('#local-count').html($('#loc-tbl-container tbody tr').length);
}

$().ready(function($) {

    $('.online-hdr').click(function(e) {
        e.preventDefault();
        if($('.online-stores').is(':visible')) {
            $('.online-stores').slideUp('fast');
            $(this).removeClass('hdr-toggled');
        }
        else {
            $('.online-stores').slideDown('fast');
            $(this).addClass('hdr-toggled');
        }
    });

    $('.local-hdr').click(function(e) {
        e.preventDefault();
        if($('.local-stores').is(':visible')) {
            $('.local-stores').slideUp('fast');
            $(this).removeClass('hdr-toggled');
        }
        else {
            $('.local-stores').slideDown('fast');
            $(this).addClass('hdr-toggled');
        }
    });

    $('#showMoreSpecs').click(function() {
        $(this).hide();
        $('#showLessSpecs').css({'display': 'block'});
        $('.spec-hidden').slideDown('fast');
    });

    $('#showLessSpecs').click(function() {
        $(this).hide();
        $('#showMoreSpecs').css({'display': 'block'});
        $('.spec-hidden').slideUp('fast');
    });

    $('.next-img').click(function() {
        var cur = $('.product-img img.current');
        if(cur.next('img').length == 0) {
            $('.product-img img').first().show().addClass('current');
        }
        else {
            cur.next('.product-img img').show().addClass('current');
        }
        cur.hide().removeClass('current');
    });

    $('.prev-img').click(function() {
        var cur = $('.product-img img.current');
        if(cur.prev('img').length == 0) {
            $('.product-img img').last().show().addClass('current');
        }
        else {
            cur.prev('.product-img img').show().addClass('current');
        }
        cur.hide().removeClass('current');
    });

    if($('.product-img img').length <= 1) {
        $('.prev-img').hide();
        $('.next-img').hide();
    }

    updateStoreCounts();

});
