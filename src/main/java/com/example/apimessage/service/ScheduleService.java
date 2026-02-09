package com.example.apimessage.service;

import com.example.apimessage.model.ScheduleInfo;
import com.example.apimessage.model.ScheduleRequest;
import com.example.apimessage.model.ScheduleResponse;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final TaskScheduler taskScheduler;
    private final MessageSenderService messageSenderService;
    private final Map<String, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>();

    public ScheduleService(TaskScheduler taskScheduler, MessageSenderService messageSenderService) {
        this.taskScheduler = taskScheduler;
        this.messageSenderService = messageSenderService;
    }

    public ScheduleResponse scheduleWhatsApp(ScheduleRequest request) {
        return schedule(
            "whatsapp",
            request,
            () -> messageSenderService.sendWhatsApp(request.getRecipient(), request.getMessage(), request.getWhatsappFrom()));
    }

    public ScheduleResponse scheduleEmail(ScheduleRequest request) {
        return schedule("email", request, () -> messageSenderService.sendEmail(request.getRecipient(), request.getMessage()));
    }

    public Collection<ScheduleInfo> listSchedules() {
        return scheduledTasks.values().stream()
            .map(task -> new ScheduleInfo(
                task.id,
                task.channel,
                task.recipient,
                task.message,
                task.time,
                task.timeZone,
                task.whatsappFrom,
                task.nextRun()))
            .toList();
    }

    public boolean cancelSchedule(String id) {
        ScheduledTask task = scheduledTasks.remove(id);
        if (task == null) {
            return false;
        }
        return task.future.cancel(false);
    }

    private ScheduleResponse schedule(String channel, ScheduleRequest request, Runnable action) {
        ZoneId zoneId = ZoneId.of(request.getTimeZone());
        LocalTime time = LocalTime.parse(request.getTime());
        String cron = String.format("0 %d %d * * *", time.getMinute(), time.getHour());
        CronTrigger trigger = new CronTrigger(cron, zoneId);
        String id = UUID.randomUUID().toString();
        ScheduledFuture<?> future = taskScheduler.schedule(action, trigger);

        ScheduledTask task = new ScheduledTask(
            id,
            channel,
            request.getRecipient(),
            request.getMessage(),
            request.getTime(),
            request.getTimeZone(),
            request.getWhatsappFrom(),
            future,
            CronExpression.parse(cron));
        scheduledTasks.put(id, task);

        return new ScheduleResponse(id, channel, task.nextRun());
    }

    private static class ScheduledTask {
        private final String id;
        private final String channel;
        private final String recipient;
        private final String message;
        private final String time;
        private final String timeZone;
        private final String whatsappFrom;
        private final ScheduledFuture<?> future;
        private final CronExpression cronExpression;

        private ScheduledTask(
            String id,
            String channel,
            String recipient,
            String message,
            String time,
            String timeZone,
            String whatsappFrom,
            ScheduledFuture<?> future,
            CronExpression cronExpression) {
            this.id = id;
            this.channel = channel;
            this.recipient = recipient;
            this.message = message;
            this.time = time;
            this.timeZone = timeZone;
            this.whatsappFrom = whatsappFrom;
            this.future = future;
            this.cronExpression = cronExpression;
        }

        private ZonedDateTime nextRun() {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(timeZone));
            return cronExpression.next(now);
        }
    }
}
