package org.authorizationserver.service;

import org.authorizationserver.dto.clientdto.ClientDTORequest;
import org.authorizationserver.dto.clientdto.ClientDto;
import org.authorizationserver.exception.GenericResponseSuccessWrapper;

public interface JpaRegisteredClientService {
	GenericResponseSuccessWrapper createClient(ClientDTORequest clientDTORequest);
}
