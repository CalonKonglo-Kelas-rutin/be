package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.OrderBook;
import com.horolofi.rwa.entity.OrderStatus;
import com.horolofi.rwa.entity.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.horolofi.rwa.entity.User;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    
    // Ubah ob.buyer menjadi ob.maker_address
    @Query("SELECT DISTINCT ob.maker_address FROM OrderBook ob " +
           "JOIN ob.asset a " +
           "WHERE a.serialNumber = :serialNumber")
    List<User> findUsersByAssetSerialNumber(@Param("serialNumber") String serialNumber);
    
    Page<OrderBook> findByAssetIdAndOrderTypeAndStatus(
            String assetId, 
            OrderType orderType, 
            OrderStatus status, 
            Pageable pageable
    );
    
    long countByAssetIdAndOrderTypeAndStatus(
            String assetId, 
            OrderType orderType, 
            OrderStatus status
    );
    
    // Tambahkan Query Custom ini untuk menangani maker_address dan filter user
    @Query("SELECT o FROM OrderBook o WHERE o.maker_address.walletAddress = :walletAddress AND o.asset.id = :assetId")
    Page<OrderBook> findByUserWalletAddressAndAssetId(@Param("walletAddress") String walletAddress, @Param("assetId") Long assetId, Pageable pageable);

    @Query("SELECT o FROM OrderBook o WHERE o.maker_address.walletAddress = :walletAddress AND o.asset.id = :assetId AND o.status = :status")
    Page<OrderBook> findByUserWalletAddressAndAssetIdAndStatus(@Param("walletAddress") String walletAddress, @Param("assetId") Long assetId, @Param("status") OrderStatus status, Pageable pageable);
}

