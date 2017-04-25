package com.ntr1x.storage.store.repository;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ntr1x.storage.store.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query(
        " SELECT o"
      + " FROM Order o"
      + " WHERE (:scope IS NULL OR o.scope = :scope)"
      + "    AND (:user IS NULL OR o.user.id = :user)"
      + "    AND (:relate IS NULL OR o.relate.id = :relate)"
      + "    AND (:state IS NULL OR o.state = :state)"
      + "    AND (:since IS NULL OR o.created >= :since)"
      + "    AND (:until IS NULL OR o.created <= :until)"
    )
    Page<Order> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("relate") Long relate,
        @Param("state") Order.State state,
        @Param("since") LocalDateTime since,
        @Param("until") LocalDateTime until,
        Pageable pageable
    );
    
    @Query(
        " SELECT o"
      + " FROM Order o"
      + " WHERE (:scope IS NULL OR o.scope = :scope)"
      + "    AND (:user IS NULL OR o.user.id = :user)"
      + "    AND (:relate IS NULL OR o.relate.id = :relate)"
      + "    AND (o.state IN :states)"
      + "    AND (:since IS NULL OR o.created >= :since)"
      + "    AND (:until IS NULL OR o.created <= :until)"
    )
    Page<Order> query(
        @Param("scope") Long scope,
        @Param("user") Long user,
        @Param("relate") Long relate,
        @Param("states") Set<Order.State> states,
        @Param("since") LocalDateTime since,
        @Param("until") LocalDateTime until,
        Pageable pageable
    );
    
    @Query(
        " SELECT o"
      + " FROM Order o"
      + " WHERE (:scope IS NULL OR o.scope = :scope)"
      + "    AND (o.id = :id)"
    )
    Order select(@Param("scope") Long scope, @Param("id") long id);
}
