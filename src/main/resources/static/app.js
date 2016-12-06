$(document).ready(function() {
    var sock = new SockJS('/message');
    sock.onopen = function() {
        console.log('opened');
        $('#loading').toggleClass("hidden-xs-up");
        $('#app').toggleClass("hidden-xs-up");
        this.onmessage = function(e) {
            console.log(e.data);
            var event = JSON.parse(e.data);
            if (event.type === 'MESSAGE') {
                addMessage('<strong>' + event.payload.author + ':</strong> ' + event.payload.content);
            } else if (event.type === 'USER_JOINED') {
                addMessage('<strong> User joined: ' + event.payload.username + '</strong>');
            } else if (event.type === 'USER_LEFT') {
                addMessage('<strong> User left: ' + event.payload.username + '</strong>');
            }
        };
        $('#msgForm').submit(function() {
            var msg = $('#msg');
            var content = msg.val();
            if (content !== '') {
                msg.val('');
                addMessage('<strong>me:</strong> ' + content);
                sock.send(JSON.stringify({
                    type: 'MESSAGE',
                    payload: {
                        content: content
                    }
                }));
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
