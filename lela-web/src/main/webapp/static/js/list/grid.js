$().ready(function($) {
    initGrid();
    initFilters();

    if($('#add-image-remote').length != 0) {
        $('#add-image-remote').trigger('click');
    }
});


function initFilters() {
    $('.fltr-active.active').click(function() {

        var obj = $(this);
        if(!obj.next('ul').is(':visible')) {

            $('.fltr-sec ul').hide();

            obj.next('ul').slideDown('fast', function() {

                // Close dropdown when clicking outside
                if(obj.next('ul').is(":visible")) {

                    $('html').click(function() {
                        $('.fltr-sec ul').hide();
                        $('html').unbind('click');
                    });

                    $('.fltr-sec ul').click(function(event){
                        event.stopPropagation();
                    });
                }

            });
        } else {
            $('.fltr-sec ul').hide();
        }

        return false;
    });

    $('.fltr-sec li a').click(function() {
        $(this).parents('ul').next('input').val($(this).attr('data'));
        $(this).parents('ul').find('.selected').removeClass('selected');
        $(this).addClass('selected');
        $(this).parents('ul').prev('.fltr-active').find('span').html($(this).html());
        $(this).parents('ul').slideUp('fast');
        submitFilters();
        return false;
    });

}

function initGrid() {
    initList();

    // Init tooltips
    $("#list-container .tip").tooltip({
        position: "bottom center",
        relative: true,
        offset: [5, 0]
    });

    $('form.custom').jqTransform();

    // Masonry Setup
    $('#list-container').hide();
    $('#main').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
    $('#list-container').imagesLoaded( function(){
        $('#main').unblock();
        $('#list-container').show();
        $('#list-container').masonry({
            itemSelector: '.card',
            isResizable: true,
            columnWidth: 242,
            gutterWidth: 18
        });

        // Show comments if cookie is set.
        if($.cookie('list_comments') == 'true') {
            $('#comment-toggle').trigger('click');
        }
    });


    // Toggle Comments
    $('#comment-toggle').click(function() {
        if(!$(this).hasClass('selected')) {
            $('.card-comments').show();
            $('#list-container').masonry('reload');
            $(this).addClass('selected');
            $(this).html('Comments on');
            $.cookie('list_comments', true, { expires: 100, path: '/' });
        } else {
            $('.card-comments').fadeOut('fast', function() {
                $('#list-container').masonry('reload');
            });
            $(this).removeClass('selected');
            $(this).html('Comments hidden');
            $.cookie('list_comments', false, { expires: 100, path: '/' });
        }

        return false;
    });

    // Init Jquery UI sortables
    $('#list-container').sortable({
        distance: 5,
        forcePlaceholderSize: true,
        items: '.card',
        placeholder: 'card-sortable-placeholder card',
        tolerance: 'pointer',
        handle: '.drag-ico',

        start:  function(event, ui) {
            ui.item.addClass('dragging').removeClass('card').addClass('card-ghost');
            ui.item.parent().masonry('reload');
        },
        change: function(event, ui) {
            ui.item.parent().masonry('reload');
        },
        stop:   function(event, ui) {
            ui.item.removeClass('dragging').addClass('card').removeClass('card-ghost');
            ui.item.parent().masonry('reload');

            var sortQuery = {};
            sortQuery.urlName = ui.item.attr('rlnm')
            sortQuery.order = ui.item.index()+1;
            var json = JSON.stringify(sortQuery);
            $.ajax({
                url: '/user/list/reorder',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data : json,

                success: function(data) {
                    // TBD
                }
            });
        }
    });

    // Init card mouse over
    $('.lay-card').live('mouseenter', function() {
        $('.card-over').removeClass('card-over');
        $(this).addClass('card-over');
    }).live('mouseleave', function() {
        $(this).removeClass('card-over');
    });
}

