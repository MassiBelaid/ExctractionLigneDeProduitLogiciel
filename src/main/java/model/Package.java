package model;

import java.util.ArrayList;
import java.util.List;

public class Package extends OBE{

	List<Interface> interfaces;
	List<Class> classes;
	
	public Package(String nom) {
		super(nom);
		interfaces = new ArrayList<Interface>();
		classes = new ArrayList<Class>();
	}
	
	
	public void addCLass(Class cls) {
		this.classes.add(cls);
	}
	
	public void addInterface(Interface inf) {
		this.interfaces.add(inf);
	}
}
