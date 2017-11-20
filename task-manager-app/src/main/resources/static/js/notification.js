var stompClient = null;
var audio = new Audio('/console/sound/notify.mp3');

function connect() {
    var socket = new SockJS('/console/notification');


    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/notify', function (notification) {
            showNotification(JSON.parse(notification.body).content);
            audio.play();
        });
    });
}

function showNotification(message) {
    var response = document.getElementById('notification-list');
    var notificationId = Date.now().toString(); //TODO id should be more unique
    var li = document.createElement('li');

    li.setAttribute('id', notificationId);
    li.setAttribute('class', 'show-notification');
    li.innerHTML += '<span>' + message + '</span>';
    response.insertBefore(li, response.childNodes[0]);
    hideNotification(notificationId);

}

function hideNotification(id) {

    var notification = $('#' + id);

    setTimeout(function () {
        notification.removeClass('show-notification');
        notification.addClass('hide-notification');
        setTimeout(function () {
            notification.hide()
        }, 250)
    }, 5000);
}




