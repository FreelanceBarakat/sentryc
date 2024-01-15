package com.sentryc.graphqlapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.List;


@SchemaMapping
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerFilterDTO {
    private String searchByName;
    private List<String> producerIds;
    private List<String> marketplaceIds;

}