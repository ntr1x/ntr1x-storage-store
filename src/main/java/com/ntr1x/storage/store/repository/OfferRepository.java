package com.ntr1x.storage.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.store.model.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	@Query(
        " SELECT o"
      + " FROM Offer o"
      + " WHERE (:scope IS NULL OR o.scope = :scope)"
      + "	AND (:user IS NULL OR (:user = 0 AND o.user IS NULL) OR o.user.id = :user)"
      + "	AND (:relate IS NULL OR (:relate = 0 AND o.relate IS NULL) OR o.relate.id = :relate)"
    )
    Page<Offer> query(
		@Param("scope") Long scope,
		@Param("user") Long user,
		@Param("relate") Long relate,
		Pageable pageable
	);
	
	@Query(
        " SELECT o"
      + " FROM Offer o"
      + " WHERE (:scope IS NULL OR o.scope = :scope)"
      + "	AND (o.id = :id)"
    )
	Offer select(@Param("scope") Long scope, @Param("id") long id);
}
