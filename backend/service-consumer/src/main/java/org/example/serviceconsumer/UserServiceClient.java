package org.example.serviceconsumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "my-multi-module-project")
public interface UserServiceClient {

    @GetMapping("/api/users/rsa-public-key")
    String getRsaPublicKey();

}