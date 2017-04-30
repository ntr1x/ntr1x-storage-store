package com.ntr1x.storage.store.model;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.security.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "prices"
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Price extends Resource {
    
    @Getter
    @RequiredArgsConstructor
    public enum PriceCurrency {
        
        USD(Currency.getInstance("USD")),
        EUR(Currency.getInstance("EUR")),
        RUR(Currency.getInstance("RUR")),
        ;
        
        private final Currency currency;
    }
    
    @XmlElement
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "UserId", nullable = true, updatable = false)
    private User user;
    
    @XmlElement
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "RelateId", nullable = true, updatable = false)
    private Resource relate;
    
    @Column(name = "Title", nullable = false)
    private String title;
    
    @Column(name = "Price", nullable = false, scale = 2, precision = 10)
    private BigDecimal price;
    
    @Column(name = "Currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private PriceCurrency currency;
}
