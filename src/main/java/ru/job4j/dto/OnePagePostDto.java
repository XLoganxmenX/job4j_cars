package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OnePagePostDto {
    private int id;
    private String carName;
    private BigInteger price;
    private String engineName;
    private String carModelName;
    private String description;
    private List<String> ownerNames;
    private List<String> participateNames;
    private String userName;
    private int headFileId;
    private List<Integer> secondaryFileIds;
    private LocalDateTime created;
    private boolean sold;
}
