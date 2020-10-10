package model;

public class Attribute extends OBE implements IArtefact{

	public Attribute(String nom) {
		super(nom);
	}

	@Override
	public int getIdentifiant() {
		return getNom().hashCode();
	}
}
