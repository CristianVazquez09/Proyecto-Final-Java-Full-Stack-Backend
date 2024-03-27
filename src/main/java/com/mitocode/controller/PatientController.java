package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.model.Patient;
import com.mitocode.service.IPatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
//@RequestMapping("/patients")
@RequestMapping("${patient.controller.path}")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class PatientController {

    private final IPatientService service;

    @Qualifier("defaultMapper")
    private final ModelMapper  mapper;


    @GetMapping
    public ResponseEntity<List<PatientDTO>> findAll() throws Exception {
        List<PatientDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id) throws Exception {
        PatientDTO patientDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(patientDTO);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody PatientDTO patientDTO) throws Exception {
        Patient patient = service.save(convertToEntity(patientDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(patient.getIdPatient()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<PatientDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody PatientDTO patientDTO) throws Exception{
        patientDTO.setIdPatient(id);
        Patient patient = service.update(id, convertToEntity(patientDTO));

        return ResponseEntity.ok(convertToDto(patient));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/hateoas/{id}")
    public EntityModel<PatientDTO> findByIdHateoas(@PathVariable("id") Integer id) throws Exception{
        EntityModel<PatientDTO> resource = EntityModel.of(convertToDto(service.findById(id)));

        // Generar un link informativo
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));

        resource.add(link1.withRel("patient-info"));

        return resource;
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<PatientDTO>> listPage(Pageable pageable) throws  Exception{
        Page<PatientDTO> list= service.listPage(pageable).map(this::convertToDto);

        return ResponseEntity.ok(list);
    }
    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private Patient convertToEntity (PatientDTO patientDTO){
        return mapper.map(patientDTO, Patient.class);
    }

    private PatientDTO convertToDto (Patient patient){
        return mapper.map(patient, PatientDTO.class);
    }


}
