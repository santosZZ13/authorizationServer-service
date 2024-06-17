package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.ClientDto;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {

	private final JpaRegisteredClientService jpaRegisteredClientService;


	@PostMapping("/client")
	public String createClient() {
		return "jpaRegisteredClientService.createClient(clientDto)";
	}
}
