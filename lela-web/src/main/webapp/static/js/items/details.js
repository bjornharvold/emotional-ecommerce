/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

function updateStoreCounts() {
    $('#buy-t #buy-tab1 .store-count').html('('+$('#buy-tbl-online tbody tr').length+')');
    $('#buy-t #buy-tab2 .store-count').html('('+$('#loc-data tbody tr').length+')');
    $('#buy-t #buy-tab3 .store-count').html('('+$('#buy-tbl-favorite tbody tr').not('tr.no-favorite-stores').length+')');
}

$().ready(function($) {

    // don't execute the details page specific stuff on local stores page
    if(!$('.lela-page-container').hasClass('page-local-stores')) {

        fetchReviews();

        function hideSharePopup() {
            $('#share-icons-trigger').removeClass('active');
            $('#share-icons').fadeOut('fast');
        }
        $('#share-icons-trigger').mouseover(function() {
            $(this).addClass('active');
            $('.ac-tip').hide();

            hideshare = setTimeout(function() { hideSharePopup(); }, 3000);
            $('#share-icons').fadeIn('fast').mouseover(function() {
                clearTimeout(hideshare);
                $(this).mouseleave(function() { hideSharePopup(); });
            });
        }).click(function () {
            if($('#share-icons').is(':visible')) {
                hideSharePopup();
            }
            else {
                clearTimeout(hideshare);
                $('#share-icons-trigger').addClass('active');
                $('#share-icons').fadeIn('fast');
            }
        });

        $('.spec-div:last').remove();

        $.waypoints.settings.scrollThrottle = 10;
        $('#p-nav-wrap').waypoint(function(event, direction) {

            if (direction === 'down' && $('#p-nav').hasClass('sticky') == false) {
                $('#p-nav').addClass('sticky');
                $('#p-nav').parent('#p-nav-wrap').next('#item-content').addClass('sticky');
            } else if (direction === 'up' && $('#p-nav').hasClass('sticky')) {
                $('#p-nav').removeClass('sticky');
                $('#p-nav').parent('#p-nav-wrap').next('#item-content').removeClass('sticky');
            }
        });

        if(window.location.hash == "#prices") {
            var target_offset = $('#p-details').offset();
            var target_top = (target_offset.top) - 100;
            $('html, body').delay(1000).animate({scrollTop:target_top}, 500);
        }
        else if(window.location.hash == "#compatibility") {
            var target_offset = $('#p-comp').offset();
            var target_top = (target_offset.top) - 100;
            $('html, body').delay(1000).animate({scrollTop:target_top+207}, 500);
        }

        $('.nav-section').waypoint(function(event, direction) {

            var active = $(this);
            if (direction === "up" && $(this).attr('data-id') != "p-item") {
                var index = $('.nav-section').index($(this)) - 1;
                active = $($('.nav-section').get(index));
            }

            if (!active.length) active = active.end();

            $('#p-nav a.p-an').removeClass('selected');
            $('#'+active.attr('data-id')+'-nav').addClass('selected');

            if(direction == 'down' && $(this).attr('data-id') != 'head') {
                trackEvent('item', 'pd-scroll', $(this).attr('data-type'), 1);
            }

            event.stopPropagation();
        },
        {
            offset: '50%'
        });

        $('a.p-an, .relevancy-mod a').click(function() {

            var id = $(this).attr('href');
            var target_offset = $(id).offset();
            var target_top = target_offset.top;

            if($('#p-nav').hasClass('sticky')) {
                target_top = target_top - 70;
            } else {
                target_top = target_top - 110;
            }

            if(id == '#p-item') {
                target_top = target_top - 50;
            }

            $('html, body').animate({scrollTop:target_top}, 500, function() {

            });


            return false;
        });

        // Add store numbers
        updateStoreCounts();


        initCompatibilityColumns();

        // Update counts when clicked on the
        //$('.save-now').click(function() { setTimeout(updateFavorites, 100); });
        //$('.remove-now').click(function() { setTimeout(updateFavorites, 100); });

        $('.lela-pct').mouseover(function() {
            var src = $(this).attr('src');
            src = src.replace('rating', 'rating_over');
            $(this).attr('src', src);
        }).mouseout(function() {
            var src = $(this).attr('src');
            src = src.replace('rating_over', 'rating');
            $(this).attr('src', src);
        });



        // Show online stores by default
        $('#buy-t #buy-tab1').addClass('selected');
        $('#buy-tbl-online').show();


        $('span#buy-tab3').click(function(e) {
            e.preventDefault();
            window.location = $('#nav-register').attr('href')+'?type=save&rt='+encodeURI(window.location.pathname)+encodeURI(window.location.search);
            return false;
        });

        $(".trigger").tooltip({
            position: "middle right",
            relative: true,
            events: {
                // def:     "click,mouseout"
            }
        });

        $(".trigger-rel").tooltip({
            position: "bottom right",
            offset: [0,-50],
            relative: true
        });

        $('.compare-check').click(function() {
            if(itemInComparisonCookie($('#l-comparison').attr('rlnm'))) {
                removeItemFromComparisonCookie($(this).attr('cat'),$(this).attr('rlnm'));
                $('.compare-check .c-btn').removeClass('c-btn-checked');
                $('#l-comparison').removeClass('l-compare-saved');
                $('#l-comparison span').html(itemsInComparisonCategory($('#l-comparison').attr('cat')).length);
                updateComparisonButton();
            }
            else {
                if(addItemToComparisonCookie($(this).attr('cat'),$(this).attr('rlnm'),$('#product-img .pr-img').attr('src'))) {
                    $('.compare-check .c-btn').addClass('c-btn-checked');
                    $('#l-comparison').addClass('l-compare-saved');
                    $('#l-comparison span').html(itemsInComparisonCategory($('#l-comparison').attr('cat')).length);
                    updateComparisonButton();
                } else {
                    $("#comparison-alert").show();
                    $("#comparison-alert-trigger").fancybox({
                        'hideOnContentClick': true
                    }).trigger('click');
                }
            }
        });

        updateComparisonButton();

        if(itemInComparisonCookie($('#l-comparison').attr('rlnm'))) {
            var item=$(this).attr('rlnm');
            var cat=$(this).attr('cat');
            $('#l-comparison').addClass('l-compare-saved');
            $('.compare-check .c-btn').addClass('c-btn-checked');
        }
        else {
        }

        $('.more-deals').fancybox();

        // init item video links
        $('.vid-l').click(function() {
            $.fancybox({
                'padding'		: 40,
                'autoScale'		: false,
                'title'			: this.title,
                'width'			: 640,
                'height'		: 385,
                'href'			: this.href.replace(new RegExp("watch\\?v=", "i"), 'v/'),
                'type'			: 'swf',
                'swf'			: {
                    'wmode'				: 'transparent',
                    'allowfullscreen'	: 'true'
                }
            });

            return false;
        });

        // init store tabs
        $('#buy-t a').live('click', function(ev) {
            ev.preventDefault();

            var target_offset = $('#p-details').offset();
            var target_top = target_offset.top-60;
            $('html, body').animate({scrollTop:target_top}, 500);

            // Switch Tab
            var tabId = $(this).attr('href');
            $('.buy-content').hide();
            $(tabId).show();

            // Load local data when tab is clicked
            if(tabId == '#loc-data') {
                loadMap(locations);
            }

            // Set selected class
            $('#buy-t a').removeClass('selected');
            $(this).addClass('selected');

            trackEvent('item', 'details', $(this).attr('data-type'), 1);

            return false;
        });

        $('.color-img a').click(function(ev) {
            ev.preventDefault();

            // Switch Tab
            $('.color-img a').removeClass('selected');
            $(this).addClass('selected');

            if($(this).hasClass('vid-l') != true) {
                var img = $(this).attr('href');
                $('#product-img img.pr-img').attr('src', img);
            }

            return false;
        });

        $('.color-sw a').mouseover(function(ev) {
            ev.preventDefault();

            // Switch Tab
            $('.color-sw a').removeClass('selected');
            $(this).addClass('selected');

            if($(this).hasClass('vid-l') != true) {
                var img = $(this).attr('imgurl');
                $('#product-img img.pr-img').attr('src', img);
            }

            // Update the color text
            if($(this).attr('clr') != null) {
                $(this).parent('.color-sw').find('.color-name span').text($(this).attr('clr'));
            } else {
                $(this).parent('.color-sw').find('.color-name span').text('');
            }

            return false;
        });

        $('.rating-anim').mouseenter(function () {
            $(this).attr('src', $(this).attr('src').replace('.jpg', '_over.gif'));
        }).mouseleave(function() {
            $(this).attr('src', $(this).attr('src').replace('_over.gif', '.jpg'));
        });

        initList();
        initLocalStores('loc-data');

    }


});

