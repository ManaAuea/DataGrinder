package project.grinder.service.transform;

import java.util.List;
import java.util.Map;

import project.grinder.model.Record;
import project.grinder.model.Summary;

public interface DataTransformationService {

    public abstract Map<String, Object> flattenJson(Map<String, Object> json);

    public abstract Map<Integer, Summary> summarizeRecord(Map<String, List<Record>> records);
    
}