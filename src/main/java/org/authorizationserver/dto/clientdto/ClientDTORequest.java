package org.authorizationserver.dto.clientdto;

import jakarta.validation.Valid;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTORequest {
	@Valid
	private ClientDto client;
}
