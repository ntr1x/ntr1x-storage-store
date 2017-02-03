package com.ntr1x.storage.store.resources;

import java.time.LocalDateTime;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.security.filters.IUserPrincipal;
import com.ntr1x.storage.store.model.Order;
import com.ntr1x.storage.store.services.IOrderService;
import com.ntr1x.storage.store.services.IOrderService.OrderCreate;
import com.ntr1x.storage.store.services.IOrderService.OrderPageResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Me")
@Component
@Path("/me/store/orders")
@PermitAll
public class OrderMe {

	@Inject
    private IOrderService orders;

    @Inject
    private Provider<IUserPrincipal> principal;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public OrderPageResponse query(
		@QueryParam("relate") Long relate,
		@QueryParam("states") Set<Order.State> states,
		@QueryParam("since") @ApiParam(example = "2016-10-01T17:30") LocalDateTime since,
		@QueryParam("until") @ApiParam(example = "2016-10-01T21:00") LocalDateTime until,
		@BeanParam PageableQuery pageable
    ) {
    	
    	Page<Order> p = orders.query(
    		scope.get().getId(),
			principal.get().getUser().getId(),
			relate,
			states,
			since,
			until,
			pageable.toPageRequest()
		);
        
        return new OrderPageResponse(
    		p.getTotalElements(),
    		p.getNumber(),
    		p.getSize(),
    		p.getContent()
		);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public Order create(@Valid OrderCreate create) {

    	create.user = principal.get().getUser().getId();
    	
        return orders.create(scope.get().getId(), create);
	}
}
