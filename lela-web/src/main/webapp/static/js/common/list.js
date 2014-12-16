/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {
    initListActions();
});

/*
function showRatingIfFbFriends(FRIEND_ID) {

    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            var uid = response.authResponse.userID;

            FB.api('/me/friends/'+FRIEND_ID, function(response) {
                // If user if friend or myself
                if(response.data.length > 0 || FRIEND_ID == uid) {
                    $('.item-rating').show();
                } else {
                    $('.item-rating').remove();
                }
            });
        }
    });

    // Check if logged in user and lela list user are friends on FB.
    // Then show ratings if yes.
    FB.getLoginStatus(function(response) {
        if (response.status === 'connected') {
            var fbid = response.id;


        }
    });
}
*/

function initListActions() {

    // Bind Carousel
    // Card pictures enlarge
    $('.addl-img a').live('click', function() {
        $(this).parents('.addl-img').find('.selected').removeClass('selected');
        $(this).addClass('selected');

        if($(this).attr('data-location') == 'largeCarousel') {
            $(this).parents('.card-modal').find('.p-img').attr('src', $(this).attr('href'));
        } else if($(this).attr('data-location') == 'gridCarousel') {
            $(this).parents('.card').find('.p-img').attr('src', $(this).attr('href'));
            $('.p-img').imagesLoaded( function(){
                $('#list-container').masonry('reload');
            });
        }

        return false;
    });

    // Toggle extra picture view
    $('.card-modal .show-imgs').live('mouseenter', function() {
        $(this).next('.list-carousel-cont').show().next('.show-pictures').hide();
    }).live('mouseleave', function(){
        $(this).next('.list-carousel-cont').hide().next('.show-pictures').show();
    });
    // Toggle extra picture view
    $('.card-modal .list-carousel-cont').live('mouseover', function() {
        $(this).show().next('.show-pictures').hide();
    }).live('mouseleave', function(){
        $(this).hide().next('.show-pictures').show();
    });

    $('.card-modal .show-pictures').live('click', function() {
        $(this).toggle().prev('.list-carousel-cont').fadeIn('slow');
        return false;
    });

    $('.list-c-prev').live('click', function(e) {

        var left = '+=194px';
        if($(this).attr('data-location') == 'largeCarousel') {
            left = '+=438px';
        }

        if($(this).next().children('.addl-img').css('left') != '0px') {
            $(this).next().next('.list-c-next').attr('data-index', Number($(this).next().next('.list-c-next').attr('data-index'))-1);
            $(this).next().children('.addl-img').animate({
                left: left
            }, 500);
        }

        return false;
    });

    $('.list-c-next').live('click', function(e) {
        var left = '-=194px';
        if($(this).attr('data-location') == 'largeCarousel') {
            left = '-=438px';
        }

        if($(this).attr('data-index') < ($(this).prev('.list-carousel').find('a').length) / 4) {
            $(this).attr('data-index', Number($(this).attr('data-index'))+1);
            $(this).prev().children('.addl-img').animate({
                left: left
            }, 500);
        }

        return false;
    });



    // Comment form actions
    $('.txt-comment-small').live('focus', function() {
        // Redirect to login page if user not logged page.
        // User ?rt param to go back to list or card view.
        if(loggedIn == 'false') {
            window.location = '/login?rt='+$(this).attr('rt');
        } else {
            $(this).parents('form').find('.addl-comment').show();
            $('#list-container').masonry('reload');
        }
    });


    // Remove card from lela list
    $('.btn-remove').live('click', function() {

        var r = confirm("Are you sure you want to delete this item?");
        if (r == true) {

            data = {};
            data.rlnm = $(this).attr('rlnm');
            data.boardCode = $(this).attr('board-code');
            var json = JSON.stringify(data);
            var url = $(this).attr('href');
            var card = $('#card-'+$(this).attr('rlnm'));
            var board = $('#board-'+$(this).attr('board-code'));

            $.ajax({
                url: url,
                type: "POST",
                contentType: 'application/json',
                data : json,
                dataType : 'json',
                success: function( data ) {

                    if($('#list-container').length > 0) {
                        card.fadeOut('fast', function() {
                            card.remove();
                            $('#list-container').masonry( 'reload' );
                        });
                    }

                    if($('#board-container').length > 0) {
                        board.find(card).remove();
                        board.find('.card-count').html(Number(board.find('.card-count').html()) - 1);
                    }

                    if($('.card-modal').length > 0) {
                        $.fancybox.close();
                    }

                    var num = Number($('#myFavesCount').html()) - 1;
                    $('#myFavesCount').html(num);
                }
            });
        }

        return false;
    });

    // Submit note or review on lela list card
    $('.submit-note').live('click', function() {

        data = {};
        data.txt = $('#txt').val();

        if($('#ttl').val() != '') {
            // for reviews only
            data.ttl = $('#ttl').val();
        }

        if($('#rtng').val() != '') {
            // for reviews only
            data.rtng = $('#rtng').val();
        }

        var json = JSON.stringify(data);
        var url = $(this).attr('href');
        var type = $(this).attr('type');

        if($('#txt').val() == '' || !$('#txt').hasClass('focus')) {
            // TBD error message
        } else {

            $.ajax({
                url: url,
                type: "POST",
                contentType: 'application/json',
                data : json,
                dataType : 'json',
                beforeSubmit: function() {
                    if($('#txt').val() == '' || !$('#txt').hasClass('focus')) {
                        return false;
                    }
                },
                success: function( data ) {

                    // Reload the card with new note
                    $.ajax({
                        url: url.replace('/note', '').replace('/review', '').replace('/user/list', '/list/card')+'&view='+type,
                        success: function( data ) {
                            $.fancybox({
                                'content' : data ,
                                'overlayOpacity' : 0.8,
                                'overlayColor' : '#000000'
                            });
                        }
                    });
                }
            });
        }

        return false;
    });

    // Toggle views on lela list card (comments, reviews, notes, alerts)
    $('.card-modal .card-data-l').live('click', function() {
        $('.card-modal .card-data-l').removeClass('selected');
        $(this).addClass('selected');
        var id = $(this).attr('href');
        $('.card-data').hide();
        $(id).fadeIn('fast', function() {
                $.fancybox.center();
        });

        return false;
    });

    // Share on Facebook button
    $('.btn-fb').live('click', function() {

        // Hide Card Modal when FB modal shows
        $('#fancybox-wrap').css('opacity', '0');

        var obj = {
            method: 'feed',
            link: $(this).attr('href'),
            picture: $(this).attr('fb-img'),
            caption: $(this).attr('fb-caption'),
            description: $(this).attr('fb-description'),
            name: $(this).attr('fb-name')
        };

        FB.ui(obj, function(response) {
            // Show card modal after FB share is completed
            $('#fancybox-wrap').css('opacity', '1');
            $.fancybox.center();
        });

        return false;
    });

    // Delete Note, Review, Alert, Picture
    $('.delete-item, .delete-txt').live('click', function() {

        var obj = $(this);
        var url = $(this).attr('href');
        var type = $(this).attr('data-type');

        var r = confirm("Are you sure you want to remove this "+ type +"?");
        if (r == true) {
            $.ajax({
                url: url,
                type: "DELETE",
                dataType : 'json',
                success: function() {
                    if(type == 'picture') {
                        obj.parent('li').fadeOut('fast');
                    } else if(type == 'list') {
                        obj.parents('.board').fadeOut('fast');
                    } else {
                        obj.parents('.data-row').fadeOut('fast');
                        if(type != 'alert') {
                            $('#'+type+'-cnt').html(Number($('#'+type+'-cnt').html()) - 1);
                        }
                    }
                }
            });
        }

        return false;
    });

    // Share on twitter via the Bit.ly URL shortener API
    $('.btn-tw').live('click', function() {
        var link = $(this);
        var url = link.attr('href');
        var text = link.attr('text');
        /*
        BitlyClient.shorten(url, function(data) {
            console.log('bit.ly response: ');
            console.log(data);
            var bitly_link = url;
            for (var r in data.results) {
                url = data.results[r]['shortUrl'];
                break;
            }


        });
        */
        window.open("https://twitter.com/share?url="+encodeURIComponent(url)+'&text='+text, '', 'width=550,height=450');

        return false;
    });

    // Upload card images
    $('.picture-card').live('change', function() {
        $(this).parents('.card-modal').find('.submit-picture').fadeIn('fast').next('.done-btn').hide();
    });

    $('.submit-picture').live('click', function() {
        var obj = $(this);
        obj.parent('form').ajaxForm({
                beforeSubmit: function(arr, $form, options) {
                    $('.card-modal').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
                },
                success: function(responseText, statusText, xhr) {


                    $(obj.parents('.card-modal')).replaceWith($(responseText));
                    $.fancybox.center();
                    $('.card-modal').unblock();

                }
            }).submit();
        return false;
    });

    // Check FB publish_stream permission
    $('.addl-comment label').live('click', function() {
            var permsNeeded = ['publish_stream'];
            checkPermissions(permsNeeded);
    });

    // Post card comment
    $('.card-comment a, .card-comment-small a').live('click', function() {

        var obj = $(this);
        var dataArray = [];

        obj.parents('form').ajaxForm({
            resetForm: true,
            beforeSubmit: function(arr, $form, options) {

                for (var i = 0; i < arr.length; i++) {
                    dataArray[arr[i].name] = arr[i].value;
                }

                if(dataArray['txt'] == "") {
                    return false;
                }
            },
            success: function(responseText, statusText, xhr) {

                // Post to Facebook Stream
                if(dataArray['fb'] == 'true') {
                    postToFeed(dataArray['txt'], obj.attr('post-name'), '', obj.attr('post-url'), obj.attr('post-picture'), '');
                }

                if(obj.parents('form').hasClass('card-comment-small')) {

                    var comments = obj.parents('.card-comments');
                    $(responseText).appendTo(comments.find('.comments-list'));
                    if(comments.find('.show-all').length > 0) {
                        comments.find('.show-all a strong').html(Number(comments.find('.show-all a strong').html()) + 1);
                    }
                    comments.find('.addl-comment').hide();
                    $('#list-container').masonry('reload');
                } else {
                    $(responseText).hide().appendTo(obj.parents('.card-data').find('.card-comments')).fadeIn('fast');
                    $('#comment-cnt').html(Number($('#comment-cnt').html()) + 1);
                    $.fancybox.center();
                }
            }
        }).submit();

        return false;
    });

}

function initTutorial(force) {

    if($.cookie('list_tutorial') == null || force == true) {

        $.fancybox({
            'content' : $('#tut').html(),
            'overlayOpacity' : 0.75,
            'overlayColor' : '#000000'
        });

        // $('#tut').remove();

        $('#fancybox-content #tut-cont .step-l').click(function() {
            $('#fancybox-content #tut-cont .step-l').removeClass('over');
            $(this).addClass('over');

            var stepId = '#fancybox-content #'+$(this).attr('id')+'-img';
            $('#fancybox-content .step-img').hide();
            $(stepId).fadeIn('fast');
            $.fancybox.center();

            return false;
        });

        $('#fancybox-content #tut-cont .next-slide').click(function() {
            var id =$(this).attr('href');
            $('#fancybox-content '+id).trigger('click');
            return false;
        });

        $.cookie("list_tutorial", "1", { path: '/' });
    }
}
