/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

var lastFilterActive;

$().ready(function($) {

    $(window).resize(positionBackToTop());

    var popUpTimer;

    if($('#reg-popup').length > 0 && $('.page-grid').length > 0) {
        if($.cookie("lelaPopup")) {
            displayPopup(20);
        }
        else {
            displayPopup(5);
        }
    }

    $('#quiz-start').mouseenter(function(e) {
        $(this).animate({
            bottom: "0px"
        }, 200);

    }).mouseleave(function() {
        $(this).animate({
            bottom: "-132px"
        }, 200);
    });

    $('#grid-motivators .close').click(function() {
        $.cookie("lelaMDisp", "1", { path: '/', expires: 7 });
        $('#grid-motivators').fadeOut('fast');
    });

    if($.cookie("lelaMDisp") && $.cookie("lelaMDisp") == '1') {
        $('#grid-motivators').hide();
    }
    else {
        $('#grid-motivators').show();
    }

    if(itemsInComparisonCategory($('.compare-check:first').attr('cat')).length > 0) {
        var cat = $('.compare-check:first').attr('cat');

        $('.compare-btn').show().removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+cat);

        var items = itemsInComparisonCategory(cat);
        for(var i=0;i<items.length;i++) {

            if(!$('#compare-items div.c-btn'+i).hasClass('c-btn-checked')) {
                $('#compare-items div.c-btn'+i).addClass('c-btn-checked').attr('cat',items[i].ctgry).attr('rlnm',items[i].rlnm);

                $('#c-btn'+i+'-overlay img').remove();
                $('<img />').attr('src', items[i].img).insertBefore($('#c-btn'+i+'-overlay .c-btn'));
                $('#c-btn'+i+'-overlay .c-btn').attr('cat',items[i].ctgry).attr('rlnm',items[i].rlnm).click(function() {
                    removeComparisonItem($(this).attr('cat'), $(this).attr('rlnm'));
                });

                $('#compare-items div.c-btn'+i).mouseenter(function() {
                    $('#compare-items .c-btn-overlay').hide();
                    $('#compare-items .c-btn-overlay[num="'+$(this).attr('num')+'"]').show().mouseleave(function() { $(this).hide(); });
                });
            }

        }

    }
    else
    {
        $('.compare-btn').attr("href", "#").addClass('waiting');
    }

    /*
    $('.filter-option-truncated').each(function() {

        if($(this).attr('title') != $(this).html()) {

            var truncated = $(this).html();

            $(this).hover(function() {
                $(this).html($(this).attr('title'));
            }, function() {
                $(this).html(truncated);
            });

        }

    });
    */

    $('.compareButton').live('click', function() {
        if($(this).find('.c-btn').hasClass('c-btn-checked')) {
            $(this).find('.c-btn').removeClass('c-btn-checked');
            $('.compare-check[rlnm="'+$(this).attr('rlnm')+'"]').hide().find('.c-btn').removeClass('c-btn-checked');
            $('#compare-items .c-btn-grid[rlnm="'+$(this).attr('rlnm')+'"]').removeClass('c-btn-checked').unbind('mouseenter');
            removeItemFromComparisonCookie($(this).attr('cat'),$(this).attr('rlnm'));

            if(itemsInComparisonCategory($(this).attr('cat')).length > 0) {
                $('.compare-btn').removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$(this).attr('cat')).fadeIn(100);
            }
            else {
                $('.compare-btn').attr("href", "#").addClass('waiting');
            }
        }
        else {
            var img = $('li#'+$(this).attr('rlnm')+' a.p-img img');
            if(addItemToComparisonCookie($(this).attr('cat'), $(this).attr('rlnm'), img.attr('src'))) {
                $(this).find('.c-btn').addClass('c-btn-checked');
                $(this).parent().show();
                $('.compare-check[rlnm="'+$(this).attr('rlnm')+'"]').show().find('.c-btn').addClass('c-btn-checked');

                for(var i=0;i<$('#compare-items .c-btn').length;i++) {
                    if(!$('#compare-items .c-btn-grid').eq(i).hasClass('c-btn-checked')) {
                        $('#compare-items .c-btn-grid').eq(i).addClass('c-btn-checked').attr('cat',$(this).attr('cat')).attr('rlnm',$(this).attr('rlnm'));

                        $('#c-btn'+i+'-overlay img').remove();
                        $('<img src="'+img.attr('src')+'" />').insertBefore($('#c-btn'+i+'-overlay .c-btn'));
                        $('#c-btn'+i+'-overlay .c-btn').attr('cat',$(this).attr('cat')).attr('rlnm',$(this).attr('rlnm')).click(function() {
                            removeComparisonItem($(this).attr('cat'), $(this).attr('rlnm'));
                        });

                        $('#compare-items .c-btn-grid').eq(i).mouseenter(function() {
                            $('.c-btn-overlay').hide();
                            $('#c-btn'+i+'-overlay').show();
                            $('#c-btn'+i+'-overlay').mouseleave(function() { $(this).hide(); });
                        });
                        break;
                    }
                }

                if(itemsInComparisonCategory($(this).attr('cat')).length > 0) {
                    $('.compare-btn').removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$(this).attr('cat')).fadeIn(100);
                }
            }
            else {
                $("#comparison-alert").show();
                $("#comparison-alert-trigger").fancybox({
                    'hideOnContentClick': true
                }).trigger('click');

            }
        }
    });

    updateComparisonCheckboxes();

    // Bind product grid mouse over details
    $('.item').live('mouseenter', function(e) {

        $(this).find('.lela-rating').mouseover(function() {
            var src = $(this).attr('src');
            src = src.replace('rating', 'rating_over');
            $(this).attr('src', src);
        }).mouseout(function() {
            var src = $(this).attr('src');
            src = src.replace('rating_over', 'rating');
            $(this).attr('src', src);
        });

        var motivatorBox = {
            'onStart'		: function() {
                initCompatibilityColumns();
                $('#fancybox-close').css('background', 'url("'+ baseUrl +'static/images/buttons/modal_close_or.png") repeat scroll 0 0 transparent');
                trackEvent('user', 'access', 'What Matters Info', 1);
            },
            'onClosed'      :  function() {
                $('#fancybox-close').css('background', 'url("'+ baseUrl +'static/images/buttons/modal_close.png") repeat scroll 0 0 transparent');
            }
        };

        //$('#l-quiz-done').fancybox(motivatorBox);
        $('.mot-l').click(function (e) {
            e.preventDefault();
            $('#grid-motivators').slideDown('fast');
            $('html, body').animate({ scrollTop: $("#grid-motivators").offset().top }, 'slow');
            $.cookie("lelaMDisp", "0", { path: '/', expires: 7 });
        });

        e.stopPropagation();

        $('.item-over').hide();

        $(this).find('.item-over').insertAfter($(this));
        var position = $(this).position();
        var left = position.left;
        var top = position.top;
        $(this).next('.item-over').css( {
            left: left,
            top: top
        }).show(0, function() {

            // Bind color toggle functions
            $(this).find('.color-img a, .color-sw a').click(function(e) {
                $(this).parent().find('a').removeClass('selected');
                $(this).addClass('selected');
                $(this).parents('.item-over').find('.p-img img').attr('src', $(this).attr('href'));
                $(this).parents('.item-over').find('.p-img').attr('href', $(this).attr('itemUrl'));

                return false;
            });

            // Bind Carousel
            $(this).find('.c-prev, .c-next').unbind('click');
            
            $(this).find('.c-prev').click(function(e) {

                $(this).next().find('.selected').prev('a').trigger('click');
                var index = $(this).next().find('.selected').index();

                if((index+1) % 3 == 0) {
                    $(this).next().children('.color-img, .color-sw').animate({
                        left: "+=167px"
                    }, 500);
                }
                return false;
            });

            $(this).find('.c-next').click(function(e) {

                $(this).prev().find('.selected').next('a').trigger('click');
                var index = $(this).prev().find('.selected').index();
                if((index) % 3 == 0) {
                    $(this).prev().children('.color-img, .color-sw').animate({
                        left: "-=167px"
                    }, 500);
                }

                return false;
            });

        }).mouseleave(function(e) {

            $(this).hide();

            // Reset Carousel
            $(this).find('.color-img').css( { left: 0 });

            // Restore original image in rollover
            $(this).find('.color-img a.selected').removeClass('selected');
            $(this).find('.color-sw a.selected').removeClass('selected');
            $(this).find('.color-img a:first').addClass('selected');
            var originalImg = $(this).prev('.item').find('.p-img img').attr('src');
            $(this).find('.p-img img').attr('src', originalImg);
        });
    });


    // if motivators are displayed, divide the explanations equally
    if($('#page-motivators')) {
        var listArr = new Array();
        var $list = $('ul#explanation-data');

        $list.find('li').each(function() {
            listArr.push($(this).html());
        });

        var firstList = listArr.splice(0, Math.round(listArr.length / 2));
        var secondList = listArr;
        ListHTML = '';
        function createHTML(list){
            ListHTML = '';
            for (var i = 0; i < list.length; i++) {
                ListHTML += '<li>' + list[i] + '</li>'
            };
        }
        createHTML(firstList);
        $('#expl-left').html(ListHTML);
        createHTML(secondList);
        $('#expl-right').html(ListHTML);

    }

    // display a quiz promo box on a random item
    if($('#item-quiz-promo') && $('#item-quiz-promo').length > 0) {
        var itemPromo = $('#item-quiz-promo');
        var rand = Math.round(Math.random()*$('#p-grid li.item').length - 1);
        if(rand < 0) rand = 0;
        var randomItem = $('#p-grid li.item').eq(rand);

        itemPromo.css(randomItem.position()).fadeIn('fast').mouseleave(function() { $(this).fadeOut('fast'); });
    }

});

