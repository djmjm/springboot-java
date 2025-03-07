package com.example.repository;

import com.example.model.Person;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    List<Person> findByNameContaining(String namePart);
}