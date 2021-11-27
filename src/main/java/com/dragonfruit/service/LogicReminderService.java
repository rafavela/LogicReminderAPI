package com.dragonfruit.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dragonfruit.bean.ReminderBean;

@Service
@ConfigurationProperties(prefix="reminder")
public class LogicReminderService {
	
	private String phraseEndpoint;
	private String countEndpoint;
	private Integer minInteger;
	
	@Autowired
	private WebClient.Builder webClientBuilder;	
	
	public ReminderBean getReminderBean() {	
		Long count = this.countReminder();
		int randomId=this.getRandomId(count);
		ReminderBean reminderBean=this.getMono(phraseEndpoint+randomId, ReminderBean.class);
		
		return reminderBean;
	}
	
	private <T> T getMono(String uri,Class<T> scienceFictionObject) {
		return webClientBuilder.build()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToMono(scienceFictionObject)
				.block();		
	}

	private Long countReminder() {
		return this.getMono(countEndpoint, Long.class);
	}
	
	private int getRandomId(Long max) {
		return ThreadLocalRandom.current()
				.nextInt(minInteger, 
						max.intValue() + 1);
	}
	
	public void setPhraseEndpoint(String phraseEndpoint) {
		this.phraseEndpoint = phraseEndpoint;
	}

	public void setCountEndpoint(String countEndpoint) {
		this.countEndpoint = countEndpoint;
	}

	public void setMinInteger(Integer minInteger) {
		this.minInteger = minInteger;
	}
}
