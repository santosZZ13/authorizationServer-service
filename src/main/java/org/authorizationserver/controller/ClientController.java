package org.authorizationserver.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.authorizationserver.dto.clientdto.ClientDTORequest;
import org.authorizationserver.dto.clientdto.ClientDto;
import org.authorizationserver.exception.GenericResponseSuccessWrapper;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ClientController {

	private final JpaRegisteredClientService jpaRegisteredClientService;


	@PostMapping("/client")
	public GenericResponseSuccessWrapper createClient(@RequestBody @Valid ClientDTORequest clientDTORequest) {
		return jpaRegisteredClientService.createClient(clientDTORequest);
	}
}
