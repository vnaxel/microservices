package com.clientui.clientui.proxies;

import com.clientui.clientui.beans.CommandeBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservice-commandes", url= "localhost:9002")
public interface MicroserviceCommandeProxy {

    @PostMapping("/commandes")
    CommandeBean ajouterCommande(@RequestBody CommandeBean commande);
}
