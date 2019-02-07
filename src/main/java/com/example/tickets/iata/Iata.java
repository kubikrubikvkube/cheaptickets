package com.example.tickets.iata;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "iata", indexes = {@Index(name = "idx_iata", columnList = "id,place")})
public class Iata {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @Column(length = 3)
    private String code;

    private String place;

    private boolean isCanonical;
}
