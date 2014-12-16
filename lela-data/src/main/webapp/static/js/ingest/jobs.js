/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $(".run-job,.resume-job,.pause-job").click(function(e) {
        var href = $(this).attr("href");
        $("#job-form").attr("action", href);
        $("#job-form").submit();

        e.preventDefault();
        return false;
    });
 
     $(".assign-trigger").click(function(e) {
        var href = $(this).attr("href");
        var selected = $(this).prev().val();
        window.location.href = href+"/"+selected;
        e.preventDefault();
        return false;
    });   
});