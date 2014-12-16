/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    // Load json2.js file for IE6/7 
    if ( $.browser.msie ) {
      if(parseInt($.browser.version, 10) < 8) {
        $.getScript(baseUrl+'static/js/plugins/json2.js');
      }
    }

    // Set Quiz Start Popup Cookies
    if($.cookie('quiz_start') == null || $.cookie('quiz_start') != 'disable') {
        $.cookie('quiz_start', Number($.cookie('quiz_start'))+1, { expires: 2, path: '/' });
    }

    $.ajaxSetup({
      cache: false
    });

    $('.f-submit').click(function() {
       $(this).parents('form').submit(); 
       return false;
    });

    // Setup Main Search Form
    $('#search-form a#s-search').click(function() {
       $('#search-form').submit(); 
    });


    // track merchant links
    $('.buy-now').live('click', function() {
        trackEvent('merchants', 'merchant redirect '+$(this).attr('data-type'), $(this).attr('store'), 1);
    });

    // navigation

    $('.nav-group-subnav a').mouseenter(function() {
        var rlnm = $(this).attr('rlnm');

        if($('.cat-hdr-imgs li img[rlnm='+rlnm+']').is(':hidden')) {

            if($('.cat-hdr-imgs li img[rlnm='+rlnm+']').hasClass('on')) {
                $('.cat-hdr-imgs li img[rlnm='+rlnm+']').removeClass('on').show();
            }
            else {
                $('.cat-hdr-imgs li img[navGroup='+$(this).attr("navGroup")+'].on').removeClass('on');
                $('.cat-hdr-imgs li img[navGroup='+$(this).attr("navGroup")+']').hide();
                $('.cat-hdr-imgs li img[rlnm='+rlnm+']').fadeIn('fast');
            }
        }

    });

    $('.nav-group').mouseenter(function() {
        var e = $(this).find('.nav-group-contents');
        e.slideDown('fast');
        $(this).find('.g-tab').addClass('g-tab-selected');
    }).mouseleave(function() {
            $(this).find('.nav-group-contents').slideUp('fast', function() {
                $(this).parent().find('.g-tab').removeClass('g-tab-selected');
            });

        });

    $('.nav-login-loggedin').mouseenter(function() {
        var e = $(this).find('.menu-container');
        e.slideDown('fast');
        $(this).addClass('nav-login-selected');
    }).mouseleave(function() {
        $(this).find('.menu-container').slideUp('fast', function() {
            $(this).parent().removeClass('nav-login-selected');
        });
    });

    $('.search-categories').mouseenter(function() {
        $(this).find('.menu-container').slideDown('fast');
        $(this).addClass('nav-on');
        $('#cat-dd').addClass('nav-on');
    }).mouseleave(function() {
        $(this).find('.menu-container').slideUp('fast', function() {
            $(this).parent().removeClass('nav-on');
            $('#cat-dd').removeClass('nav-on');
        });
    });

    var keepFocus = false;

    $('.search-categories ul.menu li a').click(function() {
        keepFocus = true;
        $('#s-cat').val($(this).attr('cat'));
        $('.search-categories .menu-container').removeClass('nav-on').slideUp('fast');
        $('#cat-dd').html($(this).html());
        $('#search-term-input').focus();
        return false;
    })

    $('.toggle-search').click(function() {

        if(searchIsDisplayed) {

            searchIsDisplayed = false;
            $('.nav-container').fadeIn('fast');
            $('.search-container').fadeOut('fast');

        } else {

            searchIsDisplayed = true;

            $('.nav-container').fadeOut('fast');
            $('.search-container').fadeIn('fast');

            $('#nav-search input#search-term-input').focus();

            $('#search-term-input').keydown(function() {
                if($('#search-term-input').val() == '') {
                    $('#nav-search-text').hide();
                }
            }).keyup(function() {
                    if($('#search-term-input').val() == '') {
                        $('#nav-search-text').fadeIn('fast');
                    }
                    else {
                        $('#nav-search-text').hide();
                    }
            }).listenForChange()
              .bind('change', function(){ $('#nav-search-text').hide(); });

            $('#nav-search input#search-term-input').focus(function() {
                keepFocus = true;
            }).blur(function() {
                keepFocus = false;
                window.setTimeout(function() {
                    if(keepFocus == false) {
                        searchIsDisplayed = false;
                        $('.nav-container').fadeIn('fast');
                        $('.search-container').fadeOut('fast');
                    }
                }, 500);
            });

        }

    });

    $('.search-submit').hover(function() {
        $(this).attr('src', searchOnButton);
    }, function() {
        $(this).attr('src', searchOffButton);
    });

    initModal();

    // Track quiz open clicks
    $('.lq').live('click', function(){ trackEvent('quiz', 'open', $(this).attr('quiz-location'), 1); });
    
    // Launch quiz when needed on load
    if(window.lq && lq != undefined && !$('.no-quiz')[0]) {
        var t = setTimeout("$(lq).trigger('click')", 500);
    }

    // Rollovers for category list
    if($('#cat-e') && $('#cat-e').length > 0) {
        $('#categories-list .cat a').each(function() {
            $(this).mouseover(function(e) {
                $(this).find('.cat-img-over').fadeIn(200, "easeOutCubic");
                $(this).find('.cat-img').fadeOut(200, "easeInCubic");
            }).mouseleave(function(e) {
                $(this).find('.cat-img-over').fadeOut(200, "easeInCubic");
                $(this).find('.cat-img').fadeIn(200, "easeOutCubic");
            });
        });
    }

});

