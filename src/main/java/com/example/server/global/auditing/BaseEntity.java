package com.example.server.global.auditing;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

	@CreationTimestamp
	@Column(updatable = false)
	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd a HH:mm")
	private LocalDateTime createdDate;

	@UpdateTimestamp
	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd a HH:mm")
	private LocalDateTime updatedDate;
}

