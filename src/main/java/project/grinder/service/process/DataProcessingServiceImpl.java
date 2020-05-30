package project.grinder.service.process;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import project.grinder.Constant;
import project.grinder.model.Sorting;
import project.grinder.model.User;
import project.grinder.model.Validation;

@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    @Override
    public Sorting sortInteger(List<Integer> list) {
        return new Sorting(bubbleSort(list, true));
    }

    @Override
    public Validation validateUser(User user) {
        Validation validation = new Validation(true, new ArrayList<String>(), user);
        Field[] fields = user.getClass().getDeclaredFields();
        for(Field field: fields) {
            switch (field.getName()) {
                case "id":
                    validation = validateId(validation);
                    break;
                case "name":
                    validation = validateName(validation);
                    break;
                case "phone":
                    validation = validatePhone(validation);
                    break;
                default:
                    break;
            }
        }
        return validation;
    }

    // Bubble sort with recursive behavior
    private List<Integer> bubbleSort(List<Integer> list, boolean sort) {
        if (sort) {
            sort = false;
            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i) > list.get(i + 1)) {
                    Collections.swap(list, i, i + 1);
                    sort = true;
                }
            }
            return bubbleSort(list, sort);
        } else {
            return list;
        }
    }

    private Validation validateId(Validation validation) {
        int id = validation.getSuggestion().getId();
        if (id < 0) {
            validation.setValid(false);
            validation.getErrors().add(Constant.ID_ERROR_MSG);
            validation.getSuggestion().setId(Math.abs(id));
        }
        return validation;
    }

    private Validation validateName(Validation validation) {
        String name = validation.getSuggestion().getName();
        if (!Pattern.matches("[A-Z][a-z]* [A-Z][a-z]*", name)) {
            validation.setValid(false);
            validation.getErrors().add(Constant.NAME_ERROR_MSG);

            StringBuffer result = new StringBuffer();
            List<String> filtered = Arrays.asList(name.trim().split(" ")).stream().filter(i -> !isTitleName(i)).collect(Collectors.toList());
            for (String n: filtered) {
                result.append(result.length() > 0 ? " " : "");
                result.append(Character.toString(n.charAt(0)).toUpperCase());
                result.append(n.substring(1).toLowerCase());  
            }
            validation.getSuggestion().setName(result.toString());
        }
        return validation;
    }

    private Validation validatePhone(Validation validation) {
        String phone = validation.getSuggestion().getPhone();
        if (!Pattern.matches("[0-9]{10}", phone)) {
            validation.setValid(false);
            validation.getErrors().add(Constant.PHONE_ERROR_MSG);
            validation.getSuggestion().setPhone(phone.replaceAll("[^0-9]", "").substring(0, 10));
        }
        return validation;
    }

    // Not cover all cases
    private boolean isTitleName(String name) {
        return Pattern.matches("[A-Z][a-z]{1,2}[.]", name);
    }
    
}