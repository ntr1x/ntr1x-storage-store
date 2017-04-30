package com.ntr1x.storage.store.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.core.model.Image;
import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IResourceService;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;
import com.ntr1x.storage.security.services.IUserService;
import com.ntr1x.storage.store.model.Offer;
import com.ntr1x.storage.store.repository.OfferRepository;
import com.ntr1x.storage.uploads.services.IImageService;

@Service
public class OfferService implements IOfferService {

    @Inject
    private EntityManager em;
    
    @Inject
    private OfferRepository offers;
    
    @Inject
    private IResourceService resources;
    
    @Inject
    private IUserService users;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IImageService images;
    
    @Inject
    private IPriceService prices;
    
    @Override
    public Offer create(long scope, OfferCreate create) {
        
        Offer p = new Offer(); {
            
            User user = create.user == null ? null : users.select(scope, create.user);
            Resource relate = create.relate == null ? null : resources.select(scope, create.relate);
            Image image = create.image == null ? null : images.select(scope, create.image);
            
            p.setScope(scope);
            p.setUser(user);
            p.setImage(image);
            p.setRelate(relate);
            p.setTitle(create.title);
            p.setPromo(create.promo);
            p.setBody(create.body);
            p.setExtra(create.extra);
            
            em.persist(p);
            em.flush();
            
            security.register(p, ResourceUtils.alias(null, "offers/i", p));
            security.grant(p.getScope(), user, p.getAlias(), "admin");
            
            images.createImages(p, create.images);
            prices.createPrices(p, user, create.prices);
        }
        
        return p;
    }

    @Override
    public Offer update(Long scope, long id, OfferUpdate update) {
        
        Offer p = offers.select(scope, id); {
            
            Image image = update.image == null ? null : images.select(scope, update.image);
            
            p.setImage(image);
            p.setTitle(update.title);
            p.setPromo(update.promo);
            p.setBody(update.body);
            p.setExtra(update.extra);
            
            em.merge(p);
            em.flush();
            
            images.updateImages(p, update.images);
            prices.updatePrices(p, p.getUser(), update.prices);
        }
        
        return p;
    }

    @Override
    public Offer select(Long scope, long id) {
        
        return offers.select(scope, id);
    }

    @Override
    public Page<Offer> query(Long scope, Long user, Long relate, Pageable pageable) {
        
        return offers.query(scope, user, relate, pageable);
    }

    @Override
    public Offer remove(Long scope, long id) {
        
        Offer p = offers.select(scope, id); {
            
            em.remove(p);
            em.flush();
        }
        
        return p;
    }
}
