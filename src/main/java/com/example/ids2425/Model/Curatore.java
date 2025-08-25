package com.example.ids2425.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Curatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    // relazione inversa: un curatore può validare più certificazioni
    @OneToMany(mappedBy = "curatoreValidatore")
    private List<Certificazione> certificazioni;

    // costruttore vuoto richiesto da JPA
    public Curatore() {}

    public Curatore(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Certificazione> getCertificazioni() { return certificazioni; }
    public void setCertificazioni(List<Certificazione> certificazioni) {
        this.certificazioni = certificazioni;
    }
}
