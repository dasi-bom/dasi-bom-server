package com.example.server.domain.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
}


