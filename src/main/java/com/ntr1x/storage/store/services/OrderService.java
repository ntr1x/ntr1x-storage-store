package com.ntr1x.storage.store.services;

import java.time.LocalDateTime;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ntr1x.storage.core.model.Resource;
import com.ntr1x.storage.core.reflection.ResourceUtils;
import com.ntr1x.storage.core.services.IResourceService;
import com.ntr1x.storage.security.model.User;
import com.ntr1x.storage.security.services.ISecurityService;
import com.ntr1x.storage.security.services.IUserService;
import com.ntr1x.storage.store.model.Order;
import com.ntr1x.storage.store.repository.OrderRepository;

@Service
public class OrderService implements IOrderService {

	@Inject
	private EntityManager em;
	
	@Inject
	private ISecurityService security;
	
	@Inject
	private IUserService users;
	
	@Inject
	private IResourceService resources;
	
	@Inject
	private OrderRepository orders;
	
	@Override
	public Page<Order> query(
		Long scope,
		Long user,
		Long relate,
		Set<Order.State> states,
		LocalDateTime since,
		LocalDateTime until,
		Pageable pageable
	) {
		return states.size() > 0
			? orders.query(scope, user, relate, states, since, until, pageable)
			: orders.query(scope, user, relate, (Order.State) null, since, until, pageable)
		;
	}
	
	@Override
	public Order create(long scope, OrderCreate create) {
		
		Order p = new Order(); {
			
			User user = users.select(scope, create.user);
			Resource relate = resources.select(scope, create.relate);
			
			p.setScope(scope);
			p.setState(Order.State.NEW);
			p.setUser(user);
			p.setRelate(relate);
			p.setQuantity(create.quantity);
			p.setExtra(create.extra);
			
			em.persist(p);
			em.flush();
			
			security.register(p, ResourceUtils.alias(null, "orders/i", p));
			security.grant(p.getScope(), user, p.getAlias(), "client");
		}
		
		return p;
	}

	@Override
	public Order update(Long scope, long id, OrderUpdate update) {
		
		Order p = orders.select(scope, id); {
			
			p.setQuantity(update.quantity);
			p.setExtra(update.extra);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}
	
	@Override
	public Order state(Long scope, long id, Order.State state) {
		
		Order p = orders.select(scope, id); {
			
			if (!p.getState().arrows().contains(state)) {
				throw new IllegalStateException();
			}
			
			p.setState(state);
			
			em.merge(p);
			em.flush();
		}
		
		return p;
	}

	@Override
	public Order select(Long scope, long id) {
		
		return orders.select(scope, id);
	}

	@Override
	public Order remove(Long scope, long id) {
		
		Order p = orders.select(scope, id); {
			
			em.remove(p);
			em.flush();
		}
		
		return p;
	}

}
