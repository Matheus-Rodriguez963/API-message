package com.example.apimessage.model;

import java.time.ZonedDateTime;

public class ScheduleInfo {

    private final String id;
    private final String channel;
    private final String recipient;
    private final String message;
    private final String time;
    private final String timeZone;
    private final String whatsappFrom;
    private final ZonedDateTime nextRun;

    public ScheduleInfo(String id, String channel, String recipient, String message, String time, String timeZone, String whatsappFrom, ZonedDateTime nextRun) {
        this.id = id;
        this.channel = channel;
        this.recipient = recipient;
        this.message = message;
        this.time = time;
        this.timeZone = timeZone;
        this.whatsappFrom = whatsappFrom;
        this.nextRun = nextRun;
    }

    public String getId() {
        return id;
    }

    public String getChannel() {
        return channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public String getWhatsappFrom() {
        return whatsappFrom;
    }

    public ZonedDateTime getNextRun() {
        return nextRun;
    }
}
