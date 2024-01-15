package com.sentryc.graphqlapi.mapper;

import com.sentryc.graphqlapi.domain.SellerFilter;
import com.sentryc.graphqlapi.dto.SellerFilterDTO;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SellerFilterMapperTest {
    private static final String PRODUCER_ID_1 = UUID.randomUUID().toString();
    private static final String PRODUCER_ID_2 = UUID.randomUUID().toString();
    private static final String MARKETPLACE_ID_1 = UUID.randomUUID().toString();
    private static final String MARKETPLACE_ID_2 = UUID.randomUUID().toString();
    private static final String NAME = "Example Seller";
    private final SellerFilterMapper sellerFilterMapper = new SellerFilterMapperImpl();

    @Test
    void testToSellerFilter() {
        // Given
        SellerFilterDTO sellerFilterDTO = createSellerFilterDTO();

        // When
        SellerFilter sellerFilter = sellerFilterMapper.toSellerFilter(sellerFilterDTO);

        // Then
        assertThat(sellerFilter)
                .isNotNull()
                .isEqualTo(
                        new SellerFilter(
                                sellerFilterDTO.getSearchByName(),
                                sellerFilterDTO.getProducerIds(),
                                sellerFilterDTO.getMarketplaceIds()
                        )
                );
    }


    private SellerFilterDTO createSellerFilterDTO() {
        return new SellerFilterDTO(
                NAME,
                createProducerIds(),
                createMarketplaceIds()
        );
    }

    private List<String> createProducerIds() {
        return Arrays.asList(PRODUCER_ID_1, PRODUCER_ID_2);
    }

    private List<String> createMarketplaceIds() {
        return Arrays.asList(MARKETPLACE_ID_1, MARKETPLACE_ID_2);
    }
}