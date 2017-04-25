package com.ntr1x.storage.store.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ntr1x.storage.core.model.Resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @XmlElement
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "RelateId", nullable = true, updatable = false)
    private Resource relate;
    
    @Column(name = "Title", nullable = false)
    private String title;
    
    @Column(name = "Price", nullable = true)
    private BigDecimal price;
}
