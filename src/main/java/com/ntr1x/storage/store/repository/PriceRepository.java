package com.ntr1x.storage.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.store.model.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {
    
    @Query(
        " SELECT p"
      + " FROM Price p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "    AND (:user IS NULL OR (:user = 0 AND p.user IS NULL) OR p.user.id = :user)"
      + "   AND (:relate IS NULL OR (:relate = 0 AND p.relate IS NULL) OR p.relate.id = :relate)"
    )
    Page<Price> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("relate") Long relate,
        Pageable pageable
    );
    
    @Query(
        " SELECT p"
      + " FROM Price p"
      + " WHERE (:scope IS NULL OR p.scope = :scope)"
      + "   AND (p.id = :id)"
    )
    Price select(@Param("scope") Long scope, @Param("id") long id);
}
