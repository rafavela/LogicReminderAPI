package com.dragonfruit.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReminderBean {	
	private Long reminderId;
	private String englishExpression;
	private String englishMeaning;
}
