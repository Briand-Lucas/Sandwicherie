package org.lpro.categoryservice.boundary;

import java.util.UUID;
import org.lpro.categoryservice.entity.Sandwich;
import org.lpro.categoryservice.exception.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SandwichRepresentation {
    
    private final SandwichResource sr;
    private final CategoryResource cr;

    // Injection de dépendances
    public SandwichRepresentation(SandwichResource sr, CategoryResource cr) {
        this.sr = sr;
        this.cr = cr;
    }
    
    @GetMapping("/categories/{id}/sandwichs")
    public ResponseEntity<?> getSandwichByCategoryId(@PathVariable("id") String id)
            throws NotFound {
        
        if (!cr.existsById(id)) {
            throw new NotFound("Catégorie inexistante");
        }
        return new ResponseEntity<>(sr.findByCategoryId(id), HttpStatus.OK);
    }
  
    @PostMapping("/categories/{id}/sandwichs")
    public ResponseEntity<?> ajoutSandwich(@PathVariable("id") String id,
            @RequestBody Sandwich sandwich) throws NotFound {
        return cr.findById(id)
                .map(category -> {
                    sandwich.setId(UUID.randomUUID().toString());
                    sandwich.setCategory(category);
                    sr.save(sandwich);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).orElseThrow ( () -> new NotFound("Catégorie inexistante"));
    }
    
    @PutMapping("/categories/{categoryId}/sandwichs/{sandwichId}")
    public ResponseEntity<?> updateSandwich(@PathVariable("categoryId") String categoryId,
            @PathVariable("sandwichId") String sandwichId,
            @RequestBody Sandwich sandwichUpdated) {
        
        if (!cr.existsById(categoryId)) {
            throw new NotFound("Catégorie inexistante");
        }
        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sandwichUpdated.setId(sandwich.getId());
                    sr.save(sandwichUpdated);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Catégorie inexistante"));
    }
     
    @DeleteMapping("/icategories/{categoryId}/sandwichs/{sandwichId}")
    public ResponseEntity<?> deleteSandwich(@PathVariable("categoryId") String categoryId,
            @PathVariable("sandwichId") String sandwichId) {
        
        if (!cr.existsById(categoryId)) {
            throw new NotFound("Catégorie inexistante");
        }
        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sr.delete(sandwich);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Sandwich inexistant"));
    }
    
}
