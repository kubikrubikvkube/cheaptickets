package com.example.tickets.repository.constant;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(indexes = {@Index(name = "idx_city", columnList = "code")})
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
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
    @Column(columnDefinition = "jsonb")
    private String coordinates;

    @Column(columnDefinition = "jsonb")
    private String cases;
    private String time_zone;
    @Column(columnDefinition = "jsonb")
    private String name_translations;
    private String country_code;
}
