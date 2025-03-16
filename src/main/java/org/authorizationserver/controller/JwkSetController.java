package org.authorizationserver.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetController {

	@Autowired
	private JWKSource<SecurityContext> jwkSource;

	@GetMapping("/oauth2/jwks")
	public Map<String, Object> getJwkSet() throws Exception {
		JWKSet jwkSet = (JWKSet) jwkSource.get(null, null);
		return jwkSet.toJSONObject();
	}
}
