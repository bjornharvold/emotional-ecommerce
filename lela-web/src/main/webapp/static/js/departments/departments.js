/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

$().ready(function($) {

    var partnerCarousel = carousel('marquee-carousel', 3000, 400, '', false);

    if($('.department-filter').length > 0) {

        $('#count-filters .filter-container .filter-header').click(function() {
            var el = $(this).parent().find('.filter-content');
            var cel = $(this).find('.closed');
            var oel = $(this).find('.open');

            if(el.is(':visible')) {
                cel.show();
                oel.hide();
                el.slideUp('fast', function() {
                    $('#count-filters .filter-container').removeClass('activedd');
                });
            }
            else {
                $('#count-filters .filter-container').removeClass('activedd');
                $(this).parent().addClass('activedd');
                $('#count-filters .filter-content').hide();
                $('#count-filters .open').hide();
                $('#count-filters .closed').show();
                el.slideDown('fast');
                cel.hide();
                oel.show();
            }
        });

        // expand/collapse toggles for tuner headers
        $('#dpt-results .filter-header').click(function () {
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

        //$('#dpt-results .filter-container[fltr="by.brand"] .filter-header').click();
        //$('#dpt-results .filter-container[fltr="by.store"] .filter-header').click();

        function updateFilterCount() {
            var elcount = $('#active-filters .active-filter').length - 1;
            $('#active-filter-count strong').html(elcount);
            if(elcount > 0) $('#active-filter-count').fadeIn('fast');
            else $('#active-filter-count').hide();
        }

        $('#active-filter-count .remove').click(function() {
            $('#active-filters .active-filter .remove').click();
            updateFilterCount();
        });

        $.each($('#count-filters .filter-container-category .filter-options input'), function() {
            availableCategoryUrlNames.push($(this).attr('name'));
        });

        var df = new Filter('#count-filters', dataUrl, "html"); // department landing filter
        var dr = new Filter('#d-tuners', resultDataUrl, "html"); // department landing results filter

        df.jsonCallback = function(filterQuery) {

            filterQuery.rlnm = currentDepartment;
            filterQuery.availableCategoryUrlNames = availableCategoryUrlNames;
            filterQuery.categoryUrlNames = [];

            $.each($('#count-filters .filter-container-category .filter-options input:checked'), function() {
                filterQuery.categoryUrlNames.push($(this).attr('name'));
            });

            return JSON.stringify(filterQuery);
        };

        df.changedCallback = function(data) {

            var ky = $(data).parents('.lela-filter').attr('key');
            var vl = $(data).next('input').attr('name');

            if($(data).hasClass('filter-option-selected')) {
                var copyel = $('#active-filters .hidden').clone().appendTo('#active-filters').removeClass('hidden').hide().fadeIn('fast');
                copyel.attr('ky',ky);
                copyel.attr('vl',vl);
                copyel.find('span').html($(data).parents('.filter-container').find('h3').html()+': '+$(data).html());
                copyel.find('.remove').click(function() {
                    $('#count-filters .lela-filter[key="'+ky+'"] .filter-option-container[option="'+vl+'"] a').click();
                });

                $('#d-tuners .lela-filter[key="'+ky+'"] .filter-option-'+vl).addClass('filter-option-selected').next('input').attr('checked', 'checked');
            }
            else if($(data).hasClass('filter-option')) {
                $('#active-filters .active-filter[ky="'+ky+'"][vl="'+vl+'"]').remove();
                $('#d-tuners .lela-filter[key="'+ky+'"] .filter-option-'+vl).removeClass('filter-option-selected').next('input').removeAttr('checked');
            }
            else {
                if(data[0][0] == '$') {
                    if(data[0] != '$'+df.range_min || data[1] != '$'+df.range_max) {
                        if($('#active-filters .active-filter[ky="price-range"]').length > 0) {
                            $('#active-filters .active-filter[ky="price-range"] span').html('Budget: '+data[0]+' - '+data[1]);
                        }
                        else {
                            var copyel = $('#active-filters .hidden').clone().appendTo('#active-filters').removeClass('hidden').hide().fadeIn('fast');
                            copyel.attr('ky','price-range');
                            copyel.attr('vl',data);
                            copyel.find('span').html('Budget: '+data[0]+' - '+data[1]);
                            copyel.find('.remove').click(function() {
                                $(df.filterContainer +' .amount_min').val(df.range_min).keyup();
                                $(df.filterContainer +' .amount_max').val(df.range_max).keyup();
                                copyel.remove();
                            });
                        }
                    }
                    else if(data[0] == '$'+df.range_min && data[1] == '$'+df.range_max) {
                        $('#active-filters .active-filter[ky="price-range"]').remove();
                    }

                    //slider.slider( 'values', 0, value );
                    $('#d-tuners .amount_min').val(String(data[0]));
                    $('#d-tuners .amount_max').val(String(data[1]));

                }
            }

            $('#count-loading').fadeIn('fast');

            updateFilterCount();

        };

        df.successCallback = function(data) {

            $('#count-loading').fadeOut('fast');
            var data = $.parseJSON(data);

            if(data.count > 0) {
                $('#search-department-button').removeClass('search-disabled').fadeIn('fast').unbind('click').click(function(c) {
                    c.preventDefault();
                    $('#dpt-filters').slideUp('fast');
                    $('#dpt-results').slideDown('fast', function() {
                        dr.init();
                        dr.submit();
                    });
                });
            }
            else {
                $('#search-department-button').addClass('search-disabled').fadeIn('fast').unbind('click');
            }

            $('#count .count-number').html(data.count);
            $('#count').fadeIn('fast');

        };

        dr.jsonCallback = function(filterQuery) {

            filterQuery.rlnm = currentDepartment;
            filterQuery.availableCategoryUrlNames = availableCategoryUrlNames;
            filterQuery.categoryUrlNames = [];

            var c = 0;

            $.each($('#d-tuners .filter-container-category .filter-options input:checked'), function() {
                filterQuery.categoryUrlNames.push($(this).attr('option'));
                c++;
            });

            if(c==0) {
                filterQuery.categoryUrlNames = availableCategoryUrlNames;
            }

            return JSON.stringify(filterQuery);
        };

        dr.changedCallback = function(data) {
            $('#grid-loading').fadeIn('fast');
        };

        dr.successCallback = function(data) {
            $('#d-grid').html(data);
            $('#grid-loading').fadeOut('fast');
        };


        df.init();
        df.submit();

    }

});
