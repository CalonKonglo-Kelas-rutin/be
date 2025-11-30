// filepath: d:\Pribadi\Belajar-2025\Kelas-Rutin\CalonKonglo\be\src\main\java\com\horolofi\rwa\service\AssetService.java
package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.AssetListingDto;
import java.util.List;
import com.horolofi.rwa.entity.AssetStatus;

public interface AssetService {
    List<AssetListingDto> getTokenizedAssets(AssetStatus status);
}