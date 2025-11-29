package com.horolofi.rwa.entity;

public enum OrderStatus {
    OPEN,           // Order tersedia untuk diproses
    ASK,            // Order sedang dalam proses penawaran
    MATCHED,        // Order sudah terpenuhi sepenuhnya
    CANCELLED       // Order dibatalkan
}