package com.example.tickets.iata;


import lombok.Data;

@Data
public class IataDto {
    private String code;
    private String place;
    private boolean isCanonical;
}
