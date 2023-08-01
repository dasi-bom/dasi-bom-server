package com.example.server.global.auditing;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.*;

import java.time.LocalDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

@Getter
@EntityListeners(value = AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

	@CreatedDate
	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd a HH:mm")
	private LocalDateTime createDate;

	@LastModifiedDate
	@JsonFormat(shape = STRING, pattern = "yyyy-MM-dd a HH:mm")
	private LocalDateTime updateDate;
}

