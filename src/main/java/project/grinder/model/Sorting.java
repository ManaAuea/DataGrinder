package project.grinder.model;

import java.util.List;

public class Sorting {

    private List<Integer> data;

    public Sorting() {}

    public Sorting(List<Integer> data) {
		this.data = data;
    }
    
    public List<Integer> getData() {
		return data;
    }
    
    public void setData(List<Integer> data) {
		this.data = data;
	}
}