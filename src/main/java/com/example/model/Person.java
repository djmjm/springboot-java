package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.UUID;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    public Person() {
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(nullable=false)
    private String name;

    @Min(value=1)
    @Column(nullable=false)
    private int age;

    @CPF
    @Column(nullable=false, unique=true)
    private String cpf;

    @Email
    @Column(nullable=false, unique=true)
    private String email;


    public UUID getId(){
        return id;
    }
    public Person(Person original){
        this.id = original.id;
        this.name = original.name;
        this.age = original.age;
        this.email = original.email;
        this.cpf = original.cpf;
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
