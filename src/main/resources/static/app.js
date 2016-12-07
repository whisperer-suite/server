function addMessage(msg) {
    let container = $("#messages");
    container.append("<br/>" + msg);
    container.scrollTop(container[0].scrollHeight);
}

$(document).ready(function() {
    let type;
    let sock;
    if (typeof WebSocket !== "undefined") {
        let protocol = window.location.protocol === "http:" ? "ws:" : "wss:";
        sock = new WebSocket(protocol + "//" + window.location.host + "/websocket");
        type = "WebSocket"
    } else if (typeof SockJS !== "undefined") {
        sock = new SockJS("/sockjs");
        type = "SockJS"
    } else {
        $("#loading-title").innerHTML = "Error: no support for WebSocket or SockJS";
        return;
    }

    sock.onopen = function() {
        $("#loading").toggleClass("hidden-xs-up");
        $("#app").toggleClass("hidden-xs-up");
        addMessage("Connected via " + type);

        this.onmessage = function(e) {
            let event = JSON.parse(e.data);
            if (event.type === "MESSAGE") {
                addMessage("<strong>" + event.payload.author + ":</strong> " + event.payload.content);
            } else if (event.type === "USER_JOINED") {
                addMessage("<strong> User joined: " + event.payload.username + "</strong>");
            } else if (event.type === "USER_LEFT") {
                addMessage("<strong> User left: " + event.payload.username + "</strong>");
            }
        };

        $("#msgForm").submit(function() {
            let msg = $("#msg");
            let content = msg.val();
            if (content !== "") {
                msg.val("");
                addMessage("<strong>me:</strong> " + content);
                sock.send(JSON.stringify({
                    type: "MESSAGE",
                    payload: { content }
                }));
            }

            return false;
        });
    };
    sock.onclose = function() {
        addMessage("Disconnected");
    };
});
