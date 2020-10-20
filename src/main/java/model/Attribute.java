package model;

import java.util.ArrayList;
import java.util.List;

public class Attribute extends OBE implements IArtefact{
	
	List<Access> access;

	public Attribute(String nom) {
		super(nom);
		access = new ArrayList<Access>();
	}

	@Override
	public int getIdentifiant() {
		return getNom().hashCode();
	}
	
	public void addAcces(Access a) {
		this.access.add(a);
	}
	
	public List<Access> getAccess() {
		return this.access;
	}
}
