var affiliateId = 'babygaga';
var applicationId = 'quiz';
var quizId = 'quiz-1';
var targetSelector = '#lela-quiz';

$().ready(function() {
    var options = {
        affiliateId: affiliateId,
        applicationId: applicationId,
        targetSelector: targetSelector,

        theme: 'theme1',

        user: {
            email: null,
            firstName: null,
            lastName: null
        },

        quizCreated: function(quiz) {
            if (console) console.log('API callback: quizCreated');
        },
        quizCompleted: function(quiz) {
            if (console) console.log('API callback: quizCompleted');
            // $(targetSelector).hide();
            // $(completedSelector).show();
            window.location = '/categories';
        },
        stepStarted: function(data) {
            if (console) console.log('API callback: stepStarted - ' + data.stepId);
        },
        stepCompleted: function(data) {
            if (console) console.log('API callback: stepCompleted - ' + data.stepId);
        },
        registrationCompleted: function(user) {
            if (console) console.log('API callback: registrationCompleted - ' + user.email);
        },
        saveQuizCompleted: function(user) {
            if (console) console.log('API callback: saveQuizCompleted - ' + user.email);
        }
    };

    Lela.createQuiz(options);
});
