package com.example.tickets.owner;

import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @JsonManagedReference
    @OneToMany(mappedBy = "owner")
    @CollectionTable(name = "owner_subscriptions")
    private List<Subscription> subscriptions;
}
