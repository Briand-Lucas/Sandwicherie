package org.lpro.categoryservice.boundary;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.lpro.categoryservice.entity.Category;
import org.lpro.categoryservice.exception.NotFound;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
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
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Category.class)
public class CategoryRepresentation {

    private final CategoryResource cr;
    private final SandwichResource sr;

    public CategoryRepresentation(CategoryResource cr, SandwichResource sr) {
        this.cr =  cr;
        this.sr = sr;
    }
    
  

 
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        Iterable<Category> allCategories = cr.findAll();
        return new ResponseEntity<>(category2Resource(allCategories), HttpStatus.OK);
    }
 
    private Resources<Resource<Category>> category2Resource(Iterable<Category> categories) {
        Link selfLink = linkTo(CategoryRepresentation.class).withSelfRel();
        List<Resource<Category>> categoryResources = new ArrayList();
        categories.forEach(category
                -> categoryResources.add(categoryToResource(category, false)));
        return new Resources<>(categoryResources, selfLink);
    }
    
    private Resource<Category> categoryToResource(Category category, Boolean collection) {
        Link selfLink = linkTo(CategoryRepresentation.class)
                .slash(category.getId())
                .withSelfRel();
        if (collection) {
        Link collectionLink = linkTo(CategoryRepresentation.class).withRel("collection");
        return new Resource<>(category, selfLink, collectionLink);
    } else {
            return new Resource<>(category, selfLink);
        }
    }
    
    
    @GetMapping(value = "/{categoryId}")
    public ResponseEntity<?> getOne(@PathVariable("categoryId") String id)
            throws NotFound {
        return Optional.ofNullable(cr.findById(id))
                .filter(Optional::isPresent)
                .map(category -> new ResponseEntity<>(categoryToResource(category.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Catégorie inexsitante"));
    }

    @GetMapping("/{id}/sandwichs")
    public ResponseEntity<?> getSandwichByCategoryId(@PathVariable("id") String id)
            throws NotFound {
        if (!cr.existsById(id)) {
            throw new NotFound("Catégorie inexsitante");
        }
        return new ResponseEntity<>(sr.findByCategoryId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Category category) {
        category.setId(UUID.randomUUID().toString());
        Category saved = cr.save(category);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CategoryRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<?> putMethod(@PathVariable("categoryId") String id,
            @RequestBody Category categoryUpdated) throws NotFound {
        return cr.findById(id)
                .map(category -> {
                    category.setId(categoryUpdated.getId());
                    cr.save(category);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Catégorie inexsitante"));
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<?> deleteMethod(@PathVariable("categoryId") String id) throws NotFound {
        return cr.findById(id)
                .map(category -> {
                    cr.delete(category);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Catégorie inexsitante"));
    }
}
