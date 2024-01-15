package com.sentryc.graphqlapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;

@Data
@SchemaMapping
@NoArgsConstructor
@AllArgsConstructor
public class PageInput {
    private int page;
    private int size;

}

