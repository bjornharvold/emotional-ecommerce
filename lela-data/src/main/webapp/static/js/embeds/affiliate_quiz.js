$().ready(function($) {

    if($('#quiz')) {
        loadQuiz(1);
    }

    $('.launch-quiz').click(function(e) {

        $.cookie('checkCookie', null);
        $.cookie( 'checkCookie', '1',  { expires: 7, path: '/' } );
        if( $.cookie('checkCookie') == null ) {
            var formUrl = $("#start-quiz-form").attr('action');
            $("#start-quiz-form").attr('target' , '_blank').attr('action', formUrl+'&blank=true');

        }

        e.stopPropagation();
        e.preventDefault();

        $("#start-quiz-form").submit();

        return false;
    });

});