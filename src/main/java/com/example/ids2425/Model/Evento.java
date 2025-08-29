package com.example.ids2425.Model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "eventi")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titolo;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Date data;

    // Relazioni con Acquirenti e Venditori
    @ManyToMany
    @JoinTable(
            name = "evento_acquirenti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "acquirente_id")
    )
    private List<Acquirente> acquirenti = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "evento_venditori",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "venditore_id")
    )
    private List<Venditore> venditori = new ArrayList<>();

    public Evento() {}

    public Evento(Integer id, String titolo, String descrizione, Date data) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public List<Acquirente> getAcquirenti() { return acquirenti; }
    public List<Venditore> getVenditori() { return venditori; }

    // Metodi di supporto
    public void aggiungiAcquirente(Acquirente a) {
        if (a != null && !acquirenti.contains(a)) acquirenti.add(a);
    }

    public void aggiungiVenditore(Venditore v) {
        if (v != null && !venditori.contains(v)) venditori.add(v);
    }
}
