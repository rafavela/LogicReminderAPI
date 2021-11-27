package com.dragonfruit.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReminderBean {	
	private Long reminderId;
	private String englishExpression;
	private String englishMeaning;
}
