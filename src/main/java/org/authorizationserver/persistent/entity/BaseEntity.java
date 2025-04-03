package org.authorizationserver.persistent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;


import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Log4j2
@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {

	public static final String CREATOR_NAME = "auth-service";

	//	@Column(name = "created_by", nullable = false, length = 50)
//	@Column(name = "created_by")
//	public String createdBy;
//
//	@Column(name = "created_date")
//	private LocalDateTime createdDate;
//
//	@Column(name = "modified_by")
//	private String modifiedBy;
//
//	@Column(name = "modified_date")
//	private LocalDateTime modifiedDate;

	@Column(name = "created_at")
	public LocalDateTime createdAt;

	@Column(name = "updated_at")
	public LocalDateTime updatedAat;

	@PrePersist
	void onCreate() {
		log.info("#onCreate - setting created by :: {} and date :: {}", LocalDateTime.now(), CREATOR_NAME);
		this.setCreatedAt(LocalDateTime.now());
		this.setUpdatedAat(LocalDateTime.now());
//		this.setCreatedBy(CREATOR_NAME.toUpperCase());
	}

	@PreUpdate
	void onUpdate() {
		log.info("#onUpdate - setting updated by :: {} and date :: {}", LocalDateTime.now(), CREATOR_NAME);
		this.setUpdatedAat(LocalDateTime.now());
//		this.setModifiedBy(CREATOR_NAME.toUpperCase());
	}
}
