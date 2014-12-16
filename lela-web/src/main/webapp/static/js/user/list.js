/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {
   initList();

    $('.item').mouseenter(function(e) {
        // $(this).find('.lela-rating span').fadeIn('fast');
        $(this).find('.p-info').addClass('p-over');
        if($(this).find('.p-action')) {
            $(this).find('.p-action').show();
        }
    }).mouseleave(function(e) {
        // $(this).find('.lela-rating span').fadeOut('fast');
        $(this).find('.p-info').removeClass('p-over');
        if($(this).find('.p-action')) {
            $(this).find('.p-action').hide();
        }
    });

    updateComparisonCheckboxes();

    $('ul#p-grid li.item').removeClass('row-last');

    $('.compare-check').bind('click', function() {
        if($(this).find('.c-btn').hasClass('c-btn-checked')) {
            $(this).find('.c-btn').removeClass('c-btn-checked');
            removeItemFromComparisonCookie($(this).attr('cat'),$(this).attr('rlnm'));
        }
        else {
            var img = $('li#'+$(this).attr('rlnm')+' a.p-img img');
            if(addItemToComparisonCookie($(this).attr('cat'), $(this).attr('rlnm'), img.attr('src'))) {
                $(this).find('.c-btn').addClass('c-btn-checked');
            }
            else {
                $('#comparison-alert .compare-btn').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$(this).attr('cat'));
                $("#comparison-alert").show();
                $("#comparison-alert-trigger").fancybox({
                    'hideOnContentClick': true
                }).trigger('click');

            }
        }
    });

});

function processAvailableFilters() { }

