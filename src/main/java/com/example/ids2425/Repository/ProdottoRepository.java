package com.example.ids2425.Repository;

import com.example.ids2425.Model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdottoRepository extends JpaRepository<Prodotto, Integer> {
    // tutti i prodotti appartenenti al venditore con quell'id
    List<Prodotto> findByVenditore_Id(Integer venditoreId);
}