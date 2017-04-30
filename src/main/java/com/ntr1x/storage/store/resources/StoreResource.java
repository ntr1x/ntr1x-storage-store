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

import com.ntr1x.storage.archery.model.Store;
import com.ntr1x.storage.archery.services.IStoreService;
import com.ntr1x.storage.archery.services.IStoreService.StoreCreate;
import com.ntr1x.storage.archery.services.IStoreService.StorePageResponse;
import com.ntr1x.storage.archery.services.IStoreService.StoreUpdate;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;

import io.swagger.annotations.Api;

@Api("Store")
@Component
@Path("/store/stores")
@PermitAll
public class StoreResource {
    
    @Inject
    private IStoreService stores;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public StorePageResponse shared(
        @QueryParam("portal") Long portal,
        @QueryParam("user") Long user,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Store> p = stores.query(
            scope.get().getId(),
            user,
            portal,
            pageable.toPageRequest()
        );
        
        return new StorePageResponse(
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
    @RolesAllowed({ "res:///stores/:admin" })
    public Store create(@Valid StoreCreate create) {

        return stores.create(scope.get().getId(), create);
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///stores/i/{id}/:admin" })
    public Store update(@PathParam("id") long id, @Valid StoreUpdate update) {
        
        return stores.update(scope.get().getId(), id, update);
    }
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///stores/i/{id}/:admin" })
    public Store select(@PathParam("id") long id) {
        
        return stores.select(scope.get().getId(), id);
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///stores/i/{id}/:admin" })
    public Store remove(@PathParam("id") long id) {
        
        return stores.remove(scope.get().getId(), id);
    }
}
