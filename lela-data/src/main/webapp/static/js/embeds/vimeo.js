function vimeo_player_loaded(player_id) {
    window.vimeoStatus = null;
    document.getElementById(player_id).api_addEventListener('play', 'playEvent');
    document.getElementById(player_id).api_addEventListener('pause', 'pauseEvent');
    document.getElementById(player_id).api_addEventListener('finish', 'pauseFinish');
    // document.getElementById(player_id).api_addEventListener('playProgress', 'playProgressEvent');
}

function playEvent() {
    trackEvent('video', 'play', String(window.location), 1);
    window.vimeoStatus = 'play';
}

function pauseEvent() {
    trackEvent('video', 'pause', String(window.location), 1);
    window.vimeoStatus = 'pause';
}

function pauseFinish() {
    trackEvent('video', 'finish', String(window.location), 1);
    window.vimeoStatus = 'finish';
}

function trackUnloadTime(player_id) {
    if(window.vimeoStatus != null && document.getElementById('vimeo-player')) {
        var t = Math.floor(document.getElementById('vimeo-player').api_getCurrentTime());
        trackEvent('video', 'leave', String(window.location), Number(t));
    }
}