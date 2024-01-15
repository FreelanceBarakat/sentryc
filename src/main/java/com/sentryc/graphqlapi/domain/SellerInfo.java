package com.sentryc.graphqlapi.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "seller_infos")
@Data
@EqualsAndHashCode
public class SellerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "marketplace_id", nullable = false)
    private Marketplace marketplace;

    private String name;

    private String url;

    private String country;

    @Column(name = "external_id")
    private String externalId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "sellerInfo", fetch = FetchType.EAGER)
    private List<Seller> sellers;


}