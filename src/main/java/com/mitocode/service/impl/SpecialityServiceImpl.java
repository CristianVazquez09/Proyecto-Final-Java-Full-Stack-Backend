package com.mitocode.service.impl;

import com.mitocode.model.Speciality;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISpecialityRepo;
import com.mitocode.service.ISpecialityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
@RequiredArgsConstructor
public class SpecialityServiceImpl extends CRUDImpl<Speciality, Integer> implements ISpecialityService {


    private final ISpecialityRepo repo;

    @Override
    protected IGenericRepo<Speciality, Integer> getConsultRepo() {
        return repo;
    }

    public static void main(String[] args) {

    }


}
