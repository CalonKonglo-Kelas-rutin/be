package com.horolofi.rwa.service;

import com.horolofi.rwa.dto.CreateUserRequestDto;
import com.horolofi.rwa.entity.User;

public interface UserService {
    User registerUser(CreateUserRequestDto request);
}