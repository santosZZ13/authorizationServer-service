package org.authorizationserver.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity(name = "user")
@Table(name = "user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {
	//	@Id
//	@UuidGenerator(style = UuidGenerator.Style.AUTO)
//	@Column(name = "id", updatable = false, nullable = false)
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "char(36)")
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private UUID id;
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "locale")
	private String locale;
	@Column(name = "avatar_url")
	private String avatarUrl;
	@Column(name = "email_verified")
	private boolean emailVerified;
	@Column(name = "enable")
	private boolean enable;
	@Column(name = "provider")
	private String provider;
	@Column(name = "provider_id")
	private Long providerId;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();


}
