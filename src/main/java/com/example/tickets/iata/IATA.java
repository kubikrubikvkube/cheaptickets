package com.example.tickets.iata;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_iata", columnList = "id,code,place")})
public class IATA {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @Column(length = 3)
    private String code;
    private String place;
}
