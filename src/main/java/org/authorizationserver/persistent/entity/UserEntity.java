package org.authorizationserver.persistent.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.authorizationserver.enums.Provider;

@Setter
@Getter
@Entity(name = "User")
@Table(name = "app_user")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity implements Serializable {
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
	@Column(name = "provider")
	@Enumerated(EnumType.STRING)
	private Provider provider;
	//	@Column(name = "provider_id")
//	private Long providerId;
	@Column(name = "birth_month")
	private int birthMonth;
	@Column(name = "birth_day")
	private int birthDay;
	@Column(name = "birth_year")
	private int birthYear;
	//	@Column(name = "phone_number")
//	private String phoneNumber;
//	@Column(name = "phone_verified")
//	private boolean phoneVerified;
//	@Column(name = "phone_verified_at")
//	private String phoneVerifiedAt;
//	@Column(name = "phone_verified_code")
//	private String phoneVerifiedCode;
//	@Column(name = "phone_verified_code_expired_at")
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roleEntities = new HashSet<>();


	public Set<GrantedAuthority> getAuthorities() {
		return this
				.getRoleEntities()
				.stream()
				.flatMap(role -> role.getAuthorities()
						.stream()
						.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				)
				.collect(Collectors.toSet());
	}
}
