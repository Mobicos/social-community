package org.example.serviceconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/consume-users")
    public String consumeUsers() {
        return userServiceClient.getRsaPublicKey();
    }
}
