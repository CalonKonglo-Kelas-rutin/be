package com.horolofi.rwa.entity;

public enum OrderStatus {
    OPEN,           // Order tersedia untuk diproses
    ASK,            // Order sedang dalam proses penawaran
    MATCHED,        // Order sudah terpenuhi sepenuhnya
    FILLED,         // Order sudah selesai dieksekusi 
    CANCELLED       // Order dibatalkan oleh pengguna
}