$(document).ready(function() {
    var sock = new SockJS('/messages');
    sock.onopen = function() {
        console.log('opened');
        $('#loading').toggleClass("hidden-xs-up");
        $('#app').toggleClass("hidden-xs-up");
        this.onmessage = function(e) {
            addMessage(e.data);
        };
        $('#msgForm').submit(function() {
            var msg = $('#msg');
            var content = msg.val();
            if (content != '') {
                msg.val('');
                addMessage('<strong>me:</strong> ' + content);
                sock.send(content);
            }

            return false;
        });
    };
    sock.onclose = function() {
        console.log('closed');
        addMessage('Disconnected')
    };
});

function addMessage(msg) {
    var container = $('#messages');
    container.append('<br/>' + msg);
    container.scrollTop(container[0].scrollHeight);
}
