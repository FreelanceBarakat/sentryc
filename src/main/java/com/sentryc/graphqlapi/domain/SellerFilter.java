package com.sentryc.graphqlapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerFilter {
    private String searchByName;
    private List<String> producerIds;
    private List<String> marketplaceIds;
}
