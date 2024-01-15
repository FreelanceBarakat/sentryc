package com.sentryc.graphqlapi.repository;

import com.sentryc.graphqlapi.domain.Seller;
import com.sentryc.graphqlapi.domain.SellerFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface SellerRepository extends PagingAndSortingRepository<Seller, UUID>, CrudRepository<Seller, UUID> {


    @Query("SELECT seller FROM Seller seller WHERE " +
            // Avoided using %:#{#filter.searchByName}% to fully utilize the index,
            //  since using wildcards will prevent using the index
            "(:#{#filter.searchByName} IS NULL OR seller.sellerInfo.name LIKE :#{#filter.searchByName}) " +
            "AND (:#{#filter.producerIds} IS NULL OR seller.producer.id IN :#{#filter.producerIds}) " +
            "AND (:#{#filter.marketplaceIds} IS NULL OR seller.sellerInfo.marketplace.id IN :#{#filter.marketplaceIds})")
    Page<Seller> findSellersWithFilter(
            @Param("filter") final SellerFilter filter,
            final Pageable pageable);
}



