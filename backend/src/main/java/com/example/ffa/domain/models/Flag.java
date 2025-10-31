package com.example.ffa.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "f_flag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flag {
    @Id
    private UUID id;
    @Column(name = "key_slug", nullable = false, unique = true, length = 80)
    private String key;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @Column(nullable = false)
    private boolean enabled;
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] tags;
    private String owner;
    @Version
    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer version;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
    private String createdBy;
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
    private String updatedBy;
}