package model;

import java.util.ArrayList;
import java.util.List;

public class Invocation {
	List<Method> canditates;
	
	public Invocation () {
		canditates = new ArrayList<Method>();
	}
	
	
	
	public void addCanditates(Method met) {
		canditates.add(met);
	}
}
