package org.taskm.app.notification;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/notification")
    @SendTo("/notify")
    public Notification notify(Message message) throws Exception {

        return new Notification(message.getName());
    }

}
