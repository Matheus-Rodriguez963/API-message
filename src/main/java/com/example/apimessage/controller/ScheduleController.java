package com.example.apimessage.controller;

import com.example.apimessage.model.ScheduleInfo;
import com.example.apimessage.model.ScheduleRequest;
import com.example.apimessage.model.ScheduleResponse;
import com.example.apimessage.service.ScheduleService;
import jakarta.validation.Valid;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/whatsapp")
    public ResponseEntity<ScheduleResponse> scheduleWhatsApp(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.scheduleWhatsApp(request));
    }

    @PostMapping("/email")
    public ResponseEntity<ScheduleResponse> scheduleEmail(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.scheduleEmail(request));
    }

    @GetMapping
    public Collection<ScheduleInfo> listSchedules() {
        return scheduleService.listSchedules();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSchedule(@PathVariable String id) {
        boolean canceled = scheduleService.cancelSchedule(id);
        if (!canceled) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
