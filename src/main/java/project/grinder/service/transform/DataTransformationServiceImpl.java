package project.grinder.service.transform;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

@Service
public class DataTransformationServiceImpl implements DataTransformationService {

    @Override
    public Map<String, Object> flattenJson(Map<String, Object> json) {
        Map <String, Object> flatJson = new LinkedHashMap<String, Object>();
        json.forEach((key, val) -> transformJson(key, val, flatJson));
        return flatJson;
    }

    private void transformJson(String key, Object value, Map <String, Object> result) {
        if (value instanceof LinkedHashMap) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = LinkedHashMap.class.cast(value);
            map.forEach((k, v) -> transformJson(key + '_' + k, v, result));

        } else if (value instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<Object> list = ArrayList.class.cast(value);
            IntStream.range(0, list.size()).forEach(idx -> transformJson(key + '_' + idx, list.get(idx), result));

        } else {
            result.put(key, value);
        }
    }
}