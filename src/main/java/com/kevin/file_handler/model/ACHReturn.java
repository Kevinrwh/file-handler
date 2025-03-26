package com.kevin.file_handler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ACHReturn {
    private String name;
    private double amount;
    private String date;
}
