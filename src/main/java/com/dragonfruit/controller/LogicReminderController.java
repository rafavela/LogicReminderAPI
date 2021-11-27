package com.dragonfruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dragonfruit.bean.ReminderBean;
import com.dragonfruit.service.LogicReminderService;

@RestController
@RequestMapping(path="LogicReminder/v1",produces = "application/json")
public class LogicReminderController {

	@Autowired
	LogicReminderService logicReminderService;
	
	@GetMapping
	public ReminderBean doGet(){
		return logicReminderService.getReminderBean();
	}

	@PostMapping
	public ReminderBean saveReminder(@RequestBody ReminderBean reminderBean) {
		return logicReminderService.saveReminderBean(reminderBean);
	}
}
