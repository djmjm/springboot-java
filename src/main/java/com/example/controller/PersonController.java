package com.example.controller;

import com.example.controller.response.BodyMessage;
import com.example.model.Person;
import com.example.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public ResponseEntity <Map <String, Object>> getAllPersons() {

        BodyMessage bodyMessage = new BodyMessage(personRepository.findAll(), HttpStatus.OK);

        return ResponseEntity.status(bodyMessage.getStatusCode()).body(
                bodyMessage.getResponse()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity <Map <String, Object>> getPersonById(@PathVariable Long id) {
        Object person = personRepository.findById(id);
        BodyMessage response = new BodyMessage(person, HttpStatus.OK);
        BodyMessage responseNull = new BodyMessage("{}", HttpStatus.NOT_FOUND);

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
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePersonById(
            @PathVariable long id,
            @RequestBody Person person){

        return personRepository.findById(id)
                .map(existingPerson -> {

                    existingPerson.setName(person.getName());
                    existingPerson.setAge(person.getAge());

                    Person updatedPerson = personRepository.save(existingPerson);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());


    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }


}