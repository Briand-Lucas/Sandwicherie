package org.lpro.categoryservice.boundary;

import java.util.List;
import org.lpro.categoryservice.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface CategoryResource extends CrudRepository<Category, String> {
    
    
    List<Category> findByNomIgnoreCase(String nom);
    
    List<Category> findAll(Pageable pageable);
}