function displayPopup(secs) {
    setTimeout(function() {
        if(!$.cookie("lelaPopup")) {
            $.cookie("lelaPopup", "1", { path: '/', expires: 1 });
        }
        $('#reg-popup').show();
        $("#regPopupTrigger").fancybox({
            centerOnScroll: true,
            overlayOpacity: 0.75,
            overlayColor: '#000000',
            hideOnOverlayClick: false,
            onClosed: function() {
                $('#reg-popup').hide();
            },
        }).trigger('click');
    }, (secs*1000));
}

function initTuners() {

    window.filterChange = false;
    window.sortChange = true;

    $('#d-tuners').html('');
    $('#d-tuners').append($('#d-tuners-tmp'));
    //$('#d-tuners').jqTransform();
    $('#ctrls-wrapper').append($('#data-sort'));

    if($('#amount_min').length > 0) {
        // Range Slider Filter Init
        var min = Number($( "#amount_min" ).val().replace('$',''));
        var max = Number($( "#amount_max" ).val().replace('$',''));
        var range_min = Number($( "#range_min" ).val());
        var range_max = Number($( "#range_max" ).val());
    }

    // expand/collapse toggles for tuner headers
    $('.filter-header').click(function () {
        var c = $(this).parent().find('.filter-content');
        if(c.css('display') == 'none') {
            c.slideDown('fast');
            $(this).find('.openclose').removeClass('collapsed');
            $(this).removeClass('filter-header-collapsed');
            $(this).find('.info').show();
        }
        else {
            c.slideUp('fast');
            $(this).find('.openclose').addClass('collapsed');
            $(this).addClass('filter-header-collapsed');
            $(this).find('.info').hide();
        }
    });

    $('#d-tuners .t-check .filter-option').click(function(e) {

        e.preventDefault();

        if($(this).parent().hasClass('filter-disabled')) {
            e.preventDefault();
            return true;
        }
        else {
            var activeCheckbox = $(this).next('.filter-hidden-input');

            // Make checkbox behave like a radio button (single answer)
            if($(this).parents('.t-check').hasClass('single-check')) {
                $(this).parents('.t-check').find('.jqTransformCheckbox').each(function(){
                    if(activeCheckbox.attr('id') != $(this).next('input').attr('id')) {
                        $(this).removeClass('jqTransformChecked');
                        $(this).parent().find('.filter-option').removeClass('filter-option-selected');
                        $(this).next('input').attr('checked', false);
                    }
                });
            }

            if($(this).hasClass('filter-option-selected')) {
                $(this).removeClass('filter-option-selected');
                activeCheckbox.removeAttr('checked');
            }
            else {
                $(this).addClass('filter-option-selected');
                activeCheckbox.attr('checked', 'checked');
            }

            window.filterChange = true;

            if(!activeCheckbox.attr('checked')) {
                trackEvent('user', 'filters', activeCheckbox.attr('name'), 1);
            }

            // Set the last filter active for hiding filters
            lastFilterActive = $(this).parents('.f-sec').attr('key');

            var t = setTimeout("submitTuners('replace')", 100);
        }


    });

    var slider = $( "#slider-range" ).slider({
        range: true,
        min: range_min,
        max: range_max,
        step: 1,
        values: [ min, max ],
        slide: function( event, ui ) {
            $( "#amount_min" ).val( "$" + ui.values[ 0 ]);
            $( "#amount_max" ).val( "$" + ui.values[ 1 ]);
        },
        stop: function(event, ui) {
            window.filterChange = true;
            trackEvent('user', 'price range', 'high', Number(ui.values[ 1 ]));
            trackEvent('user', 'price range', 'low', Number(ui.values[ 0 ]));
            submitTuners('replace');
            $('#p-filters h2').addClass('filters-enabled');
        }
    });


    // BEGIN Update slider when field is updated
    $( "#amount_min" ).data('timeout', null).keyup(function() {
        window.filterChange = true;
        var value = Number($( "#amount_min" ).val().replace(/[^0-9]+/g,''));
        clearTimeout($(this).data('timeout'));
        $(this).data('timeout', setTimeout(function() {

            if(value < Number($( "#range_min" ).val())) {
                value = $( "#range_min" ).val();
            } else if (value >= Number($( "#amount_max" ).val().replace('$',''))) {
                value = Number($( "#amount_max" ).val().replace('$','')) - 10;
            }

            slider.slider( 'values', 0, value );
            $( "#amount_min" ).val('$'+String(value));
            submitTuners('replace');

        }, 1000));
    });

    $("#amount_max").data('timeout', null).keyup(function() {
        window.filterChange = true;
        var value = Number($( "#amount_max" ).val().replace(/[^0-9]+/g,''));
        clearTimeout($(this).data('timeout'));
        $(this).data('timeout', setTimeout(function() {

            if(value > Number($( "#range_max" ).val())) {
                value = $( "#range_max" ).val();
            } else if (value <= Number($( "#amount_min" ).val().replace('$',''))) {
                value = Number($( "#amount_min" ).val().replace('$','')) + 10;
            }

            slider.slider( 'values', 1, value );
            $( "#amount_max" ).val('$'+String(value));
            submitTuners('replace');

        }, 1000));
    }); // END Update slider when field is updated

    $( "#amount_min" ).val( "$" + $( "#slider-range" ).slider( "values", 0 ));
    $( "#amount_max" ).val( "$" + $( "#slider-range" ).slider( "values", 1 ));

    $('.reset-tuners').click(function() {
        // Show any conflicting filters that had been hidden
        $('.filter-disabled').removeClass('filter-disabled');

        $('.d-sec').slideUp();
        $('.filter-toggle').find('.show-all').show();
        $('.filter-toggle').find('.hide-all').hide();

        // Reset checkbox graphics
        $(".filter-option-container").each(function(){
            $(this).removeClass('filter-disabled').find('a.filter-option').removeClass('filter-option-selected');
        });

        // Reset checkbox inputs
        $('#d-tuners input').each(function() {
            $(this).attr('checked', false);
        });

        // Reset price range
        $( "#amount_min" ).val("$" + range_min);
        $( "#amount_max" ).val("$" + range_max);
        $( "#slider-range" ).slider({
            range: true,
            min: range_min,
            max: range_max,
            step: 1,
            values: [ range_min, range_max ],
            slide: function( event, ui ) {
                $( "#amount_min" ).val( "$" + ui.values[ 0 ]);
                $( "#amount_max" ).val( "$" + ui.values[ 1 ]);
            },
            stop: function(event, ui) {
                window.filterChange = true;
                submitTuners('replace');
            }
        });

        window.filterChange = true;
        $('#p-filters h2').removeClass('filters-enabled');

        submitTuners('replace');
        return false;
    });

    // Init Sort-by dropdown
    $('#sort-form select').jqTransSelect();
    $('.jqTransformSelectOpen').click(function () {
        initModal();
    });

    $("#sort-form div.jqTransformSelectWrapper ul li a").click(function(){
        $.cookie("_lela-item-sort", $(this).attr('value'), { path: '/', expires: 7 });
        window.filterChange = true;
        submitTuners('replace');
        return false;
    });

    if(typeof openBrand !== 'undefined' && openBrand && openBrand.length > 0 && openBrand == 'false') {
       $('.filter-container[fltr="by.brand"] .closeopen').trigger('click');
    }

    if(typeof openStore !== 'undefined' && openStore && openStore.length > 0 && openStore == 'false') {
       $('.filter-container[fltr="by.store"] .closeopen').trigger('click');
    }
}

