package project.grinder.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.grinder.model.Record;
import project.grinder.model.Summary;
import project.grinder.service.transform.DataTransformationService;

@RestController
public class DataTransformationController {

	private final DataTransformationService dataTransformationService;
	
	public DataTransformationController(DataTransformationService dataTransformationService) {
        this.dataTransformationService = dataTransformationService;
    }

    @PostMapping("/flatten")
    public Map<String, Object> flatten(@RequestBody Map<String, Object> json) {
		return dataTransformationService.flattenJson(json);
    }

    @PostMapping("/summarize")
    public Map<Integer, Summary> summarize(@RequestBody Map<String, List<Record>> records) {
		return dataTransformationService.summarizeRecord(records);
    }
    
}