package project.grinder.service.transform;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Override
    public Map<Integer, Summary> summarizeRecord(Map<String, List<Record>> records) {
        List<Record> recordList = new LinkedList<Record>();
        for (Map.Entry<String, List<Record>> entry : records.entrySet()) {
            recordList.addAll(entry.getValue().stream().map(r -> {
                r.setDate(entry.getKey());
                return r;
            }).collect(Collectors.toList()));
        }

        Map<Integer, Summary> summary =  recordList.stream().collect(
            Collectors.toMap(
                r -> r.getId(),
                r ->  new Summary(new User(r.getId(), r.getName(), r.getPhone()), new HashMap<LocalDate, List<Order>>(), 0.0),
                (exist, record) -> exist
            )
        );
        
        recordList.forEach(r -> transformOrder(r, summary.get(r.getId())));
        return summary;
    }

    private void transformOrder(Record record, Summary summary) {
        Map<LocalDate, List<Order>> orderMap = summary.getOrder();
        LocalDate date = LocalDate.parse(record.getDate(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        Order order = new Order(record.getItem(), record.getAmount(), record.getPrice());
        if (orderMap.containsKey(date)) {
            orderMap.get(date).add(order);
        } else {
            orderMap.put(date, Stream.of(order).collect(Collectors.toList()));
        }
        summary.setTotalAmount(summary.getTotalAmount() + (record.getPrice() * record.getAmount()));
    }

    private void transformJson(String key, Object value, Map <String, Object> result) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = Map.class.cast(value);
            map.forEach((k, v) -> transformJson(key + Constant.JSON_KEY_DELIMITER + k, v, result));

        } else if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = List.class.cast(value);
            IntStream.range(0, list.size()).forEach(idx -> transformJson(key + Constant.JSON_KEY_DELIMITER + idx, list.get(idx), result));

        } else {
            result.put(key, value);
        }
    }
}