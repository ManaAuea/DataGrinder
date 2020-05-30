package project.grinder.service.transform;

import java.util.Map;

public interface DataTransformationService {

    public abstract Map<String, Object> flattenJson(Map<String, Object> json);
    
}