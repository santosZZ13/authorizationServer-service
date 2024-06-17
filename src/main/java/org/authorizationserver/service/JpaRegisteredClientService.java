package org.authorizationserver.service;

import org.authorizationserver.dto.ClientDto;

public interface JpaRegisteredClientService {
	String createClient(ClientDto clientDto);
}
