package com.example.ids2425.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double prezzo;

    // molti prodotti → 1 venditore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "venditore_id")   // FK nella tabella prodotti
    private Venditore venditore;

    public Prodotto() { }

    public Prodotto(Integer id, String nome, double prezzo) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public Venditore getVenditore() { return venditore; }
    public void setVenditore(Venditore venditore) { this.venditore = venditore; }
}
