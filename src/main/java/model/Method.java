package model;

import java.util.ArrayList;
import java.util.List;

public class Method extends OBE implements IArtefact{

	List<LocalVariable> variablesLocales;
	List<Signature> signatures;
	List<Invocation> invocations;
	
	public Method(String nom) {
		super(nom);
		variablesLocales = new ArrayList<LocalVariable>();
		signatures = new ArrayList<Signature>();
		invocations = new ArrayList<Invocation>();
	}
	
	
	public void addLocalVariable(LocalVariable localVar) {
		variablesLocales.add(localVar);
	}
	
	public void addSignature(Signature sign) {
		signatures.add(sign);
	}
	
	public void invocations(Invocation invo) {
		invocations.add(invo);
	}
	
	@Override
	public int getIdentifiant() {
		return getNom().hashCode();
	}
}
