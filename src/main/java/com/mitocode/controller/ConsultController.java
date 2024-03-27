package com.mitocode.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mitocode.dto.*;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.model.MediaFile;
import com.mitocode.service.IConsultService;
import com.mitocode.service.IMediaFileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

    private final IConsultService service;

    @Qualifier("consultMapper")
    private final ModelMapper  mapper;
    private final IMediaFileService mfService;
    private final Cloudinary cloudinary;


    @GetMapping
    public ResponseEntity<List<ConsultDTO>> findAll() throws Exception {
        List<ConsultDTO> list = service.findAll().stream()
                .map(this::convertToDto).toList();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id) throws Exception {
        ConsultDTO consultDTO = convertToDto(service.findById(id));
        return ResponseEntity.ok(consultDTO);
    }

    /*@PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ConsultDTO consultDTO) throws Exception {
        Consult consult = service.save(convertToEntity(consultDTO));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(consult.getIdConsult()).toUri();

        return ResponseEntity.created(location).build();
        //return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }*/

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ConsultListExamDTO dto) throws Exception {

        Consult cons = convertToEntity(dto.getConsult());
        //List<Exam> exams = dto.getLstExam().stream().map(e -> mapper.map(e, Exam.class)).toList();
        List<Exam> exams= mapper.map(dto.getLstExam(), new TypeToken<List<Exam>>(){}.getType());

        Consult obj= service.saveTransactional(cons, exams);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("{id}")
    public ResponseEntity<ConsultDTO> update (@Valid @PathVariable("id") Integer id, @RequestBody ConsultDTO consultDTO) throws Exception{
        consultDTO.setIdConsult(id);
        Consult consult = service.update(id, convertToEntity(consultDTO));

        return ResponseEntity.ok(convertToDto(consult));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id ) throws Exception {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/hateoas/{id}")
    public EntityModel<ConsultDTO> findByIdHateoas(@PathVariable("id") Integer id) throws Exception{
        EntityModel<ConsultDTO> resource = EntityModel.of(convertToDto(service.findById(id)));

        // Generar un link informativo
        WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));

        resource.add(link1.withRel("consult-info"));

        return resource;
    }
    @PostMapping("/search/others")
    public ResponseEntity<List<ConsultDTO>> searchByOthers(@RequestBody FilterConsultDTO filterDTO){
        List<Consult> consults = service.search(filterDTO.getDni(), filterDTO.getFullname());
        //List<ConsultDTO> consultDTOS = consults.stream().map(this::convertToDto).toList();
        List<ConsultDTO> consultDTOS = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return ResponseEntity.ok(consultDTOS);
    }

    @GetMapping("/search/dates")
    public ResponseEntity<List<ConsultDTO>> searchByDates(
            @RequestParam(value = "date1", defaultValue = "2024-01-01", required = true) String date1,
            @RequestParam(value = "date2", defaultValue = "2024-02-01", required = true) String date2
    ){
        List<Consult> consults = service.searchByDates(LocalDateTime.parse(date1), LocalDateTime.parse(date2));
        List<ConsultDTO> consultDTOS = mapper.map(consults, new TypeToken<List<ConsultDTO>>(){}.getType());

        return ResponseEntity.ok(consultDTOS);
    }

    @GetMapping("/callProcedureNative")
    public ResponseEntity<List<ConsultProcDTO>> callProcedureOrFunctionNative() throws Exception{

        return new ResponseEntity<>(service.callProcedureOrFunctionNative(), HttpStatus.OK);
    }

    @GetMapping("/callProcedureProjection")
    public ResponseEntity<List<IConsultProcDTO>> callProcedureOrFunctionProjection() throws Exception{

        return new ResponseEntity<>(service.callProcedureOrFunctionProjection(), HttpStatus.OK);
    }

    @GetMapping(value = "/generateReport", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE) //APPLICATION_PDF_VALUE
    public ResponseEntity<byte[]> generateReport() throws Exception{
        byte[] data = service.generateReport();

        return ResponseEntity.ok(data);
    }

    @PostMapping(value = "/saveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> saveFile(@RequestParam("file")MultipartFile file) throws Exception{
        //DB
        MediaFile mf = new MediaFile();
        mf.setFilename(file.getOriginalFilename());
        mf.setFileType(file.getContentType());
        mf.setValue(file.getBytes());
        mfService.save(mf);

        //Repo externo
        /*File f = convertToFile(file);
        Map response = cloudinary.uploader().upload(f, ObjectUtils.asMap("resource_type", "auto"));
        JSONObject json = new JSONObject(response);
        String url = json.getString("url");

        System.out.println(url);*/


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/readFile/{idFile}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> readFile(@PathVariable("idFile") Integer idFile) throws Exception{

        byte[] array= mfService.findById(idFile).getValue();

        return new ResponseEntity<>(array, HttpStatus.OK);
    }



    // -- * -- * -- * -- *-- * -- * -- * -- UTILITARIOS -- * -- * -- * ---- * -- * -- * --

    private Consult convertToEntity (ConsultDTO consultDTO){
        return mapper.map(consultDTO, Consult.class);
    }

    private ConsultDTO convertToDto (Consult consult){
        return mapper.map(consult, ConsultDTO.class);
    }

    private File convertToFile (MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(multipartFile.getBytes());
        outputStream.close();
        return  file;
    }


}
