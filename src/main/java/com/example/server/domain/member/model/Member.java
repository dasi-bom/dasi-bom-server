package com.example.server.domain.member.model;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.server.domain.image.model.Image;
import com.example.server.domain.member.model.constants.RoleType;
import com.example.server.domain.pet.model.Pet;
import com.example.server.global.auditing.BaseEntity;
import com.example.server.global.oauth.provider.constants.OAuth2Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member_tb")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "member_id")
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

	@OneToOne(fetch = LAZY, cascade = REMOVE)
	@JoinColumn(name = "image_id")
	private Image profileImage;

	private String nickname;

	@OneToMany
	private List<Pet> pets = new ArrayList<>();

	//== static factory method ==//

	//== utility method ==//
	public void updateProfileInfo(String nickname) {
		this.nickname = nickname;
	}

	public void updateProfileImage(Image image) {
		this.profileImage = image;
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	public void addPet(Pet pet) {
		this.pets.add(pet);
	}
}
