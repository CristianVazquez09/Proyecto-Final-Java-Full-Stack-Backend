package com.mitocode.controller;

import com.mitocode.dto.PatientDTO;
import com.mitocode.dto.VitalSignDTO;
import com.mitocode.model.VitalSign;
import com.mitocode.service.IVitalSignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/vitalSigns")
@RequiredArgsConstructor
public class VitalSignController {

    private final IVitalSignService service;

    @Qualifier("defaultMapper")
    private final ModelMapper  mapper;


    @GetMapping
    public ResponseEntity<List<VitalSignDTO>> findAll() throws Exception {
        List<VitalSignDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalSignDTO> findById(@PathVariable("id") Integer id) throws Exception {
        VitalSignDTO vitalSignsDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(vitalSignsDTO);
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody VitalSignDTO vitalSignsDTO) throws Exception {
        VitalSign vitalSigns = service.save(convertToEntity(vitalSignsDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(vitalSigns.getIdVitalSign()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VitalSignDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody VitalSignDTO vitalSignsDTO) throws Exception{
        vitalSignsDTO.setIdVitalSign(id);
        VitalSign vitalSigns = service.update(id, convertToEntity(vitalSignsDTO));

        return ResponseEntity.ok(convertToDto(vitalSigns));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<VitalSignDTO>> listPage(Pageable pageable) throws  Exception{

        Page<VitalSignDTO> list= service.listPage(pageable).map(this::convertToDto);

        return ResponseEntity.ok(list);
    }
    
    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private VitalSign convertToEntity (VitalSignDTO vitalSignsDTO){
        return mapper.map(vitalSignsDTO, VitalSign.class);
    }

    private VitalSignDTO convertToDto (VitalSign vitalSigns){
        return mapper.map(vitalSigns, VitalSignDTO.class);
    }


}
