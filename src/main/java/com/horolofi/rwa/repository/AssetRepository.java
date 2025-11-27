package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findBySerialNumber(String serialNumber);
}

