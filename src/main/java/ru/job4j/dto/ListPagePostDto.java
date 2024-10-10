package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ListPagePostDto {
    private int id;
    private LocalDateTime created;
    private String userName;
    private BigInteger price;
    private String carName;
    private int firstFileId;
    private boolean sold;
}
