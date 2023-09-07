package com.example.server.domain.member.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.domain.member.model.Member;
import com.example.server.global.oauth.provider.constants.OAuth2Provider;

@Repository
@Transactional
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByProviderId(String providerId);

	Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

	boolean existsByNickname(String nickname);
}
