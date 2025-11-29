package com.horolofi.rwa.service.impl;

import com.horolofi.rwa.dto.CreateUserRequestDto;
import com.horolofi.rwa.entity.User;
import com.horolofi.rwa.repository.UserRepository;
import com.horolofi.rwa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(CreateUserRequestDto request) {
        // Anda bisa menambahkan validasi di sini (misal: cek apakah email sudah ada)
        
        User user = new User();
        user.setWalletAddress(request.getWalletAddress());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
}