package com.example.tickets.owner;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_owner", columnList = "id,name,email")})
public class Owner {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private Date creationTimestamp;

    private String name;

    private String email;
}
