package com.example.auth.stockPile.decorator;

public class FcmNotification {

    private String to;
    private FcmNotificationData data;

    // getters and setters

    public static FcmNotificationBuilder builder() {
        return new FcmNotificationBuilder();
    }

    public static class FcmNotificationBuilder {

        private FcmNotification notification = new FcmNotification();

        public FcmNotificationBuilder to(String to) {
            notification.to = to;
            return this;
        }

        public FcmNotificationBuilder data(FcmNotificationData data) {
            notification.data = data;
            return this;
        }

        public FcmNotification build() {
            return notification;
        }

    }

}