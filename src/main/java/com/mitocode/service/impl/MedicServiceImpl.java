package com.mitocode.service.impl;

import com.mitocode.model.Medic;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IMedicRepo;
import com.mitocode.service.IMedicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
@RequiredArgsConstructor
public class MedicServiceImpl extends CRUDImpl<Medic, Integer> implements IMedicService {


    private final IMedicRepo repo;

    @Override
    protected IGenericRepo<Medic, Integer> getConsultRepo() {
        return repo;
    }

    public static void main(String[] args) {

    }


}
