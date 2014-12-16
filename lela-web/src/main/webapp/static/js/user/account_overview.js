/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

$().ready(function($) {

    $("form.custom").jqTransform();
    $("#lela-network .select-status").jqTransSelect();
    $('#lela-network .select-connection').jqTransCheckBox();
    //$("#lela-network table input").jqTransInputText();
    //$("#lela-network table select").jqTransSelect();
    $("#email-invitation table tr.invitee input").jqTransInputText();
    $("#email-invitation table tr.invitee select").jqTransSelect();
    //$('#facebook-invitation .select-profile').jqTransCheckBox();

    $('#change-password').click(function() {
        // Handle browser injecting password if user's password saved in browser
        $('.password-type').val('');
        $('.confirm-password-type').val('');
        $('#lela-account-password').css('visibility','visible');
        return false;
    });

    $('.email-type').bind('click change blur select focus', function() {
        // Handle browser injecting password if user's password saved in browser
        var psswrd = $('.password-type').val();
        var cnfrmpsswrd = $('.confirm-password-type').val();
        var changePasswordSection = $('#lela-account-password').css('visibility');
        if (changePasswordSection != 'visible') {
            //window.setTimeout(function() {
                $('.password-type').val('');
                $('.confirm-password-type').val('');
            //}, 100);
        } else if (cnfrmpsswrd == '') {
            //window.setTimeout(function() {
                $('.password-type').val('');
            //}, 100);
        }
    });

    $('.o-submit').click(function() {
        // Client-side Validation
        var validated = true;
        var errors = new Array();

        // Validate Password
        if ($('.confirm-password-type').length > 0) {
            // Handle browser injecting password if user's password saved in browser
            var lelaAccountPassword = $('#lela-account-password').css('visibility');
            if (lelaAccountPassword != 'visible') {
                $('.password-type').val('');
                $('.confirm-password-type').val('');
            }

            var psswrd = $('.password-type').val();
            var cnfrmpsswrd = $('.confirm-password-type').val();
            if (psswrd != cnfrmpsswrd) {
                errors[$('.confirm-password-type').attr('id')] = messages['error.password.mismatch'];
            }
        }

        // Validate First Name
        if ($('.first-name-type').length > 0) {
            var fnm = $('.first-name-type').val();
            if (fnm == '') {
                errors[$('.first-name-type').attr('id')] = messages['error.firstname.empty'];
            }
        }

        // Validate Last Name
        if ($('.last-name-type').length > 0) {
            var lnm = $('.last-name-type').val();
            if (lnm == '') {
                errors[$('.last-name-type').attr('id')] = messages['error.lastname.empty'];
            }
        }

        // Validate Email
        if ($('.email-type').length > 0) {
            var ml = $('.email-type').val();
            if (ml == '') {
                errors[$('.email-type').attr('id')] = messages['error.email.empty'];
            }

            if(validateEmail($('.email-type').val()) == false){
                errors[$('.email-type').attr('id')] = messages['error.email.invalid'];
            }
        }

        $('.error').html('');
        for (var id in errors) {
            //alert(id + "=" + errors[id]);
            validated = false;
            $('#'+id).parents('.f-row,.h-row').children('.error').html(errors[id]);
        }

        if (validated == true) {
            $(this).parents('form').submit();
        }

        return false;
    });

    $('#new-product-email-subscribe').click(function() {
        if ($(this).text() == messages['user.account.notifications.UNSUBSCRIBE']) {
            $(this).text(messages['user.account.notifications.SUBSCRIBE']);
            $('#email-subscribe-status').val(messages['user.account.notifications.SUBSCRIBE.value']);
        } else {
            $(this).text(messages['user.account.notifications.UNSUBSCRIBE']);
            $('#email-subscribe-status').val(messages['user.account.notifications.UNSUBSCRIBE.value']);
        }
        return false;
    });

    $('.owned-item-subscribe-status').click(function() {
        if ($(this).text() == messages['user.account.notifications.UNSUBSCRIBE']) {
            $(this).text(messages['user.account.notifications.SUBSCRIBE']);
            $(this).next().val(messages['user.account.notifications.SUBSCRIBE.value']);
        } else {
            $(this).text(messages['user.account.notifications.UNSUBSCRIBE']);
            $(this).next().val(messages['user.account.notifications.UNSUBSCRIBE.value']);
        }
        return false;
    });

    $('#lela-network-facebook-invitation #toggle-checkboxes #select-all').click(function() {
        $('#facebook-invitation .jqTransformCheckboxWrapper a.jqTransformCheckbox').each(function() {
            $(this).addClass('jqTransformChecked');
        });
        return false;
    });
    $('#lela-network-facebook-invitation #toggle-checkboxes #unselect-all').click(function() {
        $('#facebook-invitation .jqTransformCheckboxWrapper a.jqTransformCheckbox').each(function() {
            $(this).removeClass('jqTransformChecked');
        });
        return false;
    });

    $('#lela-network table input.email-input, #lela-network-invitation table input.email-input').live('click', function() {
        if ($(this).val() == messages['user.account.network.email']) {
            $(this).val('');
        }
    });

    $('#lela-network table input.email-input, #lela-network-invitation table input.email-input').live('blur', function() {
        if ($(this).val() == "") {
            $(this).val(messages['user.account.network.email']);
        }
    });

    $('#lela-network table input.first-name-input, #lela-network-invitation table input.first-name-input').live('click', function() {
        if ($(this).val() == messages['user.account.network.first.name']) {
            $(this).val('');
        }
    });

    $('#lela-network table input.first-name-input, #lela-network-invitation table input.first-name-input').live('blur', function() {
        if ($(this).val() == "") {
            $(this).val(messages['user.account.network.first.name']);
        }
    });

    $('#lela-network table input.last-name-input, #lela-network-invitation table input.last-name-input').live('click', function() {
        if ($(this).val() == messages['user.account.network.last.name']) {
            $(this).val('');
        }
    });

    $('#lela-network table input.last-name-input, #lela-network-invitation table input.last-name-input').live('blur', function() {
        if ($(this).val() == "") {
            $(this).val(messages['user.account.network.last.name']);
        }
    });

    $('#add-more-invitee a').click(function() {
        for (var i = 0; i < 10; i++) {
            var cloneableInvitee = $('#email-invitation .cloneable-invitee').clone();
            cloneableInvitee.addClass('invitee');
            cloneableInvitee.removeClass('cloneable-invitee');
            $('#email-invitation .cloneable-invitee').before(cloneableInvitee);
            $('#email-invitation .cloneable-invitee').prev().find('input').jqTransInputText();
            $('#email-invitation .cloneable-invitee').prev().find('select').jqTransSelect();
        }

        var count = $('.jqTransformSelectWrapper').length + 10;
        $('.jqTransformSelectWrapper').each(function() {
            $(this).css('z-index', count);
            count--;
        });
    });

    $('.net-submit').click(function() {
        var url = $(this).attr('href');

        var socialReplies = {};
        socialReplies.sclrpls = [];
        var i = 0;
        $('#lela-network .network-connection').each(function() {
            if ((($(this).find('.jqTransformSelectWrapper span') != null) &&
                    (($(this).find('.jqTransformSelectWrapper span').text() == messages["user.account.network.accept"]) ||
                    ($(this).find('.jqTransformSelectWrapper span').text() == messages["user.account.network.ignore"]))) ||
                    ($(this).find('.jqTransformCheckbox').hasClass('jqTransformChecked'))) {

                var userId = $(this).find('.relation-user-id').val();
                var email = $(this).find('.relation-email').val();
                var status = $(this).find('.status').val();
                var deleteConnection = $(this).find('.jqTransformCheckbox').hasClass('jqTransformChecked');
                var socialReply = {};
                socialReply.uid = userId;
                socialReply.ml = email;
                socialReply.stts = status;
                socialReply.dlt = deleteConnection;
                socialReplies.sclrpls[i] = socialReply;
                i = i + 1;
            }
        });

        var json = JSON.stringify(socialReplies);

        // Make the call
        $.ajax({
          url: url,
          type: 'POST',
          contentType: 'application/json',
          data : json,
          dataType : 'json',
          success: function( data ) {
              window.location = networkUrl;
          }
        });

        return false;
    });

    $('.i-submit.submit-email-invitation').click(function() {
        // Client-side Validation
        var validated = true;
        var errors = new Array();

        var url = $(this).attr('href');

        var socialRequests = {};
        socialRequests.sclrqests = [];
        var i = 0;
        $('#email-invitation .invitee').each(function() {
            var email = $(this).find('.email-input').val();
            var firstName = $(this).find('.first-name-input').val();
            var lastName = $(this).find('.last-name-input').val();
            var relation = $(this).find('.relation').val();

            // Validate input data
            if ((email != "") && (email != messages['user.account.network.email'])) {

                // Validate First Name
                if ((firstName == '') || (firstName == messages['user.account.network.first.name'])) {
                    errors['firstNameEmpty'] = messages['error.firstname.empty'];
                }

                // Validate Last Name
                if ((lastName == '') || (lastName == messages['user.account.network.last.name'])) {
                    errors['lastNameEmpty'] = messages['error.lastname.empty'];
                }

                // Validate Relation
                if (relation == '') {
                    errors['relationEmpty'] = messages['error.relation.empty'];
                }

                // Validate Email
                if ((email == "") || (email == messages['user.account.network.email'])) {
                    errors['emailEmpty'] = messages['error.email.empty'];
                }

                if(validateEmail(email) == false){
                    errors['emailInvalid'] = messages['error.email.invalid'];
                }

                $('.error').html('');
                for (var id in errors) {
                    //alert(id + "=" + errors[id]);
                    validated = false;
                    var htmlText = $('#email-invitation .error').html();
                    $('#email-invitation .error').html(htmlText + errors[id] + "<br/>");
                }


                //alert(email + " , " + firstName + " , " + lastName + " , " + relation);
                var socialRequest = {};
                socialRequest.ml = email;
                socialRequest.fnm = firstName;
                socialRequest.lnm = lastName;
                socialRequest.rltn = relation;
                socialRequests.sclrqests[i] = socialRequest;
                i = i + 1;
            }
        });

        if (validated == true) {
            var json = JSON.stringify(socialRequests);

            // Make the call
            $.ajax({
              url: url,
              type: 'POST',
              contentType: 'application/json',
              data : json,
              dataType : 'json',
              success: function( data ) {
                  window.location = networkUrl;
              }
            });
        }

        return false;
    });

    $('.i-submit.submit-facebook-invitation').click(function() {
        var url = $(this).attr('href');
        var socialRequests = {};
        socialRequests.sclrqests = [];
        var i = 0;
        $('#facebook-invitation .facebook-profile').each(function() {
            if ($(this).find('.jqTransformCheckbox').hasClass('jqTransformChecked')) {
                var email = $(this).find('.profile-email').val();
                var firstName = $(this).find('.profile-first-name').val();
                var lastName = $(this).find('.profile-last-name').val();
                var relation = $(this).find('.profile-relation').val();
                //alert(email + " , " + firstName + " , " + lastName + " , " + relation);
                if ((email != "") && (firstName != "") && (lastName != "") && (relation != "")) {
                    var socialRequest = {};
                    socialRequest.ml = email;
                    socialRequest.fnm = firstName;
                    socialRequest.lnm = lastName;
                    socialRequest.rltn = relation;
                    socialRequests.sclrqests[i] = socialRequest;
                    i = i + 1;
                }
            }
        });

        var json = JSON.stringify(socialRequests);

        // Make the call
        $.ajax({
          url: url,
          type: 'POST',
          contentType: 'application/json',
          data : json,
          dataType : 'json',
          success: function( data ) {
              window.location = networkUrl;
          }
        });

        return false;
    });

    $('.n-submit').click(function() {

        var url = $(this).attr('href');

        var notificationsUpdate = {};
        notificationsUpdate.prdctmlfrqncy = $('#email-frequency').val();
        notificationsUpdate.nwprdctmlsbscrbstts = $('#email-subscribe-status').val();
        notificationsUpdate.ntfctnstms = [];
        var i = 0;
        $('.owned-item-subscribe-status').each(function() {
            var notificationsItem = {};
            notificationsItem.rlnm = this.id;
            notificationsItem.prdctsbscrbstts = $(this).next().val();
            notificationsUpdate.ntfctnstms[i] = notificationsItem;
            i = i + 1;
        });

        var json = JSON.stringify(notificationsUpdate);

        // Make the call
        $.ajax({
          url: url,
          type: 'PUT',
          contentType: 'application/json',
          data : json,
          dataType : 'json'
        });

        return false;
    });

    $('#deactivate').fancybox();

    $('.cancel').click(function(ev) {
        ev.preventDefault();
        $.fancybox.close();
    });


    $('.deactivate').click(function(ev) {

        //ev.preventDefault();

        //$('#deactivateform').submit();

        /*
        $.ajax({
            type: "POST",
            url: deactivateUrl,
            data: $('#deactivateform').serialize(),
            success: function(data, textStatus, jqXHR) {
                window.location.replace(logoutUrl);
            },
            error: function(data, textStatus, jqXHR) {
                alert("We're terribly sorry, but an error happened while deactivating your account. Please try again later.");
            }
        });
         */

    });
});