function submitFilters() {

    // Setup Filter Query
    filterQuery = {};
    filterQuery.boardCode = $('#boardCode').attr('value');
    if($('#categoryUrlName').attr('value') != "") {
        filterQuery.categoryUrlName = $('#categoryUrlName').attr('value');
        filterQuery.contentType = 'ITEMS';
    }
    if($('#storeUrlName').attr('value') != "") {
        filterQuery.storeUrlName = $('#storeUrlName').attr('value');
        filterQuery.contentType = 'STORE_WITH_ITEMS';
    }
    if($('#ownerUrlName').attr('value') != "") {
        filterQuery.ownerUrlName = $('#ownerUrlName').attr('value');
        filterQuery.contentType = 'OWNER_WITH_ITEMS';
    }
    if($('#sortType').attr('value') != "") {
        filterQuery.sortType = $('#sortType').attr('value');
    }
    var json = JSON.stringify(filterQuery);
    var url = $('#list-filters').attr('action');

    $.ajax({
        cache: false,
        url: url,
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        data : json,
        dataType : 'html',

        beforeSend: function( xhr ) {
            $('#main').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
        },

        success: function(data) {
            $('#list-container').replaceWith(data);
            initGrid();
            initModal();
            $('#main').unblock();
        }
    });
}

function initExternal() {

    initModal();
    $(".custom").jqTransform();

    $('#external-form .submit-alert').click(function() {
        $(this).parent('form').submit();
        return false;
    });


    $('.fetch-img').click(function() {

        query = {};
        query.url = $('#externalUrl').attr('value');
        var json = JSON.stringify(query);
        var url = $(this).attr('href');

        $.ajax({
            url: url,
            type: 'POST',
            contentType: 'application/json',
            data: json,
            beforeSend: function( xhr ) {
                $('.card-modal').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});
            },
            success: function( data ) {
                $('.external-images').html(data).show();
                $('.upload-section').hide();
                $('.card-modal').unblock();

                initExternalCarousel();

                $.fancybox.center();
            }
        });


        return false;
    });

    $('.upload-img').click(function() {
        var obj = $(this);

        if($('#imageTitle').val() == '') {
            // Change to inline alert
            alert('You need to add a title');
        } else if(($('#imageUrl').val() == '' && $('#multipartFile').val() == '')) {
            // Change to inline alert
            alert('You need to select an image');
        } else {
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
        }

        return false;
    });

}

function initExternalCarousel() {

    $('#imageTitle').focus(function() {
        $(this).addClass('focus');
    });

    var img = $('.external-images a.selected');

    if($('#imageTitle').val() == '') {
        if(img.attr('data-alt') != "") {
            $('#imageTitle').val(img.attr('data-alt'));
        }
    }

    $('#imageUrl').val(img.attr('href'));
    $('#imageWidth').val(img.attr('data-width'));

    $('.external-images a.img-ctrls').click(function() {

        $('.external-images a.img-ctrls').css('visibility', 'visible');

        if($(this).attr('href') == '#previous') {
            $('.external-images a.selected').removeClass('selected').addClass('deselected').prev('a.deselected').removeClass('deselected').addClass('selected');
            $('.img-count span').html(Number($('.img-count span').html()) - 1);
            if($('.external-images a.selected').prev('a.deselected').length == 0) {
                $(this).css('visibility', 'hidden');
            }
        }
        if($(this).attr('href') == '#next') {
            $('.external-images a.selected').removeClass('selected').addClass('deselected').next('a.deselected').removeClass('deselected').addClass('selected');
            $('.img-count span').html(Number($('.img-count span').html()) + 1);
            if($('.external-images a.selected').next('a.deselected').length == 0) {
                $(this).css('visibility', 'hidden');
            }
        }

        var img = $('.external-images a.selected');

        if($('#imageTitle').val() == '' || (!$('#imageTitle').hasClass('focus') && $('#imageTitle').val() != '')) {
            if(img.attr('data-alt') != "") {
                $('#imageTitle').val(img.attr('data-alt'));
            } else {
                $('#imageTitle').val(img.attr('data-filename'));
            }
        }

        $('#imageWidth').val(img.attr('data-width'));
        $('#imageUrl').val(img.attr('href'));

        return false;
    });

}