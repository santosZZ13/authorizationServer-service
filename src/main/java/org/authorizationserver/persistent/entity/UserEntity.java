package org.authorizationserver.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "User")
@Table(name = "app_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {
	//	@GeneratedValue(generator = "UUID")
//	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//	@Column(name = "id", columnDefinition = "char(36)")
//	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Id
	@UuidGenerator(style = UuidGenerator.Style.TIME)
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	// locate: en_US, zh_CN, ja_JP
	@Column(name = "locale")
	private String locale;
	@Column(name = "avatar_url")
	private String avatarUrl;
	@Column(name = "email_verified")
	private boolean emailVerified;
	@Column(name = "active")
	private boolean active;
	/**
	 * The provider name, such as google, github, etc.
	 */
	@Column(name = "provider")
	private String provider;
//	/**
//	 * The provider id, such as google id, github id, etc.
//	 */
//	@Column(name = "provider_id")
//	private Long providerId;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roleEntities = new HashSet<>();
}
