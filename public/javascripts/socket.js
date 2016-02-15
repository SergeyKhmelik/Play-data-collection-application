//SOCKET
var serverURL = 'ws://' + window.location.host + jsRoutes.controllers.ResponseController.responseDataSocket().url;
var socket;

if (window.MozWebSocket)
    socket = new MozWebSocket(serverURL);
else if (window.WebSocket)
    socket = new WebSocket(serverURL);

var responsesCountBadge = $('#responses-count-badge');

socket.onmessage = function (event) {
    console.log("WebSocket message: " + event.data);
    var messageJson = JSON.parse(event.data);

    if (messageJson.hasOwnProperty("responsesCount")) {
        responsesCountBadge.text(messageJson.responsesCount);
    } else if (messageJson.hasOwnProperty("idResponse")) {
        var responsesCount = parseInt(responsesCountBadge.text());
        responsesCountBadge.text(responsesCount + 1);
        addRowToTable(messageJson);
    } else {
        console.error("Unknown message from socket: " + event.data);
    }
};

socket.onerror = function (error) {
    console.error("WebSocket error: " + error);
};