package com.example.ids2425.DTO;

import java.math.BigDecimal;

public record RigaBundleDTO(
        Long bundleId,
        String nome,
        BigDecimal prezzoUnitario,
        int quantita,
        BigDecimal totaleRiga
) {}
