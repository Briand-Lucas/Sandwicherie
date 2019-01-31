package org.lpro.commandservice.boundary;

import java.awt.print.Pageable;
import java.util.List;
import org.lpro.commandservice.entity.Command;
import org.lpro.commandservice.entity.Command;
import org.springframework.data.repository.CrudRepository;

public interface CommandResource extends CrudRepository<Command, String> {
    
    
    List<Command> findByNomIgnoreCase(String nom);
    
    List<Command> findAll(Pageable pageable);
}