function launchQuizStart() {
    $('#quiz-start').fadeIn('fast');
    $.cookie('quiz_start', 'disable', { expires: 1, path: '/' });
}

function blockUI(id) {
    $.blockUI({ css: {
        border: 'none', 
        padding: '15px',
        backgroundColor: '#000',
        '-webkit-border-radius': '10px',
        '-moz-border-radius': '10px',
        opacity: .5,
        color: '#fff'
    } });
}

function unblockUI(id) {
    $.unblockUI();
}

function initList() {

    // Bind Lela list events
    $('.l-list').live('click', function(e) {

        $('.ac-tip').hide();

        data = {};
        data.rlnm = $(this).attr('rlnm');

        var link = $(this);
        var id = $(this).attr('rlnm');
        var category = $(this).attr('ctrlnm');
        var method = $(this).attr('method');
        var url = $(this).attr('href');
        var json = JSON.stringify(data);

        $.ajax({
            url: url,
            type: method,
            contentType: 'application/json',
            data : json,
            dataType : 'json',
            success: function( data ) {

                if(method == 'PUT') {
                    // Added to list
                    if(data.message == 1 || data.message == 0) {
                        link.prev().removeClass('hidden');
                        link.addClass('hidden');
                        var num = Number($('#myFavesCount').html()) + 1;
                        $('#myFavesCount').html(num);

                        // Show modal with list card info
                        $.ajax({
                            url: '/list/card/'+id+'?added=1',
                            success: function( data ) {
                                $.fancybox({
                                    'content' : data,
                                    'overlayOpacity' : 0.8,
                                    'overlayColor' : '#000000'
                                });
                            }
                        });

                        // add to favorites store tab
                        if(link.hasClass('l-add-online')) {
                            $('.no-favorite-stores').hide();
                            $('#buy-tbl-favorite thead').show();
                            link.parents('tr').clone().appendTo('#buy-tbl-favorite table tbody');

                            if(typeof(window.updateStoreCounts == 'function')) updateStoreCounts();
                        }

                    } else if (data.message == 0) {
                        // alert('ERROR: This item already belongs to your list');
                    }
                } else if (method == 'POST') {
                    // Removed from list
                    link.next().removeClass('hidden');
                    link.addClass('hidden');
                    var num = Number($('#myFavesCount').html()) - 1;
                    $('#myFavesCount').html(num);
                    $('#remove-fave').fadeIn('slow').delay(3000).fadeOut('slow');

                    // remove from favorites store tab
                    if(link.hasClass('l-remove-online') || link.hasClass('l-remove-favorite') || link.hasClass('l-remove-local')) {

                        if($('#buy-tbl-favorite .buy-store').length == 1) {
                            $('.no-favorite-stores').show();
                            $('#buy-tbl-favorite thead').hide();
                        }

                        $('#buy-tbl-favorite').find('.'+link.parents('tr').attr('class')).remove();
                        $('#buy-tbl-online').find('.'+link.parents('tr').attr('class')+' a.remove-now').addClass('hidden').next('a').removeClass('hidden');
                        if(typeof(window.updateStoreCounts == 'function')) updateStoreCounts();
                    }
                }
            }
        });

        return false;
    });
}

