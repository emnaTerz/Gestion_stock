package com.emna.produit.Service.Impl;

import com.emna.produit.Entities.Attribute;
import com.emna.produit.Repository.AttributeRepository;
import com.emna.produit.Service.AttributeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {

    private  final AttributeRepository attributeRepository;

    public AttributeServiceImpl(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }


    @Override
    public void deleteattribute(Integer id) {
        Attribute att = attributeRepository.getById(id);
        attributeRepository.delete(att);
    }

    @Override
    public Attribute createattribute(Attribute attribute) {
       Attribute newatt = new Attribute(attribute.getName());


        return attributeRepository.save(newatt);
    }

    @Override
    public Attribute getattribute(Integer id) {
        return attributeRepository.getById(id);
    }

    @Override
    public Attribute updateattribute(Attribute attribute, Integer id) {
        Attribute existingatt = attributeRepository.getById(id);
        existingatt.setName(attribute.getName());
        return attributeRepository.save(existingatt);
    }

    @Override
    public List<Attribute> getattributes() {
        return attributeRepository.findAll();
    }
}
