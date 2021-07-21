package io.bgr;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="demo", indexes = {
        @Index(columnList = "id", name = "ndx_demo_id"),
        @Index(columnList = "t0", name = "ndx_demo_t0")
    }
)
public class DemoModel extends PanacheEntityBase {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    public UUID id;

    public String title;
    public String content;

    @CreationTimestamp
    public LocalDateTime t0;
}