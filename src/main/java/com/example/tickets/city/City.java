package com.example.tickets.city;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_city", columnList = "code")})
public class City {
    @Id
    @GeneratedValue
    private Long id;


    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    private String code;
    private String name;
    private String coordinates;
    private String cases;
    private String time_zone;
    private String name_translations;
    private String country_code;
}
