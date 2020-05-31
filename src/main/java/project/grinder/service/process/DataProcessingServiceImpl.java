package project.grinder.service.process;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import project.grinder.Constant;
import project.grinder.model.User;
import project.grinder.model.Validation;

@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    final Predicate<String> nullOrEmptyString = str -> str == null || str.trim().isEmpty();

    @Override
    public List<Integer> sortInteger(List<Integer> list) {
        return bubbleSort(list, true);
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
        if (id < 1) {
            assignError(validation, false, Constant.ID_ERROR_MSG);
            validation.getSuggestion().setId(Math.abs(id)); 
        }
        return validation;
    }

    private Validation validateName(Validation validation) {
        String name = validation.getSuggestion().getName();
        if (nullOrEmptyString.test(name) ) {
            assignError(validation, false, Constant.NAME_ERROR_MSG);

        } else if (!Pattern.matches("[A-Z][a-z]* [A-Z][a-z]*", name)) {
            assignError(validation, false, Constant.NAME_ERROR_MSG);

            StringBuffer result = new StringBuffer();
            AtomicInteger index = new AtomicInteger();
            String[] array = name.trim().split(" ");
            IntStream.range(0, array.length).forEach(idx -> {
                if (!isTitleName(array[idx]) && index.getAndIncrement() < 2) {
                    result.append(result.length() > 0 ? " " : "");
                    result.append(Character.toString(array[idx].charAt(0)).toUpperCase());
                    result.append(array[idx].substring(1).toLowerCase());  
                }
            });
            validation.getSuggestion().setName(result.toString());
        }
        return validation;
    }

    private Validation validatePhone(Validation validation) {
        String phone = validation.getSuggestion().getPhone();
        if (nullOrEmptyString.test(phone) ) {
            assignError(validation, false, Constant.PHONE_ERROR_MSG);

        } else if (!Pattern.matches("[0-9]{10}", phone)) {
            assignError(validation, false, Constant.PHONE_ERROR_MSG);
            phone = phone.replaceAll("[^0-9]", "");
            validation.getSuggestion().setPhone(phone.substring(0, phone.length() < 10 ? phone.length() : 10));
        }
        return validation;
    }

    // Not cover all cases
    private boolean isTitleName(String name) {
        return Pattern.matches("[A-Z][a-z]{1,2}[.]", name);
    }

    private Validation assignError(Validation validation, boolean valid, String error) {
        validation.setValid(valid);
        validation.getErrors().add(error);
        return validation;
    } 
}