function submitTuners(type) {

    if(type == 'replace') {
        window.pageNum = 0;
    }

    filterQuery = {};
    // filterQuery.size = window.pageSize;
    filterQuery.size = 12;
    filterQuery.page = window.pageNum;
    // if we have the sort cookie set, use that
    if($.cookie('_lela-item-sort')) {
        filterQuery.sortBy = $.cookie('_lela-item-sort');
    }
    else {
        filterQuery.sortBy = $('#sort-form a.selected').attr('value');
    }
    filterQuery.update = window.filterChange;
    filterQuery.sort = window.sortChange;
    filterQuery.filters = {};

    emptyFilters = true;

    $('.f-sec').each(function() {
        var key = $(this).attr('key');
        filterQuery.filters[key] = [];
        filterQuery.filters[key] = {};

        if($(this).attr('id') == 'price-range') {
            filterQuery.filters[key].high = $(this).find('#amount_max').val().replace('$','');
            filterQuery.filters[key].low = $(this).find('#amount_min').val().replace('$','');
        } else {

            $(this).find('.filter-option-container').each(function() {
                if ($(this).find('input:checked').val() !== undefined) {
                    var tKey = $(this).attr('option').split(/\s+/)[0];
                    $(this).find('input').val()
                    filterQuery.filters[key][tKey] = $(this).find('input').val();
                    emptyFilters = false;
                }
            });
        }
    });

    if($('#item-quiz-promo') && $('#item-quiz-promo').length > 0 && $('#item-quiz-promo').is(':visible')) {
        $('#item-quiz-promo').fadeOut('fast');
    }

    if(emptyFilters == true) {
        $('#p-filters h2').removeClass('filters-enabled');
    }
    else {
        $('#p-filters h2').addClass('filters-enabled');
    }

    var json = JSON.stringify(filterQuery);

    $.ajax({
      cache: false,
      url: dataUrl,
      type: 'POST',
      contentType: 'application/json; charset=utf-8',
      data : json,
      dataType : 'html',
      beforeSend: function( xhr ) {
        if(type == 'replace') {
            // $('#main').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
        }
      },
      error: function( jqXHR, textStatus, errorThrown ) {
        // TBD  
      },
      success: function( data ) {

        window.filterChange = false;
        // $('#p-filters').stickySidebar();

        if(type == 'append') {

            $('#p-grid').append(data);
            updateComparisonCheckboxes();

            // init inifite Scrolling - TB Completed
            if(data) {
                window.loading = false;
            } else {
                window.loading = true;
            }

        } else if(type == 'replace') {
            $('#p-grid').fadeOut(100, function() {
                $('#p-grid').html(data)
                $('#p-grid').fadeIn('fast');

                if(window.itemNum) {
                    var count = $('#p-grid').find('.item').length;
                    $('#num-total').html(window.itemNum['total']+' ');
                    $('#num-count').html(count+' ');
                }
            });
        }

        $.waypoints('refresh');

      }
    });
}

