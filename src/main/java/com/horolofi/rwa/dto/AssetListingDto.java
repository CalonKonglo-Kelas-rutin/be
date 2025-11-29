// filepath: d:\Pribadi\Belajar-2025\Kelas-Rutin\CalonKonglo\be\src\main\java\com\horolofi\rwa\dto\AssetListingDto.java
package com.horolofi.rwa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetListingDto {
    private Integer id;
    private String status;
    private String tokenId;
    private String brand;
    private String model;
    private String imageUrls;
    private BigDecimal price;
    // Tambahkan field lain jika diperlukan, seperti imageUrl atau status
}