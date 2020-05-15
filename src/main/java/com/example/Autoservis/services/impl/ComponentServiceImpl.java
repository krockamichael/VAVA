package com.example.Autoservis.services.impl;

import com.example.Autoservis.bean.Component;
import com.example.Autoservis.repository.ComponentRepository;
import com.example.Autoservis.repository.ComponentsRepository;
import com.example.Autoservis.services.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {
    @Autowired
    private ComponentRepository componentRepository;

    @Override
    public Component save(Component entity) {
        return componentRepository.save(entity);
    }

    @Override
    public Component update(Component entity) {
        return componentRepository.save(entity);
    }

    @Override
    public void delete(Component entity) {
        componentRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        componentRepository.deleteById(id);
    }

    @Override
    public List<Component> findAll() {
        return componentRepository.findAll();
    }
}
