/* package com.prova.e_commerce.dbRel.oracle.jpa.controller.soap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RestController
@RequestMapping("/wsdl")
public class WsdlController {

    @GetMapping("/userservice.wsdl")
    public ResponseEntity<Object> getWSDL() throws IOException {
        ClassPathResource wsdlResource = new ClassPathResource("wsdl/UserService.wsdl");
        return ResponseEntity.ok()
                .header("Content-Type", "application/wsdl+xml")
                .body(wsdlResource);
    }
}
 */