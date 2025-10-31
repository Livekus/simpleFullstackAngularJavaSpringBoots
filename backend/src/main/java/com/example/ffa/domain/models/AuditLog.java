package com.example.ffa.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.Map;

@Entity @Table(name="audit_log")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String entityType;
    private String entityId;
    private String action; // CREATE/UPDATE/DELETE
    private String actor;
    private Instant at;

    // Store as PostgreSQL jsonb using Hibernate 6 JSON support
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition="jsonb")
    private Map<String, Object> data; // snapshot/diff payload (optional)
}