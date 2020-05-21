package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Components;
import com.example.Autoservis.repository.ComponentsRepository;
import com.example.Autoservis.services.ComponentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class ComponentsServiceImpl implements ComponentsService {
    @Autowired
    private ComponentsRepository componentsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Components findByComponentId(long component_id) {
        return componentsRepository.findByComponentId(component_id);
    }

    @Override
    public List<Components> getComponents(String name, String car_type) {
        TypedQuery<Components> query = entityManager.createQuery("SELECT c FROM Components c WHERE c.name like ?1 AND c.carType like ?2", Components.class);
        query.setParameter(1, name+"%");
        query.setParameter(2, car_type+"%");
        return query.getResultList();
    }

    @Override
    public Components save(Components entity) {
        return componentsRepository.save(entity);
    }

    @Override
    public Components update(Components entity) {
        return componentsRepository.save(entity);
    }
}
