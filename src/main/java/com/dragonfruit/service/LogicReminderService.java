package com.dragonfruit.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dragonfruit.bean.ReminderBean;

import lombok.Setter;
import reactor.core.publisher.Mono;

@Service
@ConfigurationProperties(prefix="reminder")
public class LogicReminderService {
	
	@Setter
	private String phraseEndpoint;
	@Setter	
	private String countEndpoint;
	@Setter	
	private Integer minInteger;
	@Setter
	private String saveEndpont;
	
	@Autowired
	private WebClient.Builder webClientBuilder;	
	
	public ReminderBean getReminderBean() {	
		Long count = this.countReminder();
		int randomId=this.getRandomId(count);
		ReminderBean reminderBean=this.getMono(phraseEndpoint+randomId, ReminderBean.class);
		
		return reminderBean;
	}
	
	public ReminderBean saveReminderBean(ReminderBean reminderBean) {
		return this.saveMono(saveEndpont, reminderBean);
	}
	
	private <T> T getMono(String uri,Class<T> responseType) {
		return webClientBuilder.build()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToMono(responseType)
				.block();		
	}

	private ReminderBean saveMono(String uri,ReminderBean reminderBean){
		System.out.println("reminderBean "+reminderBean);
		return webClientBuilder.build()
				.post()
				.uri(uri)
				.body(Mono.just(reminderBean),ReminderBean.class)
				.retrieve()
				.bodyToMono(ReminderBean.class)
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
}