function getLocalStores(data, tabId) {

    var tabId = tabId;
    var data = JSON.stringify(data);
    var url = localStoreUrl;
    if(tabId == 'loc-data-offer') {
        url = url + '?offer=1';
    }


    $.ajax({
        cache: false,
        url: url,
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        data : data,
        dataType : 'html',
        beforeSend: function( xhr ) {
            // TBD
        },
        error: function( jqXHR, textStatus, errorThrown ) {
            // TBD
        },
        success: function( data ) {
            if(!$('.lela-page-container').hasClass('page-local-stores')) {
                $('#'+tabId).html(data);
                initLocalStores('loc-data');
                loadMap(locations);
                updateStoreCounts();
            }
            else {
                window.location = localUrl+'/?zipcode='+$('#zipcode').val();
            }
        }
    });
}

function initLocalStores (tabId) {
    // $('.new-window input').jqTransCheckBox();

    // Bind zipcode form
    $('#zip-f a').click(function() {
        $('#zip-f').submit();
        return false;
    });

    $('#zipcode').focus(function() { $(this).select(); });

    // Submit zipcode form
    $('#zip-f').submit(function() {

        var zipcode = $('#zipcode').val().replace(/[^0-9]+/g,'').substring(0,5);

        if(zipcode.length == 5) {
            data = {};
            data.zipcode = zipcode;
            getLocalStores(data, tabId);
        } else {
            alert('The zipcode you entered is invalid');
            $('#zipcode').focus().focus(function() { $(this).select(); });
        }

        return false;
    });
}


