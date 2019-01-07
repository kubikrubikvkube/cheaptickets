package com.example.tickets.city;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_city", columnList = "code")})
public class City {

    /**
     * PK айдишник базы
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Метка даты записи в БД, когда была записана константа билет.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Calendar createdOn;

    private String code;
    private String name;
    private String coordinates;
    private String cases;
    private String time_zone;
    private String name_translations;
    private String country_code;
}
