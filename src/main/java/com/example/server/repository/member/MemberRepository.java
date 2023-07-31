package com.example.server.repository.member;

import com.example.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
}


