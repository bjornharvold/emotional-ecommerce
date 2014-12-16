/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    trackEvent('items', 'search', query, 1);
    initList();
    $('#d-tuners').jqTransform();


    $('.filter-count').append($('#item-count'));


    $(".trigger").tooltip({
        position: "bottom right",
        relative: true
    });

    $('.s-term a').click(function() {

        var oldQuery = $('#nav-search input').val();
        var newQuery = oldQuery.replace($(this).html(), '');
        var newQuery = newQuery.replace('  ', ' ');
        $(this).remove();


        $('#nav-search input').val(newQuery);

        $('#search-form').submit();
        return false;
    });

    $('.compareButton').live('click', function() {
        if($(this).find('.c-btn').hasClass('c-btn-checked')) {
            $(this).find('.c-btn').removeClass('c-btn-checked');
            $('.compare-check[rlnm="'+$(this).attr('rlnm')+'"]').hide().find('.c-btn').removeClass('c-btn-checked');
            removeItemFromComparisonCookie($(this).attr('cat'),$(this).attr('rlnm'));
        }
        else {
            var img = $('li#'+$(this).attr('rlnm')+' a.p-img img');
            if(addItemToComparisonCookie($(this).attr('cat'), $(this).attr('rlnm'), img.attr('src'))) {
                $(this).find('.c-btn').addClass('c-btn-checked');
                $(this).parent().show();
                $('.compare-check[rlnm="'+$(this).attr('rlnm')+'"]').show().find('.c-btn').addClass('c-btn-checked');

                if(itemsInComparisonCategory($(this).attr('cat')).length > 1) {
                    $('.compare-btn').removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$(this).attr('cat')).fadeIn(100);
                }
            }
            else {
                $('.compare-btn').removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$(this).attr('cat')).fadeIn(100);
                $("#comparison-alert").show();
                $("#comparison-alert-trigger").fancybox({
                    'hideOnContentClick': true
                }).trigger('click');

            }
        }
    });



});