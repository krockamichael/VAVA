package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Mechanics;
import com.example.Autoservis.repository.MechanicsRepository;
import com.example.Autoservis.services.MechanicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class MechanicsServiceImpl implements MechanicsService {
    @Autowired
    private MechanicsRepository mechanicsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Mechanics findByNameAndSurname(String name, String surname) {
        return mechanicsRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public Mechanics save(Mechanics entity) {
        return mechanicsRepository.save(entity);
    }

    @Override
    public Mechanics update(Mechanics entity) {
        return mechanicsRepository.save(entity);
    }

    @Override
    public Mechanics findByMechanicId(int mechanic_id) {
        return mechanicsRepository.findByMechanicId(mechanic_id);
    }

    @Override
    public List<Mechanics> getMechanics(String name, String surname) {
        TypedQuery<Mechanics> query = entityManager.createQuery(
                "SELECT m FROM Mechanics m WHERE m.name like ?1 AND m.surname like ?2", Mechanics.class);
        query.setParameter(1, name+"%");
        query.setParameter(2, surname+"%");
        return query.getResultList();
    }
}