function loadData(pageNum, pageSize, url) {

    // Caching fix for IE
    var tsTimeStamp= new Date().getTime();

    $.ajax({
      url: url,
      data: "page="+pageNum+"&size="+pageSize+"&t="+tsTimeStamp,
      dataType: 'html',
      beforeSend: function( xhr ) {
          $('#main').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
      },

      success: function( data ) {

        $('#p-grid').fadeOut('fast', function() {
            $('#p-grid').html(data);
            $('#main').unblock();
            $('#p-grid').fadeIn('fast', function() {
               initGrid(pageNum, pageSize);
               initModal();
            });
        });
      } 
    });
}

function initGrid(pageNum, pageSize) {

    if(returnItem == null) {
        window.pageNum = pageNum;
    }

    window.pageSize = pageSize;

    if(returnItem && $("#"+returnItem).length != 0) {
        var target_offset = $("#"+returnItem).offset();
        var target_top = target_offset.top - 65;
        $('html, body').animate({scrollTop:target_top}, 500);
    }

    // Bind recommended option to quiz
    if($('.recommended-option').length > 0) {
        $('.recommended-option').unbind('click');
        $('.recommended-option').attr({'href': urls['quizUrl'], 'quiz-location': 'Quiz-Sort'});
    }

    initList();
    initPagination('enable');

    $(".trigger").tooltip({
        position: "bottom right",
        relative: true
    });
}

