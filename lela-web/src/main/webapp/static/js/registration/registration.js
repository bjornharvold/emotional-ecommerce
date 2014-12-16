/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

function initRegistration(ajax) {

    //$('#menu, #footer').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#0A0A0A', opacity: 1 }});

    $('.custom').jqTransform();

    $('.fb-submit').click(function(e) {

       e.preventDefault();

       //$('#registration-form').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.7 }});

       if(formType == 'registration'){
           trackEvent('user', 'registration', 'FB success', 1);
           trackCustomVariable(2, 'registered', 'facebook');
       }

       $('#facebook-reg').submit();
       return false;

    });

    $('.popupLogo').click(function () {
        window.location = '/';
    });

    $('#show-login').click(function() {

        $('#show-login-container').fadeOut(100, function() {
            $('#hidden-login').fadeIn('fast');
        });

        $('.ssl-info').hide();

    });

    // make enter submit the form
    $(document).keyup(function(event) {
        if (event.keyCode == 13 && $("*:focus").attr("id") == 'password' && $("*:focus").val() != "") {
            $("#reg-form").submit();
            event.preventDefault();
            return false;
        }
    })

    if(formType == 'registration'){
        trackEvent('user', 'registration', 'access', 1);
    }

    $("form.custom").jqTransform();

    $('.r-submit').click(function() {

       // Client-side Validation
       var errors = new Array();
       var validated = true;

       if(validateEmail($('.email-type').val()) == false){
            errors[$('.email-type').attr('id')] = messages['user.form.error.email'];
       }

       $('.required').each(function() {
            var classType = $(this).attr('class').split(' ');
            if($(this).val() == '' || $(this).val() == types[classType[0]]) {
                var key = String($(this).attr('id'));
                errors[$(this).attr('id')] = messages['user.form.error.empty'];
            }
       });

        $('.error').html('');
        for (var id in errors) {
            var validated = false;
            $('#'+id).parents('.f-row').children('.error').html(errors[id]);
        }

       if(validated) {

           // remove default text if not filled in.
           $('#reg-form input').each(function() {
              if($(this).attr('type') != 'hidden') {
                  var classType = $(this).attr('class').split(' ');
                  if($(this).val() == types[classType[0]]) {
                      $(this).val('');
                  }
              }
           });

           if(formType == 'registration'){
               trackEvent('user', 'registration', 'REG success', 1);
               trackCustomVariable(2, 'registered', 'regular');
           }

           $(this).parents('form').submit();
           $('.error').html('');
       }
       return false;
    });

}

