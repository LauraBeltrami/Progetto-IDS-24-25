package com.example.ids2425.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "acquirenti")
public class Acquirente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) private String nome;
    @Column(nullable = false) private String cognome;
    @Column(nullable = false, unique = true) private String email;

    public Acquirente() {}
    public Acquirente(Integer id, String nome, String cognome, String email) {
        this.id = id; this.nome = nome; this.cognome = cognome; this.email = email;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
