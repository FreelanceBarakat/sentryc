package com.sentryc.graphqlapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

import java.util.List;

@Data
@SchemaMapping
@NoArgsConstructor
@AllArgsConstructor
public class SellerPageableResponse {
    private PageMeta meta;
    private List<SellerDTO> data;
    private int totalCount;

}