$().ready(function() {
    $('#menu, #footer').block({ css: { border: '0px none #aaa', backgroundColor:'transparent'}, message: '', overlayCSS:  { backgroundColor: '#000', opacity: 0.8 }});
    $($('#task-alerts')).insertAfter($('#waiting-animation h2'));
    initSwitchAnim();
});


function initSwitchAnim() {
    var t = setTimeout(function() {
        $('.anim-steps .next-steps').addClass('active');
    }, 10000);
}