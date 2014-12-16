var showScores = true;

$().ready(function($) {

    var categories = [];
    var selectedCategory;

    $('.items .item-details').fadeIn('fast');

    $('.collapse').live('click', function () {
        if($(this).hasClass('collapsed')) {
            $(this).removeClass('collapsed').attr('src', comparisonUrls.collapseImgUrl);
            $('tr.row[group="'+$(this).attr('group')+'"]').show();
        }
        else {
            $(this).addClass('collapsed').attr('src', comparisonUrls.collapsePlusImgUrl);
            $('tr.row[group="'+$(this).attr('group')+'"]').hide();
        }
    });

    $('.remove').click(function () {
        removeItemFromComparisonCookie($(this).attr('cat'), $(this).attr('rlnm'));
        if(itemsInComparisonCategory($(this).attr('cat')).length > 0) {
            window.location=comparisonUrls.comparisonUrl+getComparisonUrl()+'#'+$(this).attr('cat');
        }
        else {
            if(getComparisonUrl() != "" && getComparisonUrl() != false && getComparisonUrl().split(',').length > 1) {
                window.location=comparisonUrls.comparisonUrl+getComparisonUrl();
            }
            else {
                window.location=comparisonUrls.categoryUrl+$(this).attr('cat');
            }
        }
    });

    $.each($('.f-sec'), function() {
        if($('.cat-items p', this).length > 0) {
            $('h3 .count', this).html('('+$('.cat-items p', this).length+')');
            categories.push($(this).attr('rlnm'));
        }
        else $(this).hide();
    });

    selectedCategory = categories[0];

    if(window.location.hash != "" && window.location.hash != "#" && window.location.hash != "#_=_") {
        selectCategory(window.location.hash.substr(1));
    }
    else {
        selectCategory(selectedCategory);
    }

    initList();

    $('.f-sec h3').click(function() {
        selectCategory($(this).parent().attr('rlnm'));
    });

    $(".trigger").tooltip({
        position: "bottom right",
        relative: true
    });

    $('.details-relevancy .c-btn').click(function() {

        if($(this).hasClass('c-btn-checked')) {
            $(this).removeClass('c-btn-checked');
            $('.relevancy-mod img').fadeOut('fast');
            showScores = false;
        }
        else {
            $(this).addClass('c-btn-checked');
            $('.relevancy-mod img').fadeIn('fast');
            showScores = true;
        }

    });

    $.each($('.items .item'), function() {
        // Bind color toggle functions
        $(this).find('.color-img a').click(function(e) {
            $(this).parent().find('a').removeClass('selected');
            $(this).addClass('selected');
            $(this).parents('.item').find('.item-img').attr('src', $(this).attr('href'));

            return false;
        });

        // Bind Carousel
        $(this).find('.c-prev, .c-next').unbind('click');

        $(this).find('.c-prev').click(function(e) {

            $(this).next().find('.selected').prev('a').trigger('click');
            var index = $(this).next().find('.selected').index();

            if((index+1) % 3 === 0) {
                $(this).next().children('.color-img').animate({
                    left: "+=142px"
                }, 'slow');
            }
            return false;
        });

        $(this).find('.c-next').click(function(e) {

            $(this).prev().find('.selected').next('a').trigger('click');
            var index = $(this).prev().find('.selected').index();

            if(index % 3 === 0) {
                $(this).prev().children('.color-img').animate({
                    left: "-=142px"
                }, 'slow');
            }

            return false;
        });


    });

});

function selectCategory(cat) {

    $('.f-sec h3').removeClass('selected');
    $('.f-sec[rlnm="'+cat+'"] h3').addClass('selected');

    $('.f-sec .cat-items').slideUp('fast');
    $('.f-sec[rlnm="'+cat+'"] .cat-items').slideDown('fast');

    $('.items').fadeOut('fast', function() {
        $('.items .item').hide();
        $('.items .item[cat="'+cat+'"]').show();
        $('.items .add-item-clone').remove();

        if($('.items .item[cat="'+cat+'"]').length < 3) {
            for(var i = 0; i < 3 - $('.items .item[cat="'+cat+'"]').length; i++) {
                var pc = $('.items .add-item-original').clone().addClass('add-item-clone').removeClass('add-item-original');
                pc.find('.add-content .box').html($('.items .item[cat="'+cat+'"]').length + i + 1);
                pc.find('.add a').attr('href', comparisonUrls.categoryUrl+cat);
                pc.insertAfter('.items .item:last').show();
            }
        }

        $('.items').fadeIn('fast', function() {
            constructDataTable(cat);
        });
    });

    if(showScores == false) {
        $('.relevancy-mod img').hide();
    }

}

function constructDataTable(cat) {

    var i = 0;

    $('#comparison-table tr:not(.hidden)').remove();

    // Populate the title fields

    $.each($('.items .item[cat="'+cat+'"]:visible:first .tab-content h3'), function() {

        var c = $('#comparison-table tr.orig-hdr:first').clone().attr('group',$(this).attr("group")).removeClass('orig-hdr').removeClass('hidden').insertBefore('#comparison-table tr.row:last');
        c.find('th').html('<img class="collapse" group="'+$(this).attr("group")+'" src="'+comparisonUrls.collapseImgUrl+'" />'+$(this).html());

        var i = 0;

        $.each($('.grp[group="'+$(this).attr("group")+'"] span.title', $(this).parent()), function() {
            var r = $('#comparison-table tr.orig-row:first').clone().attr('nm', $(this).attr("nm")).attr('group', $(this).parent().attr("group")).removeClass('orig-row').removeClass('hidden');

            if(i==0) {
                r.insertAfter('#comparison-table tr.hdr[group="'+$(this).parent().attr("group")+'"]');
            }
            else {
                r.insertAfter('#comparison-table tr.row[group="'+$(this).parent().attr("group")+'"]:last');
            }

            r.find('td:first').html($(this).html());
            i++;
        });

    });

    i = 0;

    // Populate the data fields

    $.each($('.items .item[cat="'+cat+'"]:visible'), function() {

        $.each($('.tab-content .grp span.value', $(this)), function() {
            var r = $('#comparison-table tr.row[nm="'+$(this).attr("nm")+'"]');
            var c = r.find('td.col'+i);
            c.html($(this).html());
        });

        i++;

    });

    checkComparisonCookie();

}

