package com.example.ids2425.Model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrelli")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 1 acquirente → al massimo 1 carrello
    @OneToOne
    @JoinColumn(name = "acquirente_id", unique = true)
    private Acquirente acquirente;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarrelloItem> items = new ArrayList<>();

    public Carrello() {}
    public Carrello(Integer id, Acquirente acquirente) {
        this.id = id; this.acquirente = acquirente;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Acquirente getAcquirente() { return acquirente; }
    public void setAcquirente(Acquirente acquirente) { this.acquirente = acquirente; }

    public List<CarrelloItem> getItems() { return items; }
    public void setItems(List<CarrelloItem> items) { this.items = items; }

    // utilità per totale/pezzi
    public int getNumeroArticoli() {
        return items.stream().mapToInt(CarrelloItem::getQuantita).sum();
    }
    public double getTotale() {
        return items.stream().mapToDouble(CarrelloItem::getTotaleRiga).sum();
    }
}