	package model;

import java.util.ArrayList;
import java.util.List;

public class Access {

	List<Attribute> accessible;
	List<Method> accedeA;
	
	public Access() {
		accessible = new ArrayList<Attribute>();
		accedeA = new ArrayList<Method>();
	}
	
	public void addAcce(Attribute attr, Method met) {
		this.accessible.add(attr);
		this.accedeA.add(met);
	}
}
