package com.valer.rip.lab1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.valer.rip.lab1.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}