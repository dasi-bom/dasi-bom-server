package com.example.server.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.server.config.oauth.provider.OAuth2Provider;
import com.example.server.domain.member.Member;

@Repository
@Transactional(readOnly = true)
public interface MemberQueryRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByProviderId(String providerId);

	Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String providerId);

	boolean existsByNickname(String nickname);
}


