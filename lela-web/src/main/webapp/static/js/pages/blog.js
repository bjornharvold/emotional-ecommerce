$().ready(function($) {

    var t = setTimeout("initLoader()", 1000);

    if($('.item-data').length > 0) {
        $('.item-data').lionbars();
    }

    $('.item-info').hide();

    $('.item-img a').mouseenter(function() {
        $(this).parent().next('.item-info').fadeIn('fast');
    });

    $('.item-info').mouseleave(function() {
        $(this).fadeOut('fast');
    });

    if($('.content').length > 0) {
        $('.content').columnize({columns:2});
    }

    if($('.first-row .tip').length > 0) {
        $('.first-row .tip').tooltip({
            position: "top right",
            offset: [-2, -32],
            relative: true
       });

        $('.last-row .tip').tooltip({
            position: "top left",
            offset: [-2, 32],
            relative: true
       });
    }

   $('.prev-posts h3 a').click(function() {

       $('#prev-list').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});

       $('.prev-posts h3 a').removeClass('selected');
       $(this).addClass('selected');
       var url = $(this).attr('href')+'?sort='+$(this).attr('sort');

       $('#prev-list').load(url, function() {
           $('#prev-list').unblock();
       });
       return false;
   });

});

function initLoader() {

    var images = $('.blog-item .loader');

    $(images.get(0)).fadeOut(800, function() {
        $(images.get(1)).fadeOut(800, function() {
            $(images.get(2)).fadeOut(800, function() {
                $(images.get(3)).fadeOut(800);
            });
        });
    });
}

function initPagination() {
    $('#blog-pagination a').click(function() {

        $('#prev-list').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});

        $('#prev-list').load($(this).attr('href'), function() {
            $('#prev-list').unblock();
        });

        return false;
    });
}