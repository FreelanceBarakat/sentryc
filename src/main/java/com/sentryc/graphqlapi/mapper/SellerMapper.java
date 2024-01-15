package com.sentryc.graphqlapi.mapper;

import com.sentryc.graphqlapi.domain.Seller;
import com.sentryc.graphqlapi.domain.SellerInfo;
import com.sentryc.graphqlapi.dto.ProducerSellerStateDTO;
import com.sentryc.graphqlapi.dto.SellerDTO;
import com.sentryc.graphqlapi.dto.SellerStateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SellerMapper {

    @Mappings({
            @Mapping(target = "sellerName", source = "name"),
            @Mapping(target = "externalId", source = "externalId"),
            @Mapping(target = "producerSellerStates", source = "sellers"),
            @Mapping(target = "marketplaceId", source = "marketplace.id"),
    })
    SellerDTO toSellerDTO(final SellerInfo sellerInfo);

    List<SellerDTO> toSellerDTOs(final List<SellerInfo> sellerInfos);

    @Mappings({
            @Mapping(target = "producerId", source = "seller.producer.id"),
            @Mapping(target = "producerName", source = "seller.producer.name"),
            @Mapping(target = "sellerState", source = "seller.state"),
            @Mapping(target = "sellerId", source = "seller.id")
    })
    ProducerSellerStateDTO toProducerSellerStateDTO(final Seller seller);


    List<ProducerSellerStateDTO> toProducerSellerStateDTOs(final List<Seller> seller);

    SellerStateDTO toSellerStateDTO(final Seller.SellerState sellerState);


}