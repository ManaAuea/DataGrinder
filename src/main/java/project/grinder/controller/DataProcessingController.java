package project.grinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.grinder.model.Sorting;
import project.grinder.model.User;
import project.grinder.model.Validation;
import project.grinder.service.process.DataProcessingService;

@RestController
public class DataProcessingController {

	private final DataProcessingService dataProcessingService;
	
	public DataProcessingController(DataProcessingService dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    @PostMapping("/sort")
	public Sorting sort(@RequestBody Sorting sorting) {
    	List<Integer> sorted = dataProcessingService.sortInteger(sorting.getData());
    	return new Sorting(sorted);
    }

    @PostMapping("/validate")
	public Validation validate(@RequestBody User user) {
		return dataProcessingService.validateUser(user);
    }
}