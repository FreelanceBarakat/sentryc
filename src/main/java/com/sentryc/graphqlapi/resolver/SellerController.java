package com.sentryc.graphqlapi.resolver;

import com.sentryc.graphqlapi.dto.PageInput;
import com.sentryc.graphqlapi.dto.SellerFilterDTO;
import com.sentryc.graphqlapi.dto.SellerPageableResponse;
import com.sentryc.graphqlapi.dto.SellerSortBy;
import com.sentryc.graphqlapi.service.SellerServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class SellerController {

    private final SellerServiceImpl sellerServiceImpl;

    @QueryMapping
    public SellerPageableResponse sellers(@Argument("filter") final SellerFilterDTO filter,
                                          @Argument("page") final PageInput page,
                                          @Argument("sortBy") final SellerSortBy sortBy) {
        return sellerServiceImpl.getSellers(filter, page, sortBy);
    }

}


