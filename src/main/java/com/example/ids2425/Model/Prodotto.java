package com.example.ids2425.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;


@Entity
@Table(name = "prodotti")
public class Prodotto {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Column(nullable = false)
    private String nome;


    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prezzo;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venditore_id", nullable = false)
    private Venditore venditore;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoProdotto stato = StatoProdotto.IN_VALIDAZIONE;


    // Presente solo se APPROVATO
    @OneToOne(mappedBy = "prodotto", fetch = FetchType.LAZY)
    private Certificazione certificazione;


    public Prodotto() {}
    public Prodotto(Long id, String nome, BigDecimal prezzo) {
        this.id = id; this.nome = nome; this.prezzo = prezzo;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public Venditore getVenditore() { return venditore; }
    public void setVenditore(Venditore venditore) { this.venditore = venditore; }
    public StatoProdotto getStato() { return stato; }
    public void setStato(StatoProdotto stato) { this.stato = stato; }
    public Certificazione getCertificazione() { return certificazione; }
    public void setCertificazione(Certificazione certificazione) { this.certificazione = certificazione; }


    public boolean isVendibile() { return stato == StatoProdotto.APPROVATO; }
}

