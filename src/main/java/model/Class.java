package model;

import java.util.ArrayList;
import java.util.List;

public class Class extends OBE implements IArtefact{
	List<Inheritance> sousClasses;
	List<Attribute> attributs;
	List<Method> methodes;
	
	public Class(String nom) {
		super(nom);
		sousClasses = new ArrayList<Inheritance>();
		attributs = new ArrayList<Attribute>();
		methodes = new ArrayList<Method>();
	}
	
	
	public void addSousClasse(Inheritance inh) {
		sousClasses.add(inh);
	}
	
	public void addAttribut(Attribute attr) {
		attributs.add(attr);
	}
	
	public void addMethode(Method met) {
		methodes.add(met);
	}


	@Override
	public int getIdentifiant() {
		return getNom().hashCode();
	}
}
