package org.taskm.app.notification;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @MessageMapping("/notification/quick-notification")
    @SendTo("/notify/quick-notification")
    public Notification notify(Notification notification) throws Exception {

        return notification;
    }

}
