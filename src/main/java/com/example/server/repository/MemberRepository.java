package com.example.server.repository;

import com.example.server.config.oauth.provider.OAuth2Provider;
import com.example.server.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByProviderId(String providerId);
	Optional<Member> findByProviderAndProviderId(OAuth2Provider provider, String providerId);
}


