package org.authorizationserver.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationTokenModel {
	private Integer id;

	private String token;

	private String userId;

	private LocalDateTime verificationExpiry;
}
