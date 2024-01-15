package com.sentryc.graphqlapi.repository;


import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.github.database.rider.junit5.api.DBRider;
import com.sentryc.graphqlapi.domain.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DBRider
@DBUnit(alwaysCleanBefore = true,
        schema = "public",
        url = "jdbc:h2:mem:testdb",
        driver = "org.h2.Driver",
        user = "sa"
)
@SpringBootTest
public class SellerRepositoryIntegrationTest {

    private static final String NAME = "TestSellerInfo1";
    private static final String EXTERNAL_ID = "amazon.nl";
    private static final String URL = "https://test-seller-info1.com";
    private static final UUID SELLER_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID SELLER_INFO_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID PRODUCER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String DESCRIPTION = "TestMarketplaceDescription1";
    private static final String COUNTRY = "TestCountry1";
    private static final String MARKETPLACE_ID = "TestMarketplaceId1";
    private static final String PRODUCER_NAME = "TestProducer1";

    public static final String SELLER_INFO_NAME_1 = "TestSellerInfo1";

    @Autowired
    private SellerRepository sellerRepository;

    public static Stream<Arguments> getFilters() {
        final SellerFilter filter1 = new SellerFilter();
        filter1.setSearchByName(SELLER_INFO_NAME_1);

        final SellerFilter filter2 = new SellerFilter();
        filter2.setProducerIds(List.of(PRODUCER_ID.toString()));

        final SellerFilter filter3 = new SellerFilter();
        filter3.setMarketplaceIds(List.of(MARKETPLACE_ID));

        return Stream.of(
                Arguments.arguments(filter1),
                Arguments.arguments(filter2),
                Arguments.arguments(filter3)
        );
    }

    @MethodSource("getFilters")
    @ParameterizedTest
    @DataSet(value = "datasets/repository_integration_test_initial_data.yaml",
            strategy = SeedStrategy.CLEAN_INSERT,
            disableConstraints = true, cleanAfter = true, transactional = true)
    public void findSellersWithFilter_when_given_filer_should_return_matching_results(final SellerFilter filter) {
        // When
        Page<Seller> resultPage = sellerRepository.findSellersWithFilter(filter, PageRequest.of(0, 10));
        final Seller expectedSeller = createSeller();
        // Then
        assertThat(resultPage.getContent())
                .hasSize(1)
                .extracting(
                        Seller::getId,
                        Seller::getSellerInfo,
                        Seller::getState
                )
                .containsExactly(
                        tuple(
                                expectedSeller.getId(),
                                expectedSeller.getSellerInfo(),
                                expectedSeller.getState()
                        )
                );

        assertThat(resultPage.getContent())
                .hasSize(1)
                .extracting(
                        Seller::getProducer
                ).extracting(
                        Producer::getId,
                        Producer::getName
                ).containsExactly(
                        tuple(
                                PRODUCER_ID,
                                PRODUCER_NAME
                        )
                );
    }


    private SellerInfo createSellerInfo() {
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setId(SELLER_INFO_ID);
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
        marketplace.setId(MARKETPLACE_ID);
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

    private Producer createProducer() {
        Producer producer = new Producer();
        producer.setId(PRODUCER_ID);
        producer.setName(PRODUCER_NAME);
        producer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return producer;
    }


    private Seller createSeller() {
        Seller seller = new Seller();
        seller.setId(SELLER_ID);
        seller.setSellerInfo(createSellerInfo());
        seller.setProducer(createProducer());
        seller.setState(Seller.SellerState.REGULAR);
        return seller;
    }
}