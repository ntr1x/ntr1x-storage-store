package com.ntr1x.storage.store.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.store.model.Offer;
import com.ntr1x.storage.uploads.services.IImageService.RelatedImage;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IOfferService {
    
    Offer create(long scope, OfferCreate create);
    Offer update(Long scope, long id, OfferUpdate update);
    
    Offer select(Long scope, long id);
    
    Page<Offer> query(Long scope, Long user, Long relate, Pageable pageable);
    
    Offer remove(Long scope, long id);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferPageResponse {

        public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Offer> content;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferCreate {
        
        public Long relate;
        public Long user;
        public Long image;
        
        @NotBlank
        public String title;
        public String promo;
        public String body;
        
        @ApiModelProperty(dataType = "Object")
        public JsonNode extra;
        
        public RelatedImage[] images;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferUpdate {
        
        public Long image;
        
        @NotBlank
        public String title;
        public String promo;
        public String body;
        
        @ApiModelProperty(dataType = "Object")
        public JsonNode extra;
        
        public RelatedImage[] images;
    }
}
