package org.taskm.app.notification;

public class Notification {


    private String type;
    private String message;

    public Notification(){}

    public Notification(String name) {
        this.message = name;
    }

    public String getMessage() {
        return message;
    }


    public String getType() {
        return type;
    }
}
