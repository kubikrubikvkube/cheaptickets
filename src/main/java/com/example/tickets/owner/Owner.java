package com.example.tickets.owner;

import com.example.tickets.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Data
@Entity
@Table(indexes = {@Index(name = "idx_owner", columnList = "id,email")})
public class Owner {
    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @JsonManagedReference
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Subscription> subscriptions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Owner)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(id, owner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
