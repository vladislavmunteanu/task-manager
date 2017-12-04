var stompClient = null;
// var audio = new Audio('/console/sound/notify.mp3');

var noNotificationFooter = '<span class="no-notifications">No New Notifications</span>';
var notificationFooter = '<span class="notifications-stats">4 Notification(s)</span>';

function notificationController() {
    var socket = new SockJS('/console/notification');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/notify/quick-notification', function (notification) {
            pushNotification(JSON.parse(notification.body));
        });
    });
}

function pushNotification(notification) {

    var notificationId = Date.now().toString(); //TODO id should be more unique
    var notificationTime = new Date().toLocaleTimeString();
    var li = document.createElement('li');
    var type = notification.type;

    li.setAttribute('id', notificationId);

    if (type === 'notice') {
        li.setAttribute('class', 'show-notification notice');
        displayNotification(li,notification,notificationTime,notificationId,'alert-box-notice')
    } else if (type === 'error') {
        li.setAttribute('class', 'show-notification error');
        displayNotification(li,notification,notificationTime,notificationId,'alert-box-error')
    } else {
        li.setAttribute('class', 'show-notification success');
        displayNotification(li,notification,notificationTime,notificationId,'alert-box-success')
    }

    hideNotification(notificationId);

}

function displayNotification(li,notification,notificationTime,notificationId,notificationImg) {
    var notificationTree = $('#notification-list');
    li.innerHTML += buildNotification(notification.message, notificationTime, notificationId,notificationImg);

    notificationTree.prepend(li).addClass('push-down');
    setTimeout(function () {
        notificationTree.removeClass('push-down');
    }, 499);

    $('#no-notifications-footer').remove();
}

function hideNotification(id) {

    var notification = $('#' + id);

    setTimeout(function () {
        notification.removeClass('show-notification');
        notification.addClass('hide-notification');
        setTimeout(function () {
            notification.hide();
        }, 250)
    }, 30000);

    setTimeout(function () {
        removeNotification(id);
        notificationFooterController()
    }, 30250);

}


function closeNotification(id) {

    $('#' + id).hide(300);
    setTimeout(function () {
        removeNotification(id);
        notificationFooterController();
    }, 300);

}

function removeNotification(id) {
    var notification = document.getElementById(id);
    if(notification !== null) {
        notification.parentNode.removeChild(notification);
    }
}

function buildNotification(message, notificationTime, notificationId,notificationClass) {

    return '    <div class="alert-box '+notificationClass+'">\n' +
        '                                <span class="message">'+message+'</span>\n' +
        '                                <span class="time">'+notificationTime+'</span>\n' +
        '                            </div>\n' +
        '                            <div class="alert-box-button">\n' +
        '                                <button type="button" class="notification-close close" onclick="closeNotification('+notificationId+')">&times;</button>\n' +
        '                            </div>';

}


function notificationFooterController(){
    var notificationTree = document.getElementById('notification-list');
    var li = document.createElement('li');
    var notificationTreeSize = notificationTree.getElementsByTagName('li').length;

    if(notificationTreeSize === 0){
        li.setAttribute('class','notification-footer');
        li.setAttribute('id','no-notifications-footer');
        li.innerHTML += noNotificationFooter;
        notificationTree.prepend(li);
    }
}