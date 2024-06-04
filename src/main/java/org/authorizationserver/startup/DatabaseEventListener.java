package org.authorizationserver.startup;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseEventListener {
	@EventListener
	public void appReady(ApplicationReadyEvent event) {
		System.out.println("Application ready event fired!");
	}
}
