package project.grinder.service.transform;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import project.grinder.Constant;
import project.grinder.model.Order;
import project.grinder.model.Record;
import project.grinder.model.Summary;
import project.grinder.model.User;

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
            map.forEach((k, v) -> transformJson(key + Constant.JSON_KEY_DELIMITER + k, v, result));

        } else if (value instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            ArrayList<Object> list = ArrayList.class.cast(value);
            IntStream.range(0, list.size()).forEach(idx -> transformJson(key + Constant.JSON_KEY_DELIMITER + idx, list.get(idx), result));

        } else {
            result.put(key, value);
        }
    }

    @Override
    public Map<Integer, Summary> summarizeRecord(Map<String, List<Record>> records) {
        List<Record> rec = new LinkedList<Record>();
        for (Map.Entry<String, List<Record>> entry : records.entrySet()) {
            rec.addAll(entry.getValue().stream().map(r -> {
                r.setDate(entry.getKey());
                return r;
            }).collect(Collectors.toList()));
        }

        Map<Integer, Summary> summary =  rec.stream().collect(
            Collectors.toMap(
                r -> r.getId(),
                r ->  new Summary(new User(r.getId(), r.getName(), r.getPhone()), new ArrayList<Order>(), 0.0),
                (exist, record) -> exist
            )
        );
        
        rec.forEach(r -> {
            Summary sum = summary.get(r.getId());
            sum.getOrder().add(new Order(r.getItem(), r.getAmount(), r.getPrice(), LocalDate.parse(r.getDate(), DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
            sum.setTotalAmount(sum.getTotalAmount() + (r.getPrice() * r.getAmount()));
        });

        return summary;
    }
}