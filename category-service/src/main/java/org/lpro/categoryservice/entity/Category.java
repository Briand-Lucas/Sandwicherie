package org.lpro.categoryservice.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

    @Id
    private String id;
    private String nom;
    private String desc;

    @OneToMany(mappedBy="category", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Sandwich> sandwichs;
    
    public Category() {
        // necessaire pour JPA !!!!
    }
    
    public Category (String nom, String desc) {
        this.nom = nom;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<Sandwich> getSandwichs() {
        return sandwichs;
    }

    public void setSandwichs(Set<Sandwich> sandwichs) {
        this.sandwichs = sandwichs;
    }
    
    
    
    
    
}
