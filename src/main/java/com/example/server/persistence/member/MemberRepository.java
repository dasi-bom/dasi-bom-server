package com.example.server.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.Member;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
}


