package com.example.server.repository;

import com.example.server.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Integer> {
	Member findByUsername(String username);
}


