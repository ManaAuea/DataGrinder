package project.grinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.grinder.model.Sorting;
import project.grinder.model.User;
import project.grinder.model.Validation;
import project.grinder.service.process.DataProcessingService;

@RestController
public class DataProcessingController {

    @Autowired
    DataProcessingService dataProcessingService;

    @PostMapping("/sort")
	public Sorting sort(@RequestBody Sorting sorting) {
		return dataProcessingService.sortInteger(sorting.getData());
    }

    @PostMapping("/validate")
	public Validation validate(@RequestBody User user) {
		return dataProcessingService.validateUser(user);
    }
}