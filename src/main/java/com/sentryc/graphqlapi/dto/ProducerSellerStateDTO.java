package com.sentryc.graphqlapi.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProducerSellerStateDTO {
    private String producerId;
    private String producerName;
    private SellerStateDTO sellerState;
    private String sellerId;

}