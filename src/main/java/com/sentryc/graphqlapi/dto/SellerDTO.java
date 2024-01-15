package com.sentryc.graphqlapi.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode
public class SellerDTO {
    String sellerName;
    String externalId;
    List<ProducerSellerStateDTO> producerSellerStates;
    String marketplaceId;

}