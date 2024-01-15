package com.sentryc.graphqlapi.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
@Table(name = "sellers")
public class Seller {

    @Id
    private UUID id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "producer_id", nullable = false)
    private Producer producer;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "seller_info_id", nullable = false)
    private SellerInfo sellerInfo;

    @Enumerated(EnumType.STRING)
    private SellerState state;

    public enum SellerState {
        REGULAR,
        WHITELISTED,
        GREYLISTED,
        BLACKLISTED
    }
}