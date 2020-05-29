package project.grinder.service.process;

import java.util.List;

import project.grinder.model.Sorting;
import project.grinder.model.User;
import project.grinder.model.Validation;

public interface DataProcessingService {

    public abstract Sorting sortInteger(List<Integer> list);

    public abstract Validation validateUser(User user);
}