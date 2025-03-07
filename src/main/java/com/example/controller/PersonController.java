package com.example.controller;

import com.example.controller.cache.CacheConfig;
import com.example.controller.response.BodyMessage;
import com.example.model.Person;
import com.example.repository.PersonRepository;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;
    private final Cache<String, Long> requestCache;

    @Autowired
    public PersonController(PersonRepository personRepository,  CacheConfig requestCache) {
        this.personRepository = personRepository;
        this.requestCache = requestCache.getCache();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity <Map <String, Object>> getAllPersons(@Min(1) int pageindex) {
        Pageable pages = PageRequest.of(pageindex*1 - 1, pageindex*1 + 8);
        BodyMessage bodyMessage = new BodyMessage(personRepository.findAll(pages).get(), HttpStatus.OK,
                "Carregado com sucesso!");

        return ResponseEntity.status(bodyMessage.getStatusCode()).body(
                bodyMessage.getResponse()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity <Map <String, Object>> getPersonById(@PathVariable UUID id) {
        Object person = personRepository.findById(id);
        BodyMessage response = new BodyMessage(person, HttpStatus.OK,
                "Carregado com sucesso!"
                );
        BodyMessage responseNull = new BodyMessage("{}", HttpStatus.NOT_FOUND,
                "Não encontrado!"
                );

        if(person == Optional.empty()){
            return ResponseEntity.status(responseNull.getStatusCode()).
                    body(responseNull.getResponse()
                    );
        }else{
            return ResponseEntity.status(response.getStatusCode()).
                    body(response.getResponse()
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Map<String, Object>> deletePerson(@PathVariable UUID id) {
        Object person = personRepository.findById(id);

        BodyMessage response = new BodyMessage(person, HttpStatus.OK,
                "Deletado com sucesso!"
                );
        BodyMessage responseNull = new BodyMessage("{}", HttpStatus.NOT_FOUND,
                "Não encontrado!"
                );

        if (person != Optional.empty()) {
            personRepository.deleteById(id);
            return ResponseEntity.status(response.getStatusCode()).
                    body(response.getResponse()
                    );
        } else {
            return ResponseEntity.status(responseNull.getStatusCode()).
                    body(responseNull.getResponse()
                    );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity <Map <String, Object>> updatePersonById(
            @PathVariable UUID id,
            @RequestBody Person person){

        BodyMessage responseNull = new BodyMessage("{}", HttpStatus.NOT_FOUND,
                "Não encontrado!"
                );
        return personRepository.findById(id)
                .map(existingPerson -> {

                    existingPerson.setName(person.getName());
                    existingPerson.setAge(person.getAge());
                    existingPerson.setCpf(person.getCpf());
                    existingPerson.setEmail(person.getEmail());
                    Person updatedPerson = personRepository.save(existingPerson);
                    BodyMessage response = new BodyMessage(updatedPerson, HttpStatus.OK,
                            "Atualizado com sucesso!"
                            );

                    return ResponseEntity.status(response.getStatusCode()).
                            body(response.getResponse());
                })
                .orElse(
                        ResponseEntity.status(responseNull.getStatusCode()).
                       body(responseNull.getResponse())
                );


    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity <Map<String, Object>> createPerson(@Valid @RequestBody Person person, HttpServletRequest request) {

        try {
            String clientIp = request.getRemoteAddr();
            Long lastRequestTime = requestCache.getIfPresent(clientIp);

            if (lastRequestTime != null) {
                long timeSinceLastRequest = System.currentTimeMillis() - lastRequestTime;
                if (timeSinceLastRequest < 60 * 1000) {
                    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                            .body(Map.of("message", "Aguarde 60 segundos."));
                }
            }
            requestCache.put(clientIp, System.currentTimeMillis());

            Person savedPerson = personRepository.save(person);
            BodyMessage response = new BodyMessage(savedPerson, HttpStatus.CREATED,
                    "Criado com sucesso!"
                    );
            return ResponseEntity.status(response.getStatusCode()).body(response.getResponse());
        }catch(Exception e){
            BodyMessage response = new BodyMessage("Erro de entrada!", HttpStatus.INTERNAL_SERVER_ERROR,
                    "Entrada Invalida"
                    );
            return ResponseEntity.status(response.getStatusCode()).body(response.getResponse());
        }
    }


}