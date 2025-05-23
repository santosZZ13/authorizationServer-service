package org.authorizationserver.configs.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.authorizationserver.dao.EmailDaoRepository;
import org.authorizationserver.exception.LoginException;
import org.authorizationserver.exception.NotFoundUserException;
import org.authorizationserver.persistent.entity.UserEntity;
import org.authorizationserver.persistent.entity.VerificationTokenEntity;
import org.authorizationserver.persistent.repository.UserRepository;
import org.authorizationserver.persistent.repository.VerificationTokenRepository;
import org.authorizationserver.service.UserService;
import org.authorizationserver.util.GenerateCodeOTP;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;


@Component
@AllArgsConstructor
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();
	private final Consumer<OAuth2User> oauth2UserHandler = (user) -> {
	};
	private final UserServiceOAuth2UserHandler oidcUserHandler;
	private final UserRepository userRepository;
	private final VerificationTokenRepository verificationTokenRepository;
	private final EmailDaoRepository emailDaoRepository;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException, ServletException {
		if (authentication instanceof OAuth2AuthenticationToken) {
			if (authentication.getPrincipal() instanceof OidcUser) {
				this.oidcUserHandler.accept((OidcUser) authentication.getPrincipal());
			} else if (authentication.getPrincipal() instanceof OAuth2User) {
				this.oauth2UserHandler.accept((OAuth2User) authentication.getPrincipal());
			}
			this.delegate.onAuthenticationSuccess(request, response, authentication);
		} else {
			try {
				String email = authentication.getName();
				Optional<UserEntity> userEntityByEmail = userRepository.findByEmail(email);
				if (userEntityByEmail.isEmpty()) {
					throw new NotFoundUserException("User not found", "", "");
				}
				UserEntity userEntity = userEntityByEmail.get();
				if (!userEntity.isActive()) {
					String otp = GenerateCodeOTP.generateOTP();
					VerificationTokenEntity tokenEntity = new VerificationTokenEntity(otp, userEntity);
					verificationTokenRepository.save(tokenEntity);
					sendVerificationEmail(email, otp);
					// Lưu email vào session
					HttpSession session = request.getSession();
					session.setAttribute("verificationEmail", email);
//					session.setAttribute("isOAuth2Flow", false); // Đây là form login
					response.sendRedirect("/confirmCode");
				} else {
					this.delegate.onAuthenticationSuccess(request, response, authentication);
				}
			} catch (Exception e) {
				throw new LoginException("An unexpected error occurred during login", e.getMessage(), "");
			}
		}
	}

	private void sendVerificationEmail(String email, String otp) {
		emailDaoRepository.sendVerificationEmail(email, otp);
	}
}
