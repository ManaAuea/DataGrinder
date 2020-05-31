package project.grinder.service.process;

import java.util.List;

import project.grinder.model.User;
import project.grinder.model.Validation;

public interface DataProcessingService {

    public abstract List<Integer> sortInteger(List<Integer> list);

    public abstract Validation validateUser(User user);
}