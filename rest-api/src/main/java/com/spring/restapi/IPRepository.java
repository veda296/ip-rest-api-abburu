package com.spring.restapi;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

@Component
public class IPRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<IPAddress> getAll(){
		Query q = entityManager.createQuery("Select a from IPAddress a");
		return q.getResultList();
	}
	
	@Transactional()
	public int acquireIP(String i) {
		Query q = entityManager.createQuery("Update IPAddress a set a.available=false "
				+ "where a.ipAddress=:id and a.available=true");
        q.setParameter("id", i);  
		return q.executeUpdate();
	}
	
	@Transactional()
	public int releaseIP (String i) {
		Query q = entityManager.createQuery("Update IPAddress a set a.available=true "
				+ "where a.ipAddress=:i and a.available=false");
		q.setParameter("i", i);
		return q.executeUpdate();		
	}
	
	public Optional<IPAddress> getAddress (String a) {
		return Optional.ofNullable(entityManager.find(IPAddress.class, a));
	}
	
	@Transactional()
	public void saveAddress (IPAddress a) {
		entityManager.persist(a);
	}
	
	
	@Transactional
	public void saveAll (List<IPAddress> addresses) {
		for (IPAddress a : addresses) {
			if (!getAddress(a.getIpAddress()).isPresent()) {
				saveAddress(a);
			}
		}
	}
	
}
