package com.ntr1x.storage.store.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntr1x.storage.store.model.Order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IOrderService {
	
	Order create(long scope, OrderCreate create);
	Order update(Long scope, long id, OrderUpdate update);
	Order state(Long scope, long id, Order.State state);
	
	Order select(Long scope, long id);
	
	Page<Order> query(Long scope, Long user, Long relate, Set<Order.State> states, LocalDateTime since, LocalDateTime until, Pageable pageable);
	
	Order remove(Long scope, long id);
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPageResponse {

    	public long count;
        public int page;
        public int size;

        @XmlElement
        public List<Order> content;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderCreate {
        
		public long relate;
		public long user;
		
    	public BigDecimal quantity;
    	
    	@ApiModelProperty(dataType = "Object")
    	public JsonNode extra;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderUpdate {
		
    	public long relate;
		public long user;
		
    	public BigDecimal quantity;
    	
    	@ApiModelProperty(dataType = "Object")
    	public JsonNode extra;
    }
}
