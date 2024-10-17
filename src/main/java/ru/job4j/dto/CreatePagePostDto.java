package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePagePostDto {
    private String description;
    private int price;
    private String carName;
    private int carModelId;
    private int engineId;
    private LocalDateTime created;
}
