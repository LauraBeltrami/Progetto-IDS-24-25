package com.example.ids2425.Repository;

import com.example.ids2425.Model.BundleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BundleItemRepository extends JpaRepository<BundleItem, Long> {
    Optional<BundleItem> findByBundleIdAndProdottoId(Long bundleId, Long prodottoId);
}
