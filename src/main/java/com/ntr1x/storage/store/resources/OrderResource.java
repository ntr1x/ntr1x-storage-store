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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.store.model.Order;
import com.ntr1x.storage.store.services.IOrderService;
import com.ntr1x.storage.store.services.IOrderService.OrderCreate;
import com.ntr1x.storage.store.services.IOrderService.OrderPageResponse;
import com.ntr1x.storage.store.services.IOrderService.OrderUpdate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Api("Store")
@Component
@Path("/store/orders")
@PermitAll
public class OrderResource {

	@Inject
	private IOrderService orders;
	
	@Inject
	private Provider<IUserScope> scope;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///orders:admin" })
    public OrderPageResponse query(
		@QueryParam("user") Long user,
		@QueryParam("relate") Long relate,
		@QueryParam("states") Set<Order.State> states,
		@QueryParam("since") @ApiParam(example = "2016-10-01T17:30") LocalDateTime since,
		@QueryParam("until") @ApiParam(example = "2016-10-01T21:00") LocalDateTime until,
		@BeanParam PageableQuery pageable
    ) {
    	
        Page<Order> p = orders.query(
    		scope.get().getId(),
			user,
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
    @RolesAllowed({ "res:///orders:admin" })
    public Order create(@Valid OrderCreate create) {

        return orders.create(scope.get().getId(), create);
	}
	
	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({
		"res:///orders/i/{id}:admin",
		"res:///orders/i/{id}:client"
	})
	public Order update(@PathParam("id") long id, @Valid OrderUpdate update) {
		
	    return orders.update(scope.get().getId(), id, update);
	}
	
	@PUT
	@Path("/i/{id}/accept")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///orders/i/{id}:admin" })
	public Order accept(@PathParam("id") long id) {
		
	    return orders.state(scope.get().getId(), id, Order.State.ACCEPTED);
	}
	
	@PUT
	@Path("/i/{id}/decline")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///orders/i/{id}:admin" })
	public Order decline(@PathParam("id") long id) {
		
	    return orders.state(scope.get().getId(), id, Order.State.DECLINED);
	}
	
	@PUT
	@Path("/i/{id}/cancel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///orders/i/{id}:client" })
	public Order cancel(@PathParam("id") long id) {
		
	    return orders.state(scope.get().getId(), id, Order.State.CANCELLED);
	}
	
	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({
    	"res:///orders/i/{id}:admin",
    	"res:///orders/i/{id}:client"
	})
    public Order select(@PathParam("id") long id) {
        
        return orders.select(scope.get().getId(), id);
    }
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///orders/i/{id}:admin" })
    public Order remove(@PathParam("id") long id) {
        
	    return orders.remove(scope.get().getId(), id);
    }
}
