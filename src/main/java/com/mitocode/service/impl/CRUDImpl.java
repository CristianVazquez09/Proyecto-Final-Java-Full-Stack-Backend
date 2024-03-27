package com.mitocode.service.impl;

import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.model.Menu;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.ICRUD;

import java.util.List;

public abstract class CRUDImpl <T, ID> implements ICRUD<T,ID> {


    protected abstract IGenericRepo<T, ID> getConsultRepo();
    @Override
    public T save(T t) throws Exception {
        return getConsultRepo().save(t);
    }

    @Override
    public T update(ID id, T t) throws Exception {
        getConsultRepo().findById(id).orElseThrow( () -> new ModelNotFoundException("ID NOT FOUND "+id));
        return getConsultRepo().save(t);
    }

    @Override
    public List<T> findAll() throws Exception {
        return getConsultRepo().findAll();
    }

    @Override
    public T findById(ID id) throws Exception {

        // Falta ver la excepción
        return getConsultRepo().findById(id).orElseThrow( () -> new ModelNotFoundException("ID NOT FOUND "+id));
    }

    @Override
    public void delete(ID id) throws Exception {
        getConsultRepo().findById(id).orElseThrow( () -> new ModelNotFoundException("ID NOT FOUND "+id));
        getConsultRepo().deleteById(id);
    }
}
