package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Cars;
import com.example.Autoservis.bean.Repairs;
import com.example.Autoservis.repository.RepairsRepository;
import com.example.Autoservis.services.RepairsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.ResultSet;
import java.util.List;

@Service
public class RepairsServiceImpl implements RepairsService {

    @Autowired
    EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RepairsRepository repairsRepository;

    @Override
    public Repairs save(Repairs entity) {
        return repairsRepository.save(entity);
    }

    @Override
    public Repairs update(Repairs entity) {
        return repairsRepository.save(entity);
    }

    @Override
    public void delete(Repairs entity) {
        repairsRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        repairsRepository.deleteById(id);
    }

    @Override
    public List<Repairs> findAll() {
        return (List<Repairs>) repairsRepository.findAll();
    }

    @Override
    public Repairs findByMechanicId(int mechanic_id) {
        return repairsRepository.findByMechanicId(mechanic_id);
    }

    @Override
    public String total(int mechanic_id) {
        return repairsRepository.total(mechanic_id);
    }

    @Override
    public String avgDate(int mechanic_id) {
        return repairsRepository.avgDate(mechanic_id);
    }

    @Override
    public Repairs findByCarId(int car_id) {
        return repairsRepository.findByCarId(car_id);
    }

    @Override
    public String getNumberOfR(int mechanic_id) {
        return repairsRepository.getNumberOfR(mechanic_id);
    }

    @Override
    public String getCostSum(int car_id) {
        return repairsRepository.getCostSum(car_id);
    }

    @Override
    public List<Repairs> getWorkDetails(int mechnic_id) {
        TypedQuery<Repairs> query = entityManager.createQuery("SELECT R FROM Repairs R JOIN Mechanics M on M.mechanicId = R.mechanicId WHERE M.mechanicId = ?1 ORDER BY R.start_day", Repairs.class);
        query.setParameter(1, mechnic_id);
        return query.getResultList();
    }

    @Override
    public List<Repairs> allRepairs(int car_id) {
        TypedQuery<Repairs> query = entityManager.createQuery(
                "SELECT r FROM Repairs r WHERE r.carId = ?1", Repairs.class);
        query.setParameter(1, car_id);
        return query.getResultList();
    }
}
