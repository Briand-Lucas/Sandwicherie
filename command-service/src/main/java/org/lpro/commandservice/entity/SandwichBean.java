package org.lpro.commandservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SandwichBean {
    
    @Id
    private String id;
    private String nom;
    private int montant;
    private int port;
    
    
    
    SandwichBean() {
        // pour JPA
    }

    public SandwichBean(String id, String nom, int m, int port) {
        this.id = id;
        this.nom = nom;
        this.montant = m;
        this.port = port;
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
    
    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    
    
    
}
