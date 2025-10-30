package com.example.ids2425.Controller;


import com.example.ids2425.DTO.BundleDTO;
import com.example.ids2425.DTO.BundleMapper;
import com.example.ids2425.DTO.ProdottoDTO;
import com.example.ids2425.Model.Bundle;
import com.example.ids2425.Model.Prodotto;
import com.example.ids2425.Model.StatoProdotto;
import com.example.ids2425.Repository.BundleRepository;
import com.example.ids2425.Service.ProdottoService;
import com.example.ids2425.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final ProdottoService prodottoService;
    private final BundleRepository bundleRepo;

    public CatalogoController(ProdottoService prodottoService, BundleRepository bundleRepo) {
        this.prodottoService = prodottoService;
        this.bundleRepo = bundleRepo;
    }

    // Tutti i prodotti APPROVATI (default); opz: ?stato=IN_VALIDAZIONE
    @GetMapping("/prodotti")
    public List<ProdottoDTO> listaProdotti() {
        return prodottoService.listaVendibili(); // solo APPROVATI
    }

    @GetMapping("/prodotti/{id}")
    public ProdottoDTO dettaglioProdotto(@PathVariable Long id) {
        ProdottoDTO dto = prodottoService.getById(id);
        if (!"APPROVATO".equals(dto.stato())) {
            throw new NotFoundException("Prodotto non trovato");
        }
        return dto;
    }

    // (Opzionale) Catalogo bundle
    @GetMapping("/bundles")
    public List<BundleDTO> listaBundles(@RequestParam(required = false) Long distributoreId) {
        if (distributoreId == null) {
            return BundleMapper.toDTO(bundleRepo.findAllGraph());
        }
        return BundleMapper.toDTO(bundleRepo.findGraphByDistributoreId(distributoreId));
    }

    @GetMapping("/bundles/{id}")
    public BundleDTO dettaglioBundle(@PathVariable Long id) {
        return bundleRepo.findGraphById(id)
                .map(BundleMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Bundle non trovato"));
    }
}