package com.dragonfruit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="LogicReminder/v1")
public class LogicReminderController {

	@GetMapping
	public String doGet(){
		return "test";
	}
}
