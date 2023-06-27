package com.example.server.domain;

import com.example.server.config.oauth.provider.OAuth2Provider;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name; // 사용자명
	private String username; // 아이디
	private String password;
	private String email;
	private String mobile;
	@Enumerated(EnumType.STRING)
	private RoleType role;
	@Enumerated(EnumType.STRING)
	private OAuth2Provider provider;
	private String providerId;
	@CreationTimestamp
	private Timestamp createDate;
}
