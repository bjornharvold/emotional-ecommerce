/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $(".run-job").click(function(e) {
        var href = $(this).attr("href");
        $("#job-form").attr("action", href);
        $("#job-form").submit();

        e.preventDefault();
        return false;
    });
});