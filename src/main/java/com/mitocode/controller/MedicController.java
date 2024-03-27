package com.mitocode.controller;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.IMedicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/medics")
@RequiredArgsConstructor
public class MedicController {

    private final IMedicService service;

    @Qualifier("medicMapper")
    private final ModelMapper  mapper;


    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    //@PreAuthorize("@authorizeLogic.hasAccess('findAll')")
    @GetMapping
    public ResponseEntity<List<MedicDTO>> findAll() throws Exception {
        List<MedicDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id) throws Exception {
        MedicDTO medicDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(medicDTO);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody MedicDTO medicDTO) throws Exception {
        Medic medic = service.save(convertToEntity(medicDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(medic.getIdMedic()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<MedicDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody MedicDTO medicDTO) throws Exception{
        medicDTO.setIdMedic(id);
        Medic medic = service.update(id, convertToEntity(medicDTO));

        return ResponseEntity.ok(convertToDto(medic));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/hateoas/{id}")
    public EntityModel<MedicDTO> findByIdHateoas(@PathVariable("id") Integer id) throws Exception{
        EntityModel<MedicDTO> resource = EntityModel.of(convertToDto(service.findById(id)));

        // Generar un link informativo
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));

        resource.add(link1.withRel("medic-info"));

        return resource;


    }

    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private Medic convertToEntity (MedicDTO medicDTO){
        return mapper.map(medicDTO, Medic.class);
    }

    private MedicDTO convertToDto (Medic medic){
        return mapper.map(medic, MedicDTO.class);
    }


}
