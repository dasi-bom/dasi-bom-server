package com.example.server.domain.image.model;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "image_tb")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String imgUrl;
	private String fileName;

	@Builder
	public Image(String imgUrl, String fileName) {
		this.imgUrl = imgUrl;
		this.fileName = fileName;
	}

	public void updateImage(String imgUrl, String fileName) {
		this.imgUrl = imgUrl;
		this.fileName = fileName;
	}
}
