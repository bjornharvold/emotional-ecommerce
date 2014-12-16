/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

var affiliateId = 'lela';
var applicationId = 'lela-quiz-noreg';
var quizId = 'quiz-noreg';
var targetSelector = '#lela-quiz';


$().ready(function($) {

    var options = {
        affiliateId: affiliateId,
        applicationId: applicationId,
        targetSelector: targetSelector,

        theme: 'theme2',

        user: {
            email: null,
            firstName: null,
            lastName: null
        },

        quizCompleted: function(quiz) {
            if(loggedIn == 'false') {
                window.open('/register?type=quiz&rt=/categories');
            } else {
                window.open('/categories');
            }
        }
    };

    Lela.createQuiz(options);

    $('#cat-l a').click(function() {

       $('#cat-l a').removeClass('selected');
       $(this).addClass('selected');
       $('.cat-c').hide();
       $('#'+$(this).attr('key')).show();

       return false;
    });

    $('#cat-l a.empty-a').unbind('click');
    var categoryCarousel = carousel('category-carousel', 6000, 400, 'cat-l');

});

$().bind('beforeunload', function(){
    trackUnloadTime('vimeo-player');
});