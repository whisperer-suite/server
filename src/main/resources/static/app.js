$(document).ready(function() {
    var sock = new SockJS('/messages');
    sock.onopen = function() {
        console.log('opened');
        $('#loading').toggleClass("hidden-xs-up");
        $('#app').toggleClass("hidden-xs-up");
        this.onmessage = function(e) {
            $('#messages').append('<br/>server: ' + e.data);
        };
        $('#msgForm').submit(function() {
            var msg = $('#msg');
            $('#messages').append('<br/>me: ' + msg.val());
            sock.send(msg.val());
            msg.val("");

            return false;
        });
    };
    sock.onclose = function() {
        console.log('closed');
    };
});
