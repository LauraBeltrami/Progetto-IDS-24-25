package com.example.ids2425.Repository;

import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.StatoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    List<Prodotto> findByStato(StatoProdotto stato);
}