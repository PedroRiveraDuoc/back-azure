package atenciones.back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import atenciones.back.Service.ProductorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ProductorController {

    @Autowired
    private ProductorService producer;

    @PostMapping("/send")
public String sendMessage(@RequestParam("message") String message) {
    producer.enviarMensaje(message);
    return "Mensaje enviado: " + message;
}

}
