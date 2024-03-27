package com.mitocode.controller;

import com.mitocode.dto.ExamDTO;
import com.mitocode.model.Exam;
import com.mitocode.service.IExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

    private final IExamService service;

    @Qualifier("defaultMapper")
    private final ModelMapper  mapper;


    @GetMapping
    public ResponseEntity<List<ExamDTO>> findAll() throws Exception {
        List<ExamDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id) throws Exception {
        ExamDTO examDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(examDTO);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ExamDTO examDTO) throws Exception {
        Exam exam = service.save(convertToEntity(examDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exam.getIdExam()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ExamDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody ExamDTO examDTO) throws Exception{
        examDTO.setIdExam(id);
        Exam exam = service.update(id, convertToEntity(examDTO));

        return ResponseEntity.ok(convertToDto(exam));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/hateoas/{id}")
    public EntityModel<ExamDTO> findByIdHateoas(@PathVariable("id") Integer id) throws Exception{
        EntityModel<ExamDTO> resource = EntityModel.of(convertToDto(service.findById(id)));

        // Generar un link informativo
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));

        resource.add(link1.withRel("exam-info"));

        return resource;


    }

    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private Exam convertToEntity (ExamDTO examDTO){
        return mapper.map(examDTO, Exam.class);
    }

    private ExamDTO convertToDto (Exam exam){
        return mapper.map(exam, ExamDTO.class);
    }


}
