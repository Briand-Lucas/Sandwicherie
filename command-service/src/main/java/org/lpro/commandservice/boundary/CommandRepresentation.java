package org.lpro.commandservice.boundary;

import java.awt.print.Pageable;
import static java.nio.file.Files.size;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.lpro.commandservice.entity.Command;
import org.lpro.commandservice.exception.NotFound;
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
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;

@RestController
@RequestMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Command.class)
public class CommandRepresentation {

    private final CommandResource cr;
    

    public CommandRepresentation(CommandResource cr) {
        this.cr =  cr;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllCommands(@RequestParam(value= "page", required = true) Integer page,
                                              @RequestParam(value= "size", required = true) Integer size) {
        
        return new ResponseEntity<>(cr.findAll((Pageable) PageRequest.of(page, size)), HttpStatus.OK);
        
    }

 
    private Resources<Resource<Command>> command2Resource(Iterable<Command> commands) {
        Link selfLink = linkTo(CommandRepresentation.class).withSelfRel();
        List<Resource<Command>> commandResources = new ArrayList();
        commands.forEach(command
                -> commandResources.add(commandToResource(command, false)));
        return new Resources<>(commandResources, selfLink);
    }
    
    private Resource<Command> commandToResource(Command command, Boolean collection) {
        Link selfLink = linkTo(CommandRepresentation.class)
                .slash(command.getId())
                .withSelfRel();
        if (collection) {
        Link collectionLink = linkTo(CommandRepresentation.class).withRel("collection");
        return new Resource<>(command, selfLink, collectionLink);
    } else {
            return new Resource<>(command, selfLink);
        }
    }
    
    
    @GetMapping(value = "/{commandId}")
    public ResponseEntity<?> getOne(@PathVariable("commandId") String id)
            throws NotFound {
        return Optional.ofNullable(cr.findById(id))
                .filter(Optional::isPresent)
                .map(command -> new ResponseEntity<>(commandToResource(command.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Commande inexsitante"));
    }

   

    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Command command) {
        command.setId(UUID.randomUUID().toString());
        Command saved = cr.save(command);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CommandRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{commandId}")
    public ResponseEntity<?> putMethod(@PathVariable("commandId") String id,
            @RequestBody Command commandUpdated) throws NotFound {
        return cr.findById(id)
                .map(command -> {
                    command.setId(commandUpdated.getId());
                    cr.save(command);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Commande inexsitante"));
    }

    @DeleteMapping(value = "/{commandId}")
    public ResponseEntity<?> deleteMethod(@PathVariable("commandId") String id) throws NotFound {
        return cr.findById(id)
                .map(command -> {
                    cr.delete(command);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Commande inexsitante"));
    }
}
