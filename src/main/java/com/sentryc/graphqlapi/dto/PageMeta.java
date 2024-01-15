package com.sentryc.graphqlapi.dto;

import lombok.Value;

@Value
public class PageMeta {
    int page;
    int size;

}