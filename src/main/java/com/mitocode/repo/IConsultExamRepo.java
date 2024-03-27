package com.mitocode.repo;

import com.mitocode.model.ConsultExam;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface IConsultExamRepo extends IGenericRepo<ConsultExam, Integer>{

    @Modifying
    @Query(value = "INSERT INTO consult_exam(id_consult, id_exam)VALUES(:idConsult, :idExam)", nativeQuery = true)
    Integer saveExam(@Param("idConsult") Integer idConsult, @Param("idExam") Integer idExam);

    @Query("SELECT new ConsultExam(ce.exam) FROM ConsultExam  ce where ce.consult.idConsult= :idConsult")
    List<ConsultExam> getExamsByConsultId(@Param("idConsult") Integer id);



}
