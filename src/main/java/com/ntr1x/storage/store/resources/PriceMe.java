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

import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.security.filters.IUserPrincipal;
import com.ntr1x.storage.store.model.Price;
import com.ntr1x.storage.store.services.IPriceService;
import com.ntr1x.storage.store.services.IPriceService.PriceCreate;
import com.ntr1x.storage.store.services.IPriceService.PricePageResponse;

import io.swagger.annotations.Api;

@Api("Me")
@Component
@Path("/me/store/prices")
@PermitAll
public class PriceMe {

    @Inject
    private IPriceService prices;

    @Inject
    private Provider<IUserPrincipal> principal;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "auth" })
    public PricePageResponse query(
        @QueryParam("relate") Long relate,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Price> p = prices.query(
            scope.get().getId(),
            principal.get().getUser().getId(),
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
    @RolesAllowed({ "auth" })
    public Price create(@Valid PriceCreate create) {

        create.user = principal.get().getUser().getId();
        
        return prices.create(scope.get().getId(), create);
    }
}
