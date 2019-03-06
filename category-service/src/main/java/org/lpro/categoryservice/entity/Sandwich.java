package org.lpro.categoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Sandwich {
    
    @Id
    private String id;
    private String nom;
    private int Budget;
    
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "cateogry_id", nullable = false)
    @JsonIgnore
    private Category category;
    
    public Sandwich() {
        // pour JPA
    }

    public Sandwich(String nom, int Budget) {
        this.nom = nom;
        this.Budget = Budget;
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

    public int getBudget() {
        return Budget;
    }

    public void setBudget(int Budget) {
        this.Budget = Budget;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
    
    
}
