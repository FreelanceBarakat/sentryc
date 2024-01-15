package com.sentryc.graphqlapi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "marketplaces")
@Data
@EqualsAndHashCode
public class Marketplace {

    @Id
    private String id;

    private String description;

}