function initPagination(type) {
    // init inifite Scrolling - TB Completed
    if(type == 'enable') {
        window.loading = false;

        $('#footer').waypoint(function(event, direction) {
            if (window.itemNum['count'] > 0 && window.pageNum >= 0 && !window.loading && direction === 'down') {
                window.pageNum++;
                $('#back-top').show();
                submitTuners('append');
            }
        }, {
            offset: '100%'
        });

    } else if(type == 'disable') {
        $('#footer').waypoint('destroy');
    }
}

function processAvailableFilters() {

    // Check for hiding conflicting filters
    if (window.availableFilters) {

        // Run through each filter that has had its available options defined in the
        // search results as a JSON structure
        for (var filterKey in window.availableFilters) {

            // For any filter group of options, disable all of them except the ones specifically defined
            // UNLESS the last click was in a filter section that represents a search OR or radio
            //if (filterKey != lastFilterActive) {

//                if ($("div[key='" + filterKey + "'] input:checked").length == 0 || !orFilter(window.availableFilters[filterKey].type)) {
//                    // Hide all current options for the filter
//                    var allSelector = "div[key='" + filterKey + "'] label";
//                    hideFilters(allSelector);
//              //  }
//            } else {
//                // DO NOTHING IN THIS CASE... this is effectively an OR filter and
//                // clicking additional options will increase the result set not reduce it
//                // so we can safely ignore clicks in the same group
//            }

            if (orFilter(window.availableFilters[filterKey].type) && $("div[key='" + filterKey + "'] input:checked").length > 0) {
                // DO NOTHING IN THIS CASE... this is effectively an OR filter and
                // clicking additional options will increase the result set not reduce it
                // so we can safely ignore clicks in the same group
                var allSelector = "div[key='" + filterKey + "'] .filter-option-container";
                //$(allSelector).removeClass("filter-disabled");
                $(allSelector).find('input').removeAttr('disabled');
            } else {
                // Hide all current options for the filter
                var allSelector = "div[key='" + filterKey + "'] .filter-option-container";
                hideFilters(allSelector);
            }

            // For each available options returned for the set of search results, display the filter
            for (var i=0; i < window.availableFilters[filterKey].available.length; i++) {
                var optionKey = window.availableFilters[filterKey].available[i];

                var selector = "div[key='" + filterKey + "'] .filter-option-container[option='"+ optionKey +"']";
                $(selector).removeClass("filter-disabled");
                $(selector).find('input').removeAttr('disabled');
            }

            // Can't hide checked ones
            // If we leave checked ones hidden we get in a bad state where we can't get results
            // If we don't hide them then we can cause zero result conditions by UN-checking a valid
            // option, leaving something that should have been hidden
            $("div[key='" + filterKey + "'] input:checked").parents(".filter-option-container").removeClass("filter-disabled");
            $("div[key='" + filterKey + "'] input:checked").removeAttr('disabled');
        }
    }
}

function orFilter(type) {
    return type == 'MULTIPLE_CHOICE_MULTIPLE_ANSWER_OR' ||
            type == 'MULTIPLE_CHOICE_SINGLE_ANSWER' ||
            type == 'BRAND' ||
            type == 'STORE'
}

function hideFilters(selector) {
    $(selector).addClass("filter-disabled");
    $(selector).find('input').attr('disabled', 'disabled');
}

function removeComparisonItem(cat,item) {

    var i = $('.c-btn-grid[rlnm="'+item+'"]').attr('num');

    $('#c-btn'+i+'-overlay').fadeOut(100);
    $('#compare-items .c-btn-grid[num="'+i+'"]').removeClass('c-btn-checked').unbind('mouseenter');
    $('.compareButton[rlnm="'+item+'"]').find('.c-btn').removeClass('c-btn-checked');
    $('.compare-check[rlnm="'+item+'"]').hide().find('.c-btn').removeClass('c-btn-checked');
    removeItemFromComparisonCookie(cat,item);

    if(itemsInComparisonCategory(cat).length > 0) {
        $('.compare-btn').removeClass('waiting').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+cat).fadeIn(100);
    }
    else {
        $('.compare-btn').attr("href", "#").addClass('waiting');
    }
}

