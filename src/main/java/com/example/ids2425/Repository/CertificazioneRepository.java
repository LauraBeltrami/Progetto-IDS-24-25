package com.example.ids2425.Repository;

import com.example.ids2425.Model.Certificazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertificazioneRepository extends JpaRepository<Certificazione, Long> {
    Optional<Certificazione> findByProdottoId(Long prodottoId);
    boolean existsByProdottoId(Long prodottoId);
}