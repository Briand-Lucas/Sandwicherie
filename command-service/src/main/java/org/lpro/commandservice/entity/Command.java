package org.lpro.commandservice.entity;

import java.sql.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Command {

    @Id
    private String id;
    private String mail;
    private String nom;
    private Date created_at;
    private Date livraison;
    private int status;
    private double montant;
   
    
    Command() {
        // necessaire pour JPA !!!!
    }
    
    public Command (String nom, Date c) {
        this.nom = nom;
        this.created_at = c;
       
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getLivraison() {
        return livraison;
    }

    public void setLivraison(Date livraison) {
        this.livraison = livraison;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }
    
    
    
    
    
    
    
}
