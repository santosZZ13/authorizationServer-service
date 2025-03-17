package org.authorizationserver.dto;

import jakarta.validation.Valid;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTORequest {
	@Valid
	private ClientDTO client;
}
