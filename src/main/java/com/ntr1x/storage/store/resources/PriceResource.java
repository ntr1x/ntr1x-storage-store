package com.ntr1x.storage.store.resources;

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
import com.ntr1x.storage.store.model.Price;
import com.ntr1x.storage.store.services.IPriceService;
import com.ntr1x.storage.store.services.IPriceService.PriceCreate;
import com.ntr1x.storage.store.services.IPriceService.PricePageResponse;
import com.ntr1x.storage.store.services.IPriceService.PriceUpdate;

import io.swagger.annotations.Api;

@Api("Store")
@Component
@Path("/store/prices")
@PermitAll
public class PriceResource {

    @Inject
    private IPriceService prices;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PricePageResponse query(
        @QueryParam("user") Long user,
        @QueryParam("relate") Long relate,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Price> p = prices.query(
            scope.get().getId(),
            user,
            relate,
            pageable.toPageRequest()
        );
        
        return new PricePageResponse(
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
    @RolesAllowed({ "res:///prices/:admin" })
    public Price create(@Valid PriceCreate create) {

        return prices.create(scope.get().getId(), create);
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///prices/i/{id}/:admin" })
    public Price update(@PathParam("id") long id, @Valid PriceUpdate update) {
        
        return prices.update(scope.get().getId(), id, update);
    }
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Price select(@PathParam("id") long id) {
        
        return prices.select(scope.get().getId(), id);
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///prices/i/{id}/:admin" })
    public Price remove(@PathParam("id") long id) {
        
        return prices.remove(scope.get().getId(), id);
    }
}
