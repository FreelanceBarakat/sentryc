package com.sentryc.graphqlapi.mapper;

import com.sentryc.graphqlapi.domain.SellerFilter;
import com.sentryc.graphqlapi.dto.SellerFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SellerFilterMapper {

    SellerFilter toSellerFilter(final SellerFilterDTO sellerFilterDTO);
}
