package com.isacore.quality.api;

import com.isacore.quality.model.HccdResultadoDto;
import com.isacore.quality.service.IHccHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/hccs")
public class HccController {

    @Autowired
    private IHccHeadService service;

    @PostMapping("/guardarHcc")
    public ResponseEntity<HccdResultadoDto> registrarHcc(@RequestPart("info") String info,
                                                         @RequestPart("file") MultipartFile file) throws IOException {
        HccdResultadoDto infoRegister = service.registrarConImagen(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(infoRegister);
    }

}
