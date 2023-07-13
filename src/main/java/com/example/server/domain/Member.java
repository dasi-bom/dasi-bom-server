package com.example.server.domain;

import com.example.server.config.oauth.provider.OAuth2Provider;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@Table(name = "member_tb")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "image_id")
	private Image profileImage;
	private String nickname;
	@CreationTimestamp
	private Timestamp createDate;

	public void updateProfileInfo(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileImage(Image image) {
		this.profileImage = image;
	}
}
