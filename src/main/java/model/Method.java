package model;

import java.util.ArrayList;
import java.util.List;

public class Method extends OBE implements IArtefact{

	List<LocalVariable> variablesLocales;
	List<Signature> signatures;
	List<Invocation> invocations;
	List<Access> access;
	Class classeMere;
	
	public Method(String nom) {
		super(nom);
		variablesLocales = new ArrayList<LocalVariable>();
		signatures = new ArrayList<Signature>();
		invocations = new ArrayList<Invocation>();
		access = new ArrayList<Access>();
	}
	
	
	public void addLocalVariable(LocalVariable localVar) {
		variablesLocales.add(localVar);
	}
	
	public void addSignature(Signature sign) {
		signatures.add(sign);
	}
	
	public void addInvocations(Invocation invo) {
		invocations.add(invo);
	}
	
	@Override
	public int getIdentifiant() {
		return getNom().hashCode();
	}
	
	public void addAcces(Access a) {
		this.access.add(a);
	}


	public Class getClasseMere() {
		return classeMere;
	}


	public void setClasseMere(Class classeMere) {
		this.classeMere = classeMere;
	}


	public List<Invocation> getInvocations() {
		return invocations;
	}


	public void setInvocations(List<Invocation> invocations) {
		this.invocations = invocations;
	}
	
	
	
	
}
