package com.mitocode.service.impl;

import com.mitocode.model.Exam;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IExamRepo;
import com.mitocode.service.IExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {


    private final IExamRepo repo;

    @Override
    protected IGenericRepo<Exam, Integer> getConsultRepo() {
        return repo;
    }

    public static void main(String[] args) {

    }


}
