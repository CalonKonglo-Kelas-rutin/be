package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.OrderBook;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;
import com.horolofi.rwa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, String> {
    
    @Query("SELECT DISTINCT ob.buyer FROM OrderBook ob " +
           "JOIN ob.asset a " +
           "WHERE a.serialNumber = :serialNumber")
    List<User> findUsersByAssetSerialNumber(@Param("serialNumber") String serialNumber);
    
    // FIX: Hapus @Query manual. Biarkan Spring Data JPA menangani query secara otomatis.
    // Spring akan otomatis memetakan:
    // AssetId -> field asset.id
    // OrderType -> field orderType
    // Status -> field status
    Page<OrderBook> findByAssetIdAndOrderTypeAndStatus(
            String assetId, 
            OrderType orderType, 
            OrderStatus status, 
            Pageable pageable
    );
    
    // FIX: Hapus @Query manual juga untuk count
    long countByAssetIdAndOrderTypeAndStatus(
            String assetId, 
            OrderType orderType, 
            OrderStatus status
    );
}

