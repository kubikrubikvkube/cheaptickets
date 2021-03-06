package com.example.tickets.iata;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "iata", indexes = {@Index(name = "idx_iata", columnList = "id,place")})
public class Iata {
    @Id
    @GeneratedValue
    private Long id;

    private String code;

    private String place;

    private boolean isCanonical;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iata)) return false;
        Iata iata = (Iata) o;
        return Objects.equals(id, iata.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
