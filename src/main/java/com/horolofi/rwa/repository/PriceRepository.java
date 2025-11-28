package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceHistory, Long> {

    // Ambil semua data berdasarkan productId, urutkan dari tanggal terlama ke terbaru
    List<PriceHistory> findByProductIdOrderByRecordedAtAsc(Long productId);

    // Ambil 1 data teratas (Top) diurutkan timestamp descending (terbaru)
    Optional<PriceHistory> findTopByProductIdOrderByRecordedAtDesc(Long productId);
}