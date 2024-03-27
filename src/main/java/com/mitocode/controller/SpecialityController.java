package com.mitocode.controller;

import com.mitocode.dto.SpecialityDTO;
import com.mitocode.model.Speciality;
import com.mitocode.service.ISpecialityService;
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
@RequestMapping("/specialities")
@RequiredArgsConstructor
public class SpecialityController {

    private final ISpecialityService service;

    @Qualifier("defaultMapper")
    private final ModelMapper  mapper;


    @GetMapping
    public ResponseEntity<List<SpecialityDTO>> findAll() throws Exception {
        List<SpecialityDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialityDTO> findById(@PathVariable("id") Integer id) throws Exception {
        SpecialityDTO specialityDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(specialityDTO);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody SpecialityDTO specialityDTO) throws Exception {
        Speciality speciality = service.save(convertToEntity(specialityDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(speciality.getIdSpeciality()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<SpecialityDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody SpecialityDTO specialityDTO) throws Exception{
        specialityDTO.setIdSpeciality(id);
        Speciality speciality = service.update(id, convertToEntity(specialityDTO));

        return ResponseEntity.ok(convertToDto(speciality));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/hateoas/{id}")
    public EntityModel<SpecialityDTO> findByIdHateoas(@PathVariable("id") Integer id) throws Exception{
        EntityModel<SpecialityDTO> resource = EntityModel.of(convertToDto(service.findById(id)));

        // Generar un link informativo
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));

        resource.add(link1.withRel("speciality-info"));

        return resource;


    }

    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private Speciality convertToEntity (SpecialityDTO specialityDTO){
        return mapper.map(specialityDTO, Speciality.class);
    }

    private SpecialityDTO convertToDto (Speciality speciality){
        return mapper.map(speciality, SpecialityDTO.class);
    }


}
