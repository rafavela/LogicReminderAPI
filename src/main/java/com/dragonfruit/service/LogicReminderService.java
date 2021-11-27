package com.dragonfruit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.dragonfruit.bean.ReminderBean;

@Service
public class LogicReminderService {
	
	@Autowired
	private WebClient.Builder webClientBuilder;	
	
	public ReminderBean getReminderBean() {		
		ReminderBean reminderBean=this.getMono("http://localhost:8082/reminder/v1?id=1", ReminderBean.class);
		
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
}
