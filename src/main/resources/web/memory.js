
var wsocket;
function connect() {
    wsocket = new WebSocket('ws://' + document.location.host + '/ratesrv');
    wsocket.onmessage = onMessage;
}
function onMessage(evt) {
    document.getElementById("rate").innerHTML=evt.data;
}
window.addEventListener("load", connect, false);