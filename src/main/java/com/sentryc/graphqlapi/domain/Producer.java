package com.sentryc.graphqlapi.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "producers")
@Data
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

}