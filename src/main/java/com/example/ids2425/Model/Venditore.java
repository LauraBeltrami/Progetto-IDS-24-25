package com.example.ids2425.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "venditori")
public class Venditore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true) // no duplicati di nome
    private String nome;

    public Venditore() { }

    public Venditore(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
