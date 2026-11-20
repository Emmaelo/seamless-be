package com.northwest.seamless.dtos.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HolidayRequest {
    private LocalDate date;
    private String holiday;

}
