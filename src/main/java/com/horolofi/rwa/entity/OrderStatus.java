package com.horolofi.rwa.entity;

public enum OrderStatus {
    PENDING,        // Order belum di-match
    FILLED,         // Order sudah terpenuhi sepenuhnya
    CANCELLED       // Order dibatalkan
}