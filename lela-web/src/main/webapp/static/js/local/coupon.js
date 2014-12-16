// Jquery Mobile Default settings
$(document).bind("mobileinit", function(){
  $.extend(  $.mobile , {
    ajaxEnabled: false
  });
});


// Common JS tasks
$().ready(function($) {
    if(device == undefined || device != 'mobile') {
        $('form.custom').jqTransform();
    }
});

function initCreate() {
    
    $('form#create-coupon input').keypress(function(e){
        if(e.which == 13){
            $('#create-coupon a').trigger('click');
            return false;
        }
    });

    $('a#create-cpn').click(function() {

        $('.error').hide();
        var valid = true;
        
        if($('#fnm').val() == '') {
            $('.fnm-error').show();
            valid = false;
        }

        if($('#lnm').val() == '') {
            $('.lnm-error').show();
            valid = false;
        }

        if(valid == true) {
            $(this).parents('form').submit();
        }

        return false;
    });
}

function initRedeem() {

    $('#brcd').focus(function() {
        $(this).val('');
    });

    $('form#redeem-coupon input').keypress(function(e){
        if(e.which == 13){
            $('#redeem-coupon a').trigger('click');
            return false;
        }

    });

    $('a#redeem-cpn').click(function() {
        
        $('.error').hide();
        var valid = true;

        if($('#brcd').val() == codeTip || $('#brcd').val() == '') {
            $('.brcd-error').show();
            valid = false;    
        }

        if(valid == true) {
            $(this).parents('form').submit();
        }

        return false;
    });
}