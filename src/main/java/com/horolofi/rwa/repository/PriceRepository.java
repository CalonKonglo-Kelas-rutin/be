package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceHistory, Long> {

    // Ambil semua data berdasarkan productId, urutkan dari tanggal terlama ke terbaru
    List<PriceHistory> findByProductIdOrderByRecordedAtAsc(Long productId);

}