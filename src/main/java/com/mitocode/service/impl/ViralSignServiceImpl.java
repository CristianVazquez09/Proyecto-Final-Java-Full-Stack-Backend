package com.mitocode.service.impl;

import com.mitocode.model.VitalSign;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IVitalSignRepo;
import com.mitocode.service.IVitalSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
@RequiredArgsConstructor
public class ViralSignServiceImpl extends CRUDImpl<VitalSign, Integer> implements IVitalSignService {


    private final IVitalSignRepo repo;

    @Override
    protected IGenericRepo<VitalSign, Integer> getConsultRepo() {
        return repo;
    }

    @Override
    public Page<VitalSign> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
