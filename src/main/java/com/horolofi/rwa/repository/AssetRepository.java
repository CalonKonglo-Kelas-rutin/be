package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.Asset;
import com.horolofi.rwa.entity.AssetStatus; // Add this import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByOwnerId(String ownerId);
    List<Asset> findByStatus(AssetStatus status); // Add this method
}
