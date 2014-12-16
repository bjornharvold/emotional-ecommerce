$().ready(function($) {

    if($('#partial-quiz-take') && $.cookie('task_partial-quiz-take') != 'hidden') {
        $('#partial-quiz-take').show();
    }

    $('.task-alert .close-alert').click(function() {
        $(this).parents('.task-alert').fadeOut('fast', function() {
            var taskId = $(this).attr('task-id');
            $.cookie('task_'+taskId, 'hidden', { expires: 1, path: '/' });
        });
    });

    // Check background tasks
    $('.task-alert.progress').each(function() {
        var taskId = $(this).attr('task-id');
        // Initial query
        checkTask(taskId);

        // recurring checks
        window.tasks = {};
        window.tasks.taskId = setInterval(function() {
            checkTask(taskId);
        }, 1000);
    });
});


function checkTask(taskId) {
    $.ajax({
        url: baseUrl+'task/'+taskId,
        dataType: 'json',
        contentType: 'application/json',
        success: function( data ) {
            if(data) {

                // Show alert and set cookie
                if($('#'+taskId+'_progress').is(':hidden') && $.cookie('task_'+taskId) != 'hidden') {
                    $('#'+taskId+'_progress').fadeIn('slow');
                    $.cookie('task_'+taskId, 1, { expires: 1, path: '/' });
                }

                // Update step count
                var percent = Math.round(data.cstp / data.stps * 100);
                $('#'+taskId+'_progress .p-bar-fill').css('width', percent+'%');
                $('#'+taskId+'_progress .p-val').html(percent+'% ');
            } else {
                // Show success message and stop interval
                clearInterval(window.tasks.taskId);

                if($('.anim-steps').length > 0) {

                    // Check user motivator data
                    $.ajax({
                        url: '/quiz/profilestatus',
                        contentType: 'application/json',
                        success: function( data ) {
                            // Handle alert in "waiting" view
                            $('#waiting-animation').fadeOut('fast', function() {
                                if(data.profileStatus == 'MISSING') {
                                    $('#profile-incomplete').fadeIn('fast');
                                } else {
                                    $('#profile-complete').fadeIn('fast');
                                }
                            });
                        }
                    });

                } else {
                    // Handle alert in all other pages
                    $('#'+taskId+'_progress').fadeOut('slow', function() {
                        $('#'+taskId+'_done').fadeIn('slow');
                    });
                }
            }
        }
    });
}