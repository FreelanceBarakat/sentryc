package com.sentryc.graphqlapi.mapper;

import com.sentryc.graphqlapi.domain.Marketplace;
import com.sentryc.graphqlapi.domain.Producer;
import com.sentryc.graphqlapi.domain.Seller;
import com.sentryc.graphqlapi.domain.SellerInfo;
import com.sentryc.graphqlapi.dto.ProducerSellerStateDTO;
import com.sentryc.graphqlapi.dto.SellerDTO;
import com.sentryc.graphqlapi.dto.SellerStateDTO;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SellerMapperTest {


    private static final String NAME = "TestSellerInfo";
    private static final String EXTERNAL_ID = "amazon.nl";
    private static final String URL = "https://example.com";
    private static final UUID SELLER_ID = UUID.randomUUID();
    private static final UUID PRODUCER_ID = UUID.randomUUID();
    private static final String DESCRIPTION = "TestMarketplace";
    private static final String COUNTRY = "USA";
    private static final UUID MARKETPLACE_ID = UUID.randomUUID();
    private static final String PRODUCER_NAME = "TestProducer";
    @InjectMocks
    private SellerMapperImpl sellerMapper;


    @Test
    void toSellerDTOs_should_map_correctly() {
        // Given
        List<SellerInfo> sellerInfos = createSellerInfos();

        // When
        List<SellerDTO> sellerDTOs = sellerMapper.toSellerDTOs(sellerInfos);

        //then
        assertThat(sellerDTOs)
                .isNotNull()
                .hasSize(1)
                .extracting(
                        SellerDTO::getSellerName,
                        SellerDTO::getExternalId,
                        SellerDTO::getProducerSellerStates,
                        SellerDTO::getMarketplaceId
                ).containsExactly(
                        tuple(
                                NAME,
                                EXTERNAL_ID,
                                Collections.singletonList(
                                        new ProducerSellerStateDTO(
                                                PRODUCER_ID.toString(),
                                                PRODUCER_NAME,
                                                SellerStateDTO.REGULAR,
                                                SELLER_ID.toString()
                                        )
                                ),
                                MARKETPLACE_ID.toString()
                        )


                );

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

    private Producer createProducer() {
        Producer producer = new Producer();
        producer.setId(PRODUCER_ID);
        producer.setName(PRODUCER_NAME);
        producer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        return producer;
    }

    private List<SellerInfo> createSellerInfos() {
        List<SellerInfo> sellerInfos = new ArrayList<>();
        sellerInfos.add(createSellerInfo());
        return sellerInfos;
    }

}