function initModal() {
    $('.modal').each(function() {
        var a = $(this);
        a.fancybox({
            'padding'		: 0,
            'autoScale'		: true,
            'autoScale'     : false,
            'overlayOpacity' : 0.8,
            'overlayColor' : '#000000',
            'type'			: 'ajax',
            'onStart'		: function() {
                $.blockUI({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#666', opacity: 0.3 }});
            },
            'onComplete'    : function() {
                $.unblockUI();
            }
        });
    });

    $('.inline-modal').each(function() {
        var a = $(this);
        a.fancybox({
            'padding'		: 0,
            'autoScale'		: true,
            'autoScale'     : false,
            'overlayOpacity' : 0.8,
            'overlayColor' : '#000000',
            'onStart'		: function() {
                $.blockUI({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#666', opacity: 0.3 }});
                $('#fancybox-close').css('background', 'url("'+ baseUrl +'static/images/buttons/modal_close_or.png") repeat scroll 0 0 transparent');
            },
            'onComplete'    : function() {
                $.unblockUI();
            },
            'onClosed'      :  function() {
                $('#fancybox-close').css('background', 'url("'+ baseUrl +'static/images/buttons/modal_close.png") repeat scroll 0 0 transparent');
            }
        });
    });
}

function closeQuizModal() {

    var url = window.location.href;
    url = url.replace('&lq=1', '');
    url = url.replace('?lq=1', '');

    if(window.location.hash != "" && window.location.hash != undefined) {
        url += window.location.hash;
    }

    // force page reload if the url is the same
    if(url == window.location.href+window.location.hash) {
        window.location.reload(true);
    }
    else {
        window.location = url;
    }
}

function validateEmail(elementValue){
   var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
   return emailPattern.test(elementValue);
}

function clearForm(oForm) {

  var elements = oForm.elements;

  oForm.reset();

  for(i=0; i<elements.length; i++) {

	field_type = elements[i].type.toLowerCase();

	switch(field_type) {

		case "text":
		case "password":
        case "file":
		case "textarea":
	        case "hidden":

			elements[i].value = "";
			break;

		case "radio":
		case "checkbox":
  			if (elements[i].checked) {
   				elements[i].checked = false;
			}
			break;

		case "select-one":
		case "select-multi":
            		elements[i].selectedIndex = -1;
			break;

		default:
			break;
	}
    }
}

function trackCustomVariable(slot, cat, value) {
    // alert(slot + ' - ' + cat + ' - ' + value);
    if(analyticsEnabled == true) {
        _gaq.push(['_setCustomVar',
            slot,                   // This custom var is set to slot #1.  Required parameter.
            cat,     // The name acts as a kind of category for the user activity.  Required parameter.
            value               // This value of the custom variable.  Required parameter.
        ]);
    }
}


function trackEvent(cat, action, label, value) {
    // alert(cat + ' - ' + action + ' - ' + label + ' - ' + value);
    if(analyticsEnabled == true) {
        _gaq.push(['_trackEvent', cat, action, label, value]);
    }
}

function initCompatibilityColumns () {
    // init compatibility tab layout
    $('.rating-values-tmp li').each(function(index) {
        if(index < $('.rating-values-tmp li').length) {
            $('#r-cont-l').append($(this));
        } else {
            $('#r-cont-r').append($(this));
        }
    });
    $('.rating-values-tmp').remove();
}

// Custom Jquery extension to serialize to JSON all fields of a form.
// Use as follow: var data = JSON.stringify($('form').serializeObject());
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/*
 * Element carousel
 * Takes a carousel container id. Each carousel page has to have the .carousel-row class and a attribute row-num.
 * Carousel controls have a .carousel-control class and the row-num attribute.
 *
 * @param el          Carousel container element (element id)
 * @param interval    Interval between changes (ms)
 * @param animTime    Animation time (ms)
 * @param cel         Container element for controls (element id, leave empty if none)
 * @param fadeBefore  Fade element out before fading new in (assumed true)
 * @param enableBack  Enable back and next elements (#back-button and #next-button)
 * @param loop        Loop carousel (bool)
 *
 */
function carousel(el, interval, animTime, cel, fadeBefore, enableBack, loop) {

    var counter = 0;
    var totalCount = 0;
    $('#'+el+' .carousel-row').hide();
    $('#'+el+' [row-num=0]').fadeIn(animTime);

    if(loop == undefined) loop = true;

    return function() {

        var cl = $('#'+cel).length;
        var nc = 0;

        function fadeElements(newCounter) {

            if(nc > -1) {

                if(newCounter && newCounter >= 0) {
                    nc = newCounter;
                }
                else {
                    if(totalCount == $('#'+el+' .carousel-row').length - 1 && loop == false) {
                        nc = -1;
                    }
                    else {
                        if(nc++ >= $('#'+el+' .carousel-row').length - 1) { nc = 0; counter = $('#'+el+' .carousel-row').length - 1 }
                    }
                }

                if(nc > -1) {
                    if(cl > 0) {
                        $('#'+cel+' .carousel-control').removeClass('selected');
                        $('#'+cel+' [row-num='+nc+']').addClass('selected');
                    }
                    $('#'+el+' .carousel-row').stop(true, true);

                    if(fadeBefore == false) {
                        $('#'+el+' [row-num='+counter+']').fadeOut(animTime, 'easeInQuart');
                        $('#'+el+' [row-num='+nc+']').fadeIn(animTime, 'easeOutCubic');
                    }
                    else {
                        $('#'+el+' [row-num='+counter+']').fadeOut(animTime, function() {
                            $('#'+el+' [row-num='+nc+']').fadeIn(animTime);
                        });
                    }

                    counter = nc;
                }

            }

            totalCount++;
        }

        var switcher = setInterval(fadeElements, interval);

        // Add control elements
        if(cl > 0) {

            $('#'+cel+' .carousel-control').click(function (ev) {
                ev.preventDefault();
                clearTimeout(switcher);
                fadeElements($(this).attr('row-num'));
            });

        }

        if(enableBack && enableBack == true) {

            $('#back-button').mouseover(function(ev) {
                $(this).addClass('back-on');
            }).mouseout(function(ev) {
                $(this).removeClass('back-on');
            }).click(function(ev) {
                ev.preventDefault();
                var ncl = counter - 1;
                if(ncl < 0) { ncl = $('#'+el+' .carousel-row').length - 1; }
                clearTimeout(switcher);
                fadeElements(ncl);
            });

            $('#next-button').mouseover(function(ev) {
                $(this).addClass('next-on');
            }).mouseout(function(ev) {
                $(this).removeClass('next-on');
            }).click(function(ev) {
                ev.preventDefault();
                var ncl = counter;
                if(ncl++ >= $('#'+el+' .carousel-row').length - 1) { ncl = 0; }
                clearTimeout(switcher);
                fadeElements(ncl);
            });

        }

    }();
}

function positionBackToTop() {
    if($(window).width() < 1280) {
        $('#back-top').addClass('back-top-left');
    }
    else {
        $('#back-top').removeClass('back-top-left');
    }
}