$().ready(function($) {

    /*
    var target_offset = $('#cat-head').offset();
    var target_top = target_offset.top;
    $('html, body').animate({scrollTop:target_top}, 500);
    */

});

function initGrid(pageSize) {
    window.pageNum = 0;
    window.sortChange = false;
    window.filterChange = false;
    window.pageSize = pageSize;
    initPagination('enable');
    initSort();
}

function initSort() {
    $('#sort-ctrls a').click(function() {
        $('#sort-ctrls .selected').removeClass('selected');
        $(this).addClass('selected');
        window.pageNum = 0;
        window.sortChange = true;
        window.filterChange = true;
        loadMore('replace');
        return false;
    });
}

function initPagination(type) {
    // init inifite Scrolling - TB Completed
    if(type == 'enable') {
        window.loading = false;
        $('#footer').waypoint(function(event, direction) {
            if (window.itemNum['count'] > 0 && window.pageNum >= 0 && !window.loading && direction === 'down') {
                window.pageNum++;

                loadMore('append');

            }
        }, {
            offset: '100%'
        });

    } else if(type == 'disable') {
        $('#footer').waypoint('destroy');
    }
}

function loadMore(type) {
    // AJAX TO GET MORE
    filterQuery = {};
    filterQuery.size = 12;
    filterQuery.page = window.pageNum;
    filterQuery.sort = window.sortChange;
    filterQuery.update = window.filterChange;
    filterQuery.sortBy = $('#sort-ctrls a.selected').attr('sort-type');
    var json = JSON.stringify(filterQuery);

    if(type == 'replace') {
        $('#p-grid').fadeTo('fast', 0.5);
    }

    $.ajax({
        cache: false,
        url: dataUrl,
        type: 'POST',
        contentType: 'application/json; charset=utf-8',
        data : json,
        dataType : 'html',
        success: function( data ) {
            if(type == 'append') {
                $('#p-grid').append(data);
            } else {
                $('#p-grid').html(data);
                $('#p-grid').fadeTo('fast', 1);
            }

            $.waypoints('refresh');

        }
    });
}