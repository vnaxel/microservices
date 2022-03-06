package com.clientui.clientui.controller;

import com.clientui.clientui.beans.CardNumberBean;
import com.clientui.clientui.beans.CommandeBean;
import com.clientui.clientui.beans.PaiementBean;
import com.clientui.clientui.beans.ProductBean;
import com.clientui.clientui.proxies.MicroserviceCommandeProxy;
import com.clientui.clientui.proxies.MicroservicePaiementProxy;
import com.clientui.clientui.proxies.MicroserviceProduitsProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class ClientController {

    @Autowired
    private MicroserviceProduitsProxy ProduitsProxy;

    @Autowired
    private MicroserviceCommandeProxy CommandesProxy;

    @Autowired
    private MicroservicePaiementProxy paiementProxy;

    /*
     * Étape (1)
     * Opération qui récupère la liste des produits et on les affiche dans la page d'accueil.
     * Les produits sont récupérés grâce à ProduitsProxy
     * On finit par retourner la page Accueil.html à laquelle on passe la liste d'objets "produits" récupérés.
     * */
    @RequestMapping("/")
    public String accueil(Model model) {
        List<ProductBean> produits = ProduitsProxy.listeDesProduits();
        model.addAttribute("produits", produits);
        return "Accueil";
    }

    /*
     * Étape (2)
     * Opération qui récupère les détails d'un produit
     * On passe l'objet "produit" récupéré et qui contient les détails en question à FicheProduit.html
     * */
    @RequestMapping("/details-produit/{id}")
    public String ficheProduit(@PathVariable int id, Model model){
        ProductBean produit = ProduitsProxy.recupererUnProduit(id);
        model.addAttribute("produit", produit);
        return "FicheProduit";
    }

    /*
    * Étape (3) et (4)
    * Opération qui fait appel au microservice de commande pour passer une commande et récupérer les details de la commande créée
    * */
    @RequestMapping("/commander-produit/{idProduit}/{montant}")
    public String passerCommande(@PathVariable int idProduit, @PathVariable Double montant, Model model) {

        CommandeBean commande = new CommandeBean();

        commande.setProductId(idProduit);
        commande.setQuantite(1);
        commande.setDateCommande(new Date());

        CommandeBean commandeAjoutee = CommandesProxy.ajouterCommande(commande);

        model.addAttribute("commande", commandeAjoutee);
        model.addAttribute("montant", montant);

        return "Paiement";
    }

    /*
     * Étape (5)
     * Opération qui fait appel au microservice de paiement pour traiter un paiement
     * */
    @RequestMapping("/payer-commande/{idCommande}/{montantCommande}")
    public String payerCommande(@PathVariable int idCommande, @PathVariable Double montantCommande, Model model){

        PaiementBean paiementAExecuter = new PaiementBean();

        paiementAExecuter.setIdCommande(idCommande);
        paiementAExecuter.setMontant(montantCommande);
        paiementAExecuter.setNumeroCarte(numcarte());

        ResponseEntity<PaiementBean> paiement = paiementProxy.payerUneCommande(paiementAExecuter);

        Boolean paiementAccepte = false;
        if(paiement.getStatusCode() == HttpStatus.CREATED) {
            paiementAccepte = true;
        }
        model.addAttribute("paiementOk", paiementAccepte);

        return "Confirmation";
    }

    private Long numcarte() {
        return ThreadLocalRandom.current().nextLong(1000000000000000L,9000000000000000L );
    }
}
