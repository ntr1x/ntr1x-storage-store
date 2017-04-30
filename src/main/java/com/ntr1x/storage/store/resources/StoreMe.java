package com.ntr1x.storage.store.resources;

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

import com.ntr1x.storage.archery.model.Store;
import com.ntr1x.storage.archery.services.IStoreService;
import com.ntr1x.storage.archery.services.IStoreService.StoreCreate;
import com.ntr1x.storage.archery.services.IStoreService.StorePageResponse;
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.security.filters.IUserPrincipal;

import io.swagger.annotations.Api;

@Api("Me")
@Component
@Path("/me/store/stores")
@PermitAll
public class StoreMe {

    @Inject
    private IStoreService stores;

    @Inject
    private Provider<IUserPrincipal> principal;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public StorePageResponse query(
        @QueryParam("portal") Long portal,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Store> p = stores.query(
            scope.get().getId(),
            principal.get().getUser().getId(),
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
    @RolesAllowed({ "auth" })
    public Store create(@Valid StoreCreate create) {

        return stores.create(scope.get().getId(), create);
    }
}
