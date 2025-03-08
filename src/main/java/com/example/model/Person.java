package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Min(1)
    @Column(nullable = false)
    private int age;

    @CPF
    @Column(nullable = false, unique = true)
    private String cpf;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    
    public Person() {
    }

    public Person(UUID id, String name, int age, String cpf, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.cpf = cpf;
        this.email = email;
    }

    public Person(Person original) {
        this.id = original.id;
        this.name = original.name;
        this.age = original.age;
        this.cpf = original.cpf;
        this.email = original.email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
