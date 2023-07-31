package com.example.server.domain.member;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.server.config.oauth.provider.OAuth2Provider;
import com.example.server.domain.image.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor // todo how about refactor to static factory method??
@Table(name = "member_tb")
public class Member {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String name; // Username

	private String username; // Spring Security ID

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

	//== static factory method ==//

	//== utility method ==//
	public void updateProfileInfo(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileImage(Image image) {
		this.profileImage = image;
	}
}
