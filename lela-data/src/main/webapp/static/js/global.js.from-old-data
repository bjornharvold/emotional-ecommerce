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

    $.ajaxSetup({
      cache: false
    });

    $('.f-submit').click(function() {
       $(this).parents('form').submit(); 
       return false;
    });

    // Setup Main Search Form
    $('#search-form a').click(function() {
       $('#search-form').submit(); 
    });

    $('#search-form input').focus(function() {

       if($(this).val() == messages['search.input']) {
        $(this).val('');
       } else {
        $(this).select();           
       }
        
    }).blur(function() {
        if($(this).val() == '') {
            $(this).val(messages['search.input']);
        }
    });

    // Setup user Tabs
    $('#user-tab').mouseenter(function() {
        $(this).next().toggle();
        $(this).addClass('ad-tab');
    }).mouseleave(function() {
        $(this).next().toggle();
        $(this).removeClass('ad-tab');
    });

    $('#user-nav').mouseleave(function() {
        $(this).toggle();
        $(this).prev().removeClass('ad-tab');
    }).mouseenter(function() {
        $(this).toggle();
        $(this).prev().addClass('ad-tab');
    });

    $('#lela-tab').mouseenter(function() {
        $(this).next().toggle();
        $(this).addClass('ad-or-tab');
    }).mouseleave(function() {
        $(this).next().toggle();
        $(this).removeClass('ad-or-tab');
    });

    $('#lela-nav').mouseleave(function() {
        $(this).toggle();
        $(this).prev().removeClass('ad-or-tab');
    }).mouseenter(function() {
        $(this).toggle();
        $(this).prev().addClass('ad-or-tab');
    });

    initModal();
    
    // Launch quiz when needed on load
    if(lq != undefined) {
        var t = setTimeout("$(lq).trigger('click')", 500);
    }

});

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

    if(window.itemNum) {
        var count = $('#p-grid').find('.item').length;

        $('#num-total').html(window.itemNum['total']+' ');
        $('#num-count').html(count+' ');
        $('#filter-count').html(count+' ');
    }
    
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

    $('.l-list').click(function(e) {

        if(loggedIn == 'false') {

            $(this).parent().find('.l-alert').show();
            $('.l-alert').mouseleave(function() {
                $(this).fadeOut('fast');
            });

            $('.l-alert a').click(function() {
                var redirect = $(this).attr('href')+'?referrer='+window.location;
                window.location = redirect;
                return false;
            });

        } else {

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
              beforeSend: function( xhr ) {
                if(method == 'PUT') {
                    // TBD
                } else if (method == 'POST') {
                  $('#'+id).block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
                }
              },
              success: function( data ) {

                if(method == 'PUT') {
                    // Added to list
                    if(data.message == 1) {
                        link.prev().removeClass('hidden');
                        link.addClass('hidden');
                        var num = Number($('#lela-nav2 b').html()) + 1;
                        $('#lela-nav2 b').html(num+' ');

                    } else if (data.message == 0) {
                        // alert('ERROR: This item already belongs to your list');
                    }
                } else if (method == 'POST') {
                    // Removed from list
                    $('#count-all').html(($('#count-all').html()) - 1);
                    $('#count-'+category).html(($('#count-'+category).html()) - 1);
                    var num = Number($('#lela-nav2 b').html()) - 1;
                    $('#lela-nav2 b').html(num+' ');
                    $('#'+id).fadeOut(function() {
                        $('#'+id).unblock;
                    });
                }
              }
            });

        }

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

        if(returnItem == null) {
            window.pageNum = pageNum;
        }
          
        window.pageSize = pageSize;

        $('#p-grid').fadeOut('fast', function() {
            $('#p-grid').html(data);
            $('#main').unblock();
            $('#p-grid').fadeIn('fast', function() {
                if(returnItem && $("#"+returnItem).length != 0) {
                    var target_offset = $("#"+returnItem).offset();
                    var target_top = target_offset.top;
                    $('html, body').animate({scrollTop:target_top}, 500);
                }

                // Bind recommended option to quiz
                if($('.recommended-option').length > 0) {
                    $('.recommended-option').unbind('click');
                    $('.recommended-option').addClass('modal quiz-modal').attr('href', urls['quizUrl'] + '?src=grid&amp;vid=true');
                }
                
                initList();
                initPagination('enable');
                initModal();

                $(".trigger").tooltip({
                    position: "bottom right",
                    relative: true
                });

            });
        });
      } 
    });
}

function initModal() {
    $('.modal').each(function() {
        var a = $(this);
        a.fancybox({
            'padding'		: 0,
            'autoScale'		: true,
            'autoScale'     : false,
            'type'			: 'ajax',
            'onStart'		: function() {
                $.blockUI({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#666', opacity: 0.3 }});
            },
            'onComplete'    : function() {
                $.unblockUI();
                if(a.hasClass('quiz-modal')) {
                    $('#fancybox-close').unbind('click').click(function() {
                        if(stepName != undefined) {
                            trackEvent('quiz', 'close', stepName, 1);
                        }
                        closeQuizModal();    
                    });
                }
            }
        });
    });
}

function closeQuizModal() {
    location.reload(true);
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

function trackEvent(cat, action, label, value) {
    if(analyticsEnabled == true) {
        pageTracker._trackEvent(cat, action, label, value);
    }
}

