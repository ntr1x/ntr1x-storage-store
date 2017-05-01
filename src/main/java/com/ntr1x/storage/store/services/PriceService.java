package com.ntr1x.storage.store.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.ForbiddenException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IResourceService;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;
import com.ntr1x.storage.security.services.IUserService;
import com.ntr1x.storage.store.model.Price;
import com.ntr1x.storage.store.repository.PriceRepository;

@Service
public class PriceService implements IPriceService {

    @Inject
    private EntityManager em;
    
    @Inject
    private IResourceService resources;
    
    @Inject
    private IUserService users;
    
    @Inject
    private PriceRepository prices;
    
    @Inject
    private ISecurityService security;
    
    @Override
    public Price create(long scope, PriceCreate create) {
        
        Price p = new Price(); {
            
            User user = create.user == null ? null : users.select(scope, create.user);
            Resource relate = create.relate == null ? null : resources.select(scope, create.relate);
            
            p.setScope(scope);
            p.setUser(user);
            p.setRelate(relate);
            p.setTitle(create.title);
            p.setPrice(create.price);
            p.setCurrency(create.currency);
            p.setExtra(create.extra);
            
            em.persist(p);
            em.flush();
            
            security.register(p, ResourceUtils.alias(null, "prices/i", p));
            security.grant(p.getScope(), user, p.getAlias(), "admin");
        }
        
        return p;
    }

    @Override
    public Price update(Long scope, long id, PriceUpdate update) {
        
        Price p = prices.select(scope, id); {
            
            p.setTitle(update.title);
            p.setPrice(update.price);
            p.setCurrency(update.currency);
            p.setExtra(update.extra);
            
            em.merge(p);
            em.flush();
        }
        
        return p;
    }

    @Override
    public Price select(Long scope, long id) {
        
        return prices.select(scope, id);
    }

    @Override
    public Page<Price> query(Long scope, Long user, Long relate, Pageable pageable) {
        
        return prices.query(scope, user, relate, pageable);
    }

    @Override
    public Price remove(Long scope, long id) {
        
        Price p = prices.select(scope, id); {
            
            em.remove(p);
            em.flush();
        }
        
        return p;
    }

    @Override
    public void createPrices(Resource resource, User user, RelatedPrice[] related) {
        
        if (related != null) {
            
            for (RelatedPrice t : related) {
                
                Price p = new Price(); {
                    
                    p.setScope(resource.getScope());
                    p.setUser(user);
                    p.setRelate(resource);
                    p.setTitle(t.title);
                    p.setPrice(t.price);
                    p.setCurrency(t.currency);
                    p.setExtra(t.extra);
                    
                    em.persist(p);
                    em.flush();
                    
                    security.register(p, ResourceUtils.alias(null, "prices/i", p));
                    security.grant(resource.getScope(), user, p.getAlias(), "admin");
                }
            }
        }
    }

    @Override
    public void updatePrices(Resource resource, User user, RelatedPrice[] related) {
        
        if (related != null) {
            
            for (RelatedPrice t : related) {
                
                switch (t.action) {
                
                    case CREATE: {
                        
                        Price p = new Price(); {
                            
                            p.setScope(resource.getScope());
                            p.setUser(user);
                            p.setRelate(resource);
                            p.setTitle(t.title);
                            p.setPrice(t.price);
                            p.setCurrency(t.currency);
                            p.setExtra(t.extra);
                            
                            em.persist(p);
                            em.flush();
                            
                            security.register(p, ResourceUtils.alias(null, "prices/i", p));
                            security.grant(resource.getScope(), user, p.getAlias(), "admin");
                            
                        }
                        break;
                    }
                    case UPDATE: {
                    
                        Price p = prices.select(resource.getScope(), t.id); {
                            
                            p.setTitle(t.title);
                            p.setPrice(t.price);
                            p.setCurrency(t.currency);
                            p.setExtra(t.extra);
                            
                            em.merge(p);
                            em.flush();
                        }
                        
                        break;
                    }
                    case REMOVE: {
                        
                        Price p = prices.select(resource.getScope(), t.id); {
                            
                            if (p.getRelate().getId() != resource.getId() || p.getRelate().getScope() != p.getScope()) {
                                throw new ForbiddenException("Price relates to another scope or resource");
                            }
                            em.remove(p);
                        }
                        break;
                    }
                default:
                    break;
                }
            }
            
            em.flush();
        }
    }

}
