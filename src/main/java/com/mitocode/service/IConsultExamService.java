package com.mitocode.service;

import com.mitocode.model.ConsultExam;
import com.mitocode.model.Exam;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IConsultExamService {

    List<ConsultExam> getExamsByConsultId( Integer id);


}
