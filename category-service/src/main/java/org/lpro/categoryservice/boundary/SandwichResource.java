package org.lpro.categoryservice.boundary;

import java.util.List;
import org.lpro.categoryservice.entity.Sandwich;
import org.springframework.data.repository.CrudRepository;

public interface SandwichResource extends CrudRepository<Sandwich, String> {
    
    List<Sandwich> findByCategoryId(String categoryId);
    
}
