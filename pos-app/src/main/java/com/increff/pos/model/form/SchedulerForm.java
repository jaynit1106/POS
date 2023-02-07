package com.increff.pos.model.form;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter@Setter
public class SchedulerForm {
    private String startDate;
    private String endDate;
}
