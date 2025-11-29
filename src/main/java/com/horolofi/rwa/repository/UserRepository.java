package com.horolofi.rwa.repository;

import com.horolofi.rwa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByWalletAddress(String WalletAddress);
    Optional<User> findFirstByWalletAddress(String WalletAddress);
}

