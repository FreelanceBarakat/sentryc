package com.sentryc.graphqlapi.service;

import com.sentryc.graphqlapi.domain.*;
import com.sentryc.graphqlapi.dto.*;
import com.sentryc.graphqlapi.mapper.SellerFilterMapper;
import com.sentryc.graphqlapi.mapper.SellerMapper;
import com.sentryc.graphqlapi.repository.SellerRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SellerServiceImplTest {
    private static final String NAME = "TestSellerInfo";
    private static final String EXTERNAL_ID = "amazon.nl";
    private static final String URL = "https://example.com";
    private static final UUID SELLER_ID = UUID.randomUUID();
    private static final UUID PRODUCER_ID = UUID.randomUUID();
    private static final String DESCRIPTION = "TestMarketplace";
    private static final String COUNTRY = "USA";
    private static final UUID MARKETPLACE_ID = UUID.randomUUID();
    private static final String PRODUCER_NAME = "TestProducer";
    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private SellerMapper sellerMapper;

    @Mock
    private SellerFilterMapper sellerFilterMapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @EnumSource(SellerSortBy.class)
    @ParameterizedTest
    void getSellers_ShouldReturnSellerPageableResponse(final SellerSortBy sortBy) {
        // Arrange
        final SellerFilterDTO filterDTO = new SellerFilterDTO(
                "Example Seller name",
                Collections.singletonList(PRODUCER_ID.toString()),
                Collections.singletonList(MARKETPLACE_ID.toString())
        );

        // size will get lowered to protect from overloading the service
        final PageInput pageInput = new PageInput(0, 9876);

        final SellerFilter filter = new SellerFilter(
                "Example Seller name",
                Collections.singletonList(PRODUCER_ID.toString()),
                Collections.singletonList(MARKETPLACE_ID.toString())
        );

        final Pageable pageable = PageRequest.of(
                pageInput.getPage(),
                pageInput.getSize(),
                getSort(sortBy)
        );


        mockMappers(filterDTO, filter);

        // when
        final SellerPageableResponse result = sellerService.getSellers(filterDTO, pageInput, sortBy);

        // then

        final List<SellerDTO> expectedSellerDTOs = List.of(
                new SellerDTO(
                        NAME,
                        EXTERNAL_ID,
                        List.of(new ProducerSellerStateDTO(
                                PRODUCER_ID.toString(),
                                PRODUCER_NAME,
                                SellerStateDTO.REGULAR, SELLER_ID.toString())),
                        MARKETPLACE_ID.toString())
        );

        assertThat(result)
                .isNotNull()
                .extracting(
                        SellerPageableResponse::getData,
                        SellerPageableResponse::getMeta,
                        SellerPageableResponse::getTotalCount
                ).containsExactly(
                        expectedSellerDTOs,
                        new PageMeta(0, 1),
                        1
                );
    }

    private void mockMappers(final SellerFilterDTO filterDTO, final SellerFilter filter) {
        final PageImpl<Seller> sellerPage = new PageImpl(
                List.of(createSeller()),
                PageRequest.of(
                        0,
                        1
                ),
                1
        );

        when(sellerFilterMapper.toSellerFilter(filterDTO)).thenReturn(filter);
        when(sellerRepository.findSellersWithFilter(any(SellerFilter.class), any(Pageable.class))).thenReturn(sellerPage);
        when(sellerMapper.toSellerDTOs(any())).thenReturn(List.of(
                new SellerDTO(
                        NAME,
                        EXTERNAL_ID,
                        List.of(new ProducerSellerStateDTO(
                                PRODUCER_ID.toString(),
                                PRODUCER_NAME,
                                SellerStateDTO.REGULAR, SELLER_ID.toString())),
                        MARKETPLACE_ID.toString())
        ));
    }

    private Seller createSeller() {
        Seller seller = new Seller();
        seller.setId(SELLER_ID);
        seller.setSellerInfo(createSellerInfo());
        seller.setProducer(createProducer());
        seller.setState(Seller.SellerState.REGULAR);
        return seller;
    }

    private Producer createProducer() {
        Producer producer = new Producer();
        producer.setId(PRODUCER_ID);
        producer.setName(PRODUCER_NAME);
        producer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return producer;
    }

    private SellerInfo createSellerInfo() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setId(UUID.randomUUID());
        sellerInfo.setName(NAME);
        sellerInfo.setMarketplace(createMarketplace());
        sellerInfo.setExternalId(EXTERNAL_ID);
        sellerInfo.setUrl(URL);

        sellerInfo.setCountry(COUNTRY);
        sellerInfo.setSellers(createSellersForSellerInfo(sellerInfo));
        return sellerInfo;
    }

    private Marketplace createMarketplace() {
        Marketplace marketplace = new Marketplace();
        marketplace.setId(MARKETPLACE_ID.toString());
        marketplace.setDescription(DESCRIPTION);
        return marketplace;
    }

    private List<Seller> createSellersForSellerInfo(SellerInfo sellerInfo) {
        List<Seller> sellers = new ArrayList<>();
        sellers.add(createSeller(sellerInfo));
        return sellers;
    }

    private Seller createSeller(SellerInfo sellerInfo) {
        Seller seller = new Seller();
        seller.setId(SELLER_ID);
        seller.setSellerInfo(sellerInfo);
        seller.setProducer(createProducer());
        seller.setState(Seller.SellerState.REGULAR);
        return seller;
    }

    private Sort getSort(final SellerSortBy sellerSortBy) {
        final Sort sort;
        switch (sellerSortBy) {
            case SELLER_INFO_EXTERNAL_ID_ASC -> sort = Sort.by("sellerInfo.externalId").ascending();
            case SELLER_INFO_EXTERNAL_ID_DESC -> sort = Sort.by("sellerInfo.externalId").descending();
            case NAME_ASC -> sort = Sort.by("sellerInfo.name").ascending();
            case NAME_DESC -> sort = Sort.by("sellerInfo.name").descending();
            case MARKETPLACE_ID_ASC -> sort = Sort.by("marketplace.id").ascending();
            case MARKETPLACE_ID_DESC -> sort = Sort.by("marketplace.id").descending();
            default -> sort = null;
        }

        return sort;
    }
}