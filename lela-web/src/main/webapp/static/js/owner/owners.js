/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $('#excelFile').blur(function() {
        var excelFilePath = $(this).val();
        if (excelFilePath == '') {
            $('#step3').show();
        } else {
            $('#step3').hide();
        }
    });

    $('#excelFile').change(function() {
        var excelFilePath = $(this).val();
        if (excelFilePath == '') {
            $('#step3').show();
        } else {
            $('#step3').hide();
        }
    });

    $('#clear').click(function() {
        $('#step3').show();
    });

    $('#itemWithOutStoreSearch').click(function() {
        $('#itemsWithOutStoresList').html("");
        document.body.style.cursor = 'wait';
        $('html').css('cursor', 'wait');
        $('#itemWithOutStoreSearch').css('cursor', 'wait');
    });
});
