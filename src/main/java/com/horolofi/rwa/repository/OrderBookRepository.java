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
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> { // Changed String to Long
    
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
}

