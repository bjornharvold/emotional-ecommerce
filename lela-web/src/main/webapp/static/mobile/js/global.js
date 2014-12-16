$().ready(function($) {

    // Menu Toggle
    $('#menu-tog').click(function() {
        if($('#cat-menu').is(':hidden')) {

            $(this).addClass('selected');
            $('#main').fadeTo('fast', 0.2, function() {
                $('#cat-menu').slideDown();
            });
        } else {
            $(this).removeClass('selected');
            $('#cat-menu').slideUp('fast', function() {
                $('#main').fadeTo('fast', 1);
            });
        }
    });

    // Search Toggle
    $('#nav-sc').click(function() {
        $('#nav-form input, #close-sc').unbind();
        $('#nav-sc, #nav-ctrls, #nav-form').toggle();
        $('#nav-form input').focus();

        $('#nav-form input').keydown(function() {
            if($('#nav-form input').val() == 'Search') {
                $('#nav-form input').val('');
            }
        });

        $('#close-sc').click(function() {
            $('#nav-sc, #nav-ctrls, #nav-form').toggle();
            $('#nav-form input').val('Search');
            return false;
        });

        return false;
    });

    // dashboard/logout toggle
    $('#usernav').click(function() {
        $('#usernav-cnt').fadeToggle('fast');
    });


    $('.f-submit').click(function() {
        $(this).parents('form').submit();
        return false;
    });

});

function validateEmail(elementValue){
    var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailPattern.test(elementValue);
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