function loadMap (locations) {

    var map = new google.maps.Map(document.getElementById('loc-map'), {
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        panControl: false,
        zoomControl: true,
        scaleControl: true
    });

    var bounds = new google.maps.LatLngBounds();
    var infowindow = new google.maps.InfoWindow();
    var marker, i;

    for (i = 0; i < locations.length; i++) {

        var image = new google.maps.MarkerImage(
            baseUrl + 'static/images/icons/maps/g_map'+ locations[i][3] +'.png',
            new google.maps.Size(33,33),
            new google.maps.Point(0,0),
            new google.maps.Point(33,33)
        );

        var shadowImg = 'shadow.png';
        if(i == 0) {
            shadowImg = 'shadow_l.png';
        }

        var shadow = new google.maps.MarkerImage(
            baseUrl + 'static/images/icons/maps/'+shadowImg,
            new google.maps.Size(53,33),
            new google.maps.Point(0,0),
            new google.maps.Point(33,33)
        );

        var shape = {
            coord: [26,2,27,3,28,4,28,5,28,6,28,7,28,8,28,9,28,10,28,11,28,12,28,13,28,14,28,15,28,16,28,17,28,18,28,19,28,20,28,21,28,22,28,23,27,24,26,25,24,26,23,27,23,28,23,29,23,30,23,31,23,31,22,30,20,29,19,28,18,27,9,26,6,25,5,24,4,23,4,22,4,21,4,20,4,19,4,18,4,17,4,16,4,15,4,14,4,13,4,12,4,11,4,10,4,9,4,8,4,7,4,6,4,5,4,4,5,3,6,2,26,2],
            type: 'poly'
        };

        marker = new google.maps.Marker({
            draggable: false,
            raiseOnDrag: false,
            icon: image,
            shadow: shadow,
            shape: shape,
            position: new google.maps.LatLng(locations[i][1], locations[i][2]),
            map: map
        });

        bounds.extend(marker.position);

        // Open marker
        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infowindow.setContent(locations[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }

    // Autozoom and center map
    if (locations.length == 1) {
        map.setCenter(bounds.getCenter());
        map.setZoom(12);
    } else {
        map.fitBounds(bounds);

        if (locations.length > 10) {
            var listener = google.maps.event.addListener(map, "idle", function() {
                zoomLevel = map.getZoom() + 1;
                map.setZoom(zoomLevel);
                google.maps.event.removeListener(listener);
            });
        }
    }
}

function updateComparisonButton() {
    if(itemsInComparisonCategory($('#l-comparison').attr('cat')).length > 0) {
        $('#l-comparison').addClass('l-compare').removeClass('l-compare-waiting').show();
        $('#l-comparison span').html(itemsInComparisonCategory($('#l-comparison').attr('cat')).length);
        $('#l-comparison').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$('#l-comparison').attr('cat'));
        $('.compare-btn').attr("href", comparisonUrls.comparisonUrl+getComparisonUrl()+"#"+$('#l-comparison').attr('cat'));;
    }
    else {
        $('#l-comparison').addClass('l-compare').removeClass('l-compare-waiting').hide();
        $('#l-comparison span').html('0');
        $('#l-comparison').attr("href", '#');
        $('.compare-btn').attr("href", '#');
    }
}

function fetchReviews() {

    $.ajax({
        cache: false,
        url: reviewUrl,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        data : '',
        dataType : 'html',
        error: function( jqXHR, textStatus, errorThrown ) {
            console.log(jqXHR+" "+textStatus+" "+errorThrown);
        },
        success: function( data ) {
            if(data != 'none') {
                $('#p-reviews').html(data);
                $('#review-img').attr('src', $('#product-img img.pr-img').attr('src'));
                $('#p-reviews-nav').show();
            }
            else {
                $('#p-reviews-nav').hide();
            }
        }
    });

}