package com.emna.produit.Service;

import com.emna.produit.Entities.Attribute;

import java.util.List;

public interface AttributeService {

    void deleteattribute (Integer id);
    Attribute createattribute (Attribute attribute);
    Attribute getattribute (Integer id);
    Attribute updateattribute (Attribute attribute , Integer id);
    List <Attribute> getattributes ();
}
