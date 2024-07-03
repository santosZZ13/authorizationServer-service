package org.authorizationserver.service;

import org.authorizationserver.dto.clientdto.ClientDTORequest;
import org.authorizationserver.exception.util.GenericResponseSuccessWrapper;

public interface JpaRegisteredClientService {
	GenericResponseSuccessWrapper createClient(ClientDTORequest clientDTORequest);
}
