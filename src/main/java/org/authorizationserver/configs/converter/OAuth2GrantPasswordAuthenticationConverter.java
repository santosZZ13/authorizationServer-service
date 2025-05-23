package org.authorizationserver.configs.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.authorizationserver.configs.model.GrantPasswordAuthenticationToken;
import org.authorizationserver.util.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.authorizationserver.configs.model.AuthorizationGrantTypePassword.GRANT_PASSWORD;

public class OAuth2GrantPasswordAuthenticationConverter implements AuthenticationConverter {
	@Override
	public Authentication convert(HttpServletRequest request) {

		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

		if (!GRANT_PASSWORD.getValue().equals(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		// username (REQUIRED)
		String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
		if (!StringUtils.hasText(username) && parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		// password (REQUIRED)
		String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
		if (!StringUtils.hasText(password) && parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}


		Map<String, Object> additionalParameters = parameters.entrySet().stream()
				.filter(entry -> !OAuth2ParameterNames.SCOPE.equals(entry.getKey())
						&& !OAuth2ParameterNames.USERNAME.equals(entry.getKey())
						&& !OAuth2ParameterNames.PASSWORD.equals(entry.getKey())
						&& !OAuth2ParameterNames.GRANT_TYPE.equals(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();


		return new GrantPasswordAuthenticationToken(clientPrincipal, username, password, requestedScopes, additionalParameters);
	}


}
