/* package com.prova.e_commerce.grpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrpcController {

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/send-input")
    public String sendInput(@RequestParam String input, @RequestParam int id) {
        grpcClientService.sendInput(input, id);
        return "Input inviato al microservizio Flask!";
    }
}
 */