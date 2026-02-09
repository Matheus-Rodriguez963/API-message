package com.example.apimessage.model;

import java.time.ZonedDateTime;

public class ScheduleResponse {

    private String id;
    private String channel;
    private ZonedDateTime nextRun;

    public ScheduleResponse(String id, String channel, ZonedDateTime nextRun) {
        this.id = id;
        this.channel = channel;
        this.nextRun = nextRun;
    }

    public String getId() {
        return id;
    }

    public String getChannel() {
        return channel;
    }

    public ZonedDateTime getNextRun() {
        return nextRun;
    }
}
