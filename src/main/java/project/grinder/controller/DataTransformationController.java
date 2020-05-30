package project.grinder.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import project.grinder.service.transform.DataTransformationService;

@RestController
public class DataTransformationController {

    @Autowired
    DataTransformationService dataTransformationService;

    @PostMapping("/flatten")
    public Map<String, Object> flatten(@RequestBody Map<String, Object> json) {
		return dataTransformationService.flattenJson(json);
    }
    
}