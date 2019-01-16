package com.example.tickets.subscription;

import com.example.tickets.owner.Owner;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(indexes = {@Index(name = "idx_subscription", columnList = "id,origin,destination,departDate,returnDate")})
public class Subscription {


    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;

    /**
     * Кто создал подписку
     */

    @JsonBackReference
    @ManyToOne
    private Owner owner;
    /**
     * IAT отправления
     */
    private String origin;
    /**
     * IAT назначения
     */
    private String destination;

    /**
     * Дата отправления
     */
    private Date departDate;

    /**
     * Дата возвращения
     */
    private Date returnDate;

}
