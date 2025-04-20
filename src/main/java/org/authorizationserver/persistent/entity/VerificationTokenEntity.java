package org.authorizationserver.persistent.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.authorizationserver.model.VerificationTokenModel;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "VerificationTokenEntity")
@Table(name = "verification_token")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTokenEntity extends BaseEntity {
	private static final int EXPIRATION = 60 * 24;
	private static final int EXPIRATION_MINUTES = 30;

	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "verification_token_seq")
	@GenericGenerator(
			name = "verification_token_seq",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "verification_token_seq"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
			}
	)
	private Integer id;

	private String token;

	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(nullable = false, name = "user_id")
	private UserEntity user;

	private LocalDateTime verificationExpiry;

	public VerificationTokenEntity(String token, UserEntity user) {
		this.token = token;
		this.user = user;
		this.verificationExpiry = LocalDateTime.now()
				.plusMinutes(EXPIRATION_MINUTES);
	}
}
