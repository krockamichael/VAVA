package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.repository.CarsRepository;
import com.example.Autoservis.services.CarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class CarsServiceImpl implements CarsService {

    @Autowired
    private CarsRepository carsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Cars> getCarsMaybe(String model, String brand, String vin) {
        TypedQuery<Cars> query = entityManager.createQuery(
                "SELECT c FROM Cars c WHERE c.model like ?1 AND c.brand like ?2 AND c.vin like ?3", Cars.class);
        query.setParameter(1, model+"%");
        query.setParameter(2, brand+"%");
        query.setParameter(3, vin+"%");
        return query.getResultList();
    }

    @Override
    public Cars save(Cars entity) {
        return carsRepository.save(entity);
    }

    @Override
    public Cars update(Cars entity) {
        return carsRepository.save(entity);
    }
}
