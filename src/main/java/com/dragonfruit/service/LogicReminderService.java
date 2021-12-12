package com.dragonfruit.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
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
	@Setter	
	private String fallBackMeaning;
	@Setter	
	private String fallbackExpression;
	
	@Autowired
	private WebClient.Builder webClientBuilder;	
	
	private final ReactiveCircuitBreaker reminderCircuitBreaker;	

	public LogicReminderService(ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
		this.reminderCircuitBreaker = circuitBreakerFactory.create("reminder");
	}	
	
	public ReminderBean getReminderBean() {	
		Long count = this.countReminder();
		int randomId=this.getRandomId(count);
		ReminderBean reminderBean=reminderCircuitBreaker.run(
				this.getMono(phraseEndpoint+randomId, ReminderBean.class),
				reminderFallback)
				.block();
		
		return reminderBean;
	}
	
	public ReminderBean saveReminderBean(ReminderBean reminderBean) {
		return this.saveMono(saveEndpont, reminderBean);
	}
	
	private <T> Mono<T> getMono(String uri,Class<T> responseType) {
		return webClientBuilder.build()
				.get()
				.uri(uri)
				.retrieve()
				.bodyToMono(responseType);		
	}
	
	private Function<Throwable, Mono<Long>> countFallback=throwable->{
		return Mono.just(1L);		
	};

	private Function<Throwable, Mono<ReminderBean>> reminderFallback=throwable->{
		ReminderBean reminderBean=new ReminderBean(-1L, fallbackExpression, fallBackMeaning);
		return Mono.just(reminderBean);		
	};	
	
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
		return reminderCircuitBreaker.run(
				this.getMono(countEndpoint, Long.class), 
				countFallback)
				.block();
	}
	
	private int getRandomId(Long max) {
		return ThreadLocalRandom.current()
				.nextInt(minInteger, 
						max.intValue() + 1);
	}
}
