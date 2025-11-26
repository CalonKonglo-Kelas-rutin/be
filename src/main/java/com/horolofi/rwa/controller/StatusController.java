package com.calonkonglo.kelasrutin.be.controller; // Sesuaikan dengan package Anda

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/status")
    public String checkStatus() {
        // Ini hanya mengembalikan status 200 OK dan teks
        return "OK - Service is running smoothly.";
    }
}