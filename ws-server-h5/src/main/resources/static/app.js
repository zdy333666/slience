var ws = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
//    var socket = new SockJS('/gs-guide-websocket');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, function (frame) {
//        setConnected(true);
//        console.log('Connected: ' + frame);
//        stompClient.subscribe('/topic/greetings', function (greeting) {
//            showGreeting(JSON.parse(greeting.body).content);
//        });
//    });


    ws = new WebSocket('ws://localhost:7070/websocket');
    setConnected(true);

    ws.onopen = function () {
        console.log("client has connected");
    };

    ws.onmessage = function (evt) {
        showGreeting(JSON.parse(evt.data).content);
        //showGreeting(evt.data);
    };

    ws.onclose = function () {
        console.log("client has disconnected");
    };

    ws.onerror = function (evt) {
        console.error("" + evt.data + "");

    };

}

function disconnect() {
    if (ws !== null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {

    //ws.send(JSON.stringify({'name': $("#name").val()}));
    ws.send($("#name").val());
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
});
