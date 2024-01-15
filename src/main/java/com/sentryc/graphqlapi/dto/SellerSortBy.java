package com.sentryc.graphqlapi.dto;

import org.springframework.graphql.data.method.annotation.SchemaMapping;

@SchemaMapping
public enum SellerSortBy {
    SELLER_INFO_EXTERNAL_ID_ASC,
    SELLER_INFO_EXTERNAL_ID_DESC,
    NAME_ASC,
    NAME_DESC,
    MARKETPLACE_ID_ASC,
    MARKETPLACE_ID_DESC
}