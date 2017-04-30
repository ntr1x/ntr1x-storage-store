package com.ntr1x.storage.store.services;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.core.model.Action;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.store.model.Price;
import com.ntr1x.storage.store.model.Price.PriceCurrency;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPriceService {
    
    Price create(long scope, PriceCreate create);
    Price update(Long scope, long id, PriceUpdate update);
    
    Price select(Long scope, long id);
    
    Page<Price> query(Long scope, Long user, Long relate, Pageable pageable);
    
    Price remove(Long scope, long id);
    
    void createPrices(Resource resource, User user, RelatedPrice[] prices);
    void updatePrices(Resource resource, User user, RelatedPrice[] prices);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PricePageResponse {

        public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Price> content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedPrice {
        
        public Long id;
        
        public Long user;
        
        @NotBlank
        public String title;
        
        @NotBlank
        public BigDecimal price;
        
        @NotBlank
        public PriceCurrency currency;
        
        @ApiModelProperty(dataType = "Object")
        public JsonNode extra;
        
        public Action action;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceCreate {
        
        public Long relate;
        
        public Long user;
        
        @NotBlank
        public String title;
        
        @NotBlank
        public BigDecimal price;
        
        @NotBlank
        public PriceCurrency currency;
        
        @ApiModelProperty(dataType = "Object")
        public JsonNode extra;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceUpdate {
        
        @NotBlank
        public String title;
        
        @NotBlank
        public BigDecimal price;
        
        @NotBlank
        public PriceCurrency currency;
        
        @ApiModelProperty(dataType = "Object")
        public JsonNode extra;
    }
}
