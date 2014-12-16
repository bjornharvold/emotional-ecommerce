$().ready(function() {

    var affiliateId = 'lela';
    var targetSelector = '#lela-quiz';

    // Set quiz type based on user motivator data.
    // var quizType is set in quiz.jspx
    if(quizType == 'full') {

        var applicationId = 'lela-quiz-noreg';
        var quizId = 'quiz-noreg';

    } else if(quizType == 'partial') {

        var applicationId = 'lela-quiz-partial';
        var quizId = 'quiz-partial';

    }

    $('#lela-quiz').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '<img src="'+ loaderUrl +'" alt="Loading" />', overlayCSS:  { backgroundColor: '#ffffff', opacity: 0.6 }});

    if(device == 'normal') {
        initQuizAlert();
        var theme = '';
    } else if(device == 'mobile') {
        var theme = 'mobile1';
    }

    var options = {
        affiliateId: affiliateId,
        applicationId: applicationId,
        targetSelector: targetSelector,

        theme: theme,

        user: {
            email: null,
            firstName: null,
            lastName: null
        },

        quizCreated: function(quiz) {
            $('#lela-quiz').unblock();
        },

        quizCompleted: function(quiz) {
            if(loggedIn == 'false') {
                window.location = '/register?type=quiz&rt='+rt;
            } else {
                window.location = rt;
            }
        }
    };

    Lela.createQuiz(options);
});

function initQuizAlert() {

    $('#quiz-exit').insertAfter('#lela-quiz');

    $('#quiz-close').click(function() {
        $('#lela-quiz').css('opacity', '0.3');
        $('#quiz-exit').fadeIn('fast', function() {

            $('#ex-qz').click(function() {
                window.location = '/categories';
                return false;
            });

            $('#cmp-qz').click(function() {
                $('#quiz-exit').fadeOut();
                $('#lela-quiz').css('opacity', '1');
                return false;
            });
        });

        return false;
    });

}