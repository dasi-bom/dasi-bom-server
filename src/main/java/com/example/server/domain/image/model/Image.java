package com.example.server.domain.image.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Image(
		final String imgUrl,
		final String fileName
	) {
		this.imgUrl = imgUrl;
		this.fileName = fileName;
	}

	// public static Image of(
	// 	final String imgUrl,
	// 	final String fileName
	// ) {
	// 	return Image.builder()
	// 		.imgUrl(imgUrl)
	// 		.fileName(fileName)
	// 		.build();
	// }

	public void deleteImage() {
		this.imgUrl = null;
		this.fileName = null;
	}

}
