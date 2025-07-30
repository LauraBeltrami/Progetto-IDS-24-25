package com.example.ids2425.Repository;

import com.example.ids2425.Model.Carrello;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {

    Optional<Carrello> findByAcquirente_Id(Integer acquirenteId);

    // Carica anche items e relativo prodotto (evita LazyInitializationException)
    @EntityGraph(attributePaths = {"items", "items.prodotto"})
    Optional<Carrello> findWithItemsById(Integer id);

    @EntityGraph(attributePaths = {"items", "items.prodotto"})
    Optional<Carrello> findWithItemsByAcquirente_Id(Integer acquirenteId);
}
