package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.ClientDto;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {

	private final JpaRegisteredClientService jpaRegisteredClientService;


	@PostMapping("/oauth2/client")
	public String createClient(@RequestBody ClientDto clientDto) {
		return jpaRegisteredClientService.createClient(clientDto);
	}
}
