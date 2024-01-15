package com.sentryc.graphqlapi.service;

import com.sentryc.graphqlapi.domain.Seller;
import com.sentryc.graphqlapi.domain.SellerFilter;
import com.sentryc.graphqlapi.domain.SellerInfo;
import com.sentryc.graphqlapi.dto.*;
import com.sentryc.graphqlapi.mapper.SellerFilterMapper;
import com.sentryc.graphqlapi.mapper.SellerMapper;
import com.sentryc.graphqlapi.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerServiceImpl {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final SellerFilterMapper sellerFilterMapper;
    private final static int MAX_ALLOWED_PAGE_SIZE = 30;

    @Cacheable(value = "sellers", key = "{#filterDto, #page, #sortBy}")
    public SellerPageableResponse getSellers(final SellerFilterDTO filterDto, final PageInput page, final SellerSortBy sortBy) {
        log.info("getting sellers with filters={}, page={}, sort-by={}", filterDto, page, sortBy);
        final SellerFilter filter = sellerFilterMapper.toSellerFilter(filterDto);
        final Page<Seller> sellersWithFilter = sellerRepository.findSellersWithFilter(filter, toPageable(page, sortBy));

        final List<SellerInfo> sellerInfos = sellersWithFilter.getContent()
                .stream()
                .map(Seller::getSellerInfo)
                .toList();

        return new SellerPageableResponse(
                new PageMeta(page.getPage(), sellerInfos.size()),
                sellerMapper.toSellerDTOs(sellerInfos),
                sellersWithFilter.getSize()
        );
    }

    private Pageable toPageable(final PageInput page, final SellerSortBy sellerSortBy) {
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

        if (page.getSize() > MAX_ALLOWED_PAGE_SIZE) {
            log.warn("Requesting page size of more than the maximum allowed. Lowering it to a reasonable size!");
            page.setSize(MAX_ALLOWED_PAGE_SIZE);
        }

        return PageRequest.of(page.getPage(), page.getSize(), sort);
    }
}
