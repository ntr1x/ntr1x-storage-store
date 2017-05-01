package com.ntr1x.storage.store.resources;

import java.util.stream.Collectors;

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
import com.ntr1x.storage.core.filters.IUserScope;
import com.ntr1x.storage.core.services.IParamService;
import com.ntr1x.storage.core.transport.PageableQuery;
import com.ntr1x.storage.store.model.Offer;
import com.ntr1x.storage.store.services.IOfferService;
import com.ntr1x.storage.store.services.IOfferService.OfferCreate;
import com.ntr1x.storage.store.services.IOfferService.OfferPageResponse;
import com.ntr1x.storage.store.services.IOfferService.OfferUpdate;
import com.ntr1x.storage.store.services.IPriceService;

import io.swagger.annotations.Api;

@Api("Store")
@Component
@Path("/store/offers")
@PermitAll
public class OfferResource {

    @Inject
    private IOfferService offers;
    
    @Inject
    private IStoreService stores;
    
    @Inject
    private IPriceService prices;
    
    @Inject
    private IParamService params;
    
    @Inject
    private Provider<IUserScope> scope;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OfferPageResponse query(
        @QueryParam("user") Long user,
        @QueryParam("relate") Long relate,
        @BeanParam PageableQuery pageable
    ) {
        
        Page<Offer> p = offers.query(
            scope.get().getId(),
            user,
            relate,
            pageable.toPageRequest()
        );
        
        return new OfferPageResponse(
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
    @RolesAllowed({ "res:///offers/:admin" })
    public Offer create(@Valid OfferCreate create) {
        
        return offers.create(scope.get().getId(), create);
    }
    
    @PUT
    @Path("/i/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///offers/i/{id}/:admin" })
    public Offer update(@PathParam("id") long id, @Valid OfferUpdate update) {
        
        return offers.update(scope.get().getId(), id, update);
    }
    
    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Offer select(@PathParam("id") long id) {
        
        return offers.select(scope.get().getId(), id);
    }
    
    @GET
    @Path("/i/{id}/context")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public IOfferService.OfferContext context(@PathParam("id") long id) {
        
        Offer offer = offers.select(scope.get().getId(), id);
        
        return new IOfferService.OfferContext(
            offer,
            prices.query(scope.get().getId(), null, offer.getId(), null).getContent(),
            stores.query(scope.get().getId(), null, null, null).getContent().stream()
                .map(
                    (s) -> new IStoreService.StoreContext(
                        s,
                        params.map(scope.get().getId(), s.getId(), Store.ParamType.PUBLIC.name())
                    )
                )
                .collect(Collectors.toList())
        );
    }
    
    @DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///offers/i/{id}/:admin" })
    public Offer remove(@PathParam("id") long id) {
        
        return offers.remove(scope.get().getId(), id);
    }
}
