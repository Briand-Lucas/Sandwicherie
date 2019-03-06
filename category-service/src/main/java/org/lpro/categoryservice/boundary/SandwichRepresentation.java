package org.lpro.categoryservice.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.lpro.categoryservice.entity.Category;
import org.lpro.categoryservice.entity.Sandwich;
import org.lpro.categoryservice.exception.NotFound;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/sandwichs", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Category.class)
public class SandwichRepresentation {
    
    private final SandwichResource sr;
    private final CategoryResource cr;

    // Injection de dépendances
    public SandwichRepresentation(SandwichResource sr, CategoryResource cr) {
        this.sr = sr;
        this.cr = cr;
    }
    
    @GetMapping()
    public ResponseEntity<?> getAllSandwichs(){
        
        Iterable<Sandwich> allSandwichs = sr.findAll();
        return new ResponseEntity<>(sandwich2Resource(allSandwichs), HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/{sandwichId}")
    public ResponseEntity<?> getOneSandwich(@PathVariable("sandwichId") String id)
            throws NotFound {
        return Optional.ofNullable(sr.findById(id))
                .filter(Optional::isPresent)
                .map(sandwich -> new ResponseEntity<>(sandwichToResource(sandwich.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Sandwich inexsitant"));
    }
    
    
    private Resources<Resource<Sandwich>> sandwich2Resource(Iterable<Sandwich> sandwichs) {
        Link selfLink = linkTo(SandwichRepresentation.class).withSelfRel();
        List<Resource<Sandwich>> sandwichResources = new ArrayList();
        sandwichs.forEach(sandwich
                -> sandwichResources.add(sandwichToResource(sandwich, false)));
        return new Resources<>(sandwichResources, selfLink);
    }
      
    private Resource<Sandwich> sandwichToResource(Sandwich sandwich, Boolean collection) {
        Link selfLink = linkTo(SandwichRepresentation.class)
                .slash(sandwich.getId())
                .withSelfRel();
        if (collection) {
        Link collectionLink = linkTo(SandwichRepresentation.class).withRel("collection");
        return new Resource<>(sandwich, selfLink, collectionLink);
    } else {
            return new Resource<>(sandwich, selfLink);
        }
    }
    
    
    @PutMapping("/{sandwichId}")
    public ResponseEntity<?> updateSandwich(@PathVariable("sandwichId") String sandwichId,
            @RequestBody Sandwich sandwichUpdated) {
        
        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sandwich.setNom(sandwichUpdated.getNom());
                    sandwich.setBudget(sandwichUpdated.getBudget());
                    sr.save(sandwich);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Sandwich inexistant"));
    }
    
     
    @DeleteMapping("/{sandwichId}")
    public ResponseEntity<?> deleteSandwich(
            @PathVariable("sandwichId") String sandwichId) {
        

        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sr.delete(sandwich);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Sandwich inexistant"));
    }
    
}
