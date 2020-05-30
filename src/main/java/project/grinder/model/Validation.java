package project.grinder.model;

import java.util.List;

public class Validation {
    
    private boolean valid;
    private List<String> errors;
    private User user;

    public Validation(boolean valid, List<String> errors, User user) {
        this.valid = valid;
        this.user = user;
        this.errors = errors;
    }

    public boolean getValid() {
		return valid;
    }
    
    public void setErrors(List<String> errors) {
		this.errors = errors;
    }

    public List<String> getErrors() {
		return errors;
    }
    
    public void setValid(boolean valid) {
		this.valid = valid;
    }

    public User getSuggestion() {
		return user;
    }
    
    public void setSuggestion(User user) {
		this.user = user;
    }
}