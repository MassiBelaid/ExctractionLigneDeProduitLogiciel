package analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Attribute;
import model.Class;
import model.IArtefact;
import model.Interface;
import model.LocalVariable;
import model.Method;
import model.OBE;
import model.Package;
import model.Signature;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.visitor.filter.TypeFilter;

public class SpoonMain {

	public static void main(String[] args) {
		
		System.out.println("Begin Analysis");

		// Parsing arguments using JCommander
		Arguments arguments = new Arguments();
		boolean isParsed = arguments.parseArguments(args);

		// if there was a problem parsing the arguments then the program is terminated.
		if(!isParsed)
			return;
		
		// Parsed Arguments
		String experiment_source_code = arguments.getSource();
		String experiment_output_filepath = arguments.getTarget();
		
		// Load project (APP_SOURCE only, no TEST_SOURCE for now)
		Launcher launcher = null;
		if(arguments.isMavenProject() ) {
			launcher = new MavenLauncher(experiment_source_code, MavenLauncher.SOURCE_TYPE.APP_SOURCE); // requires M2_HOME environment variable
		}else {
			launcher = new Launcher();
			launcher.addInputResource(experiment_source_code + "/sources");
		}
		
		// Setting the environment for Spoon
		Environment environment = launcher.getEnvironment();
		environment.setCommentEnabled(true); // represent the comments from the source code in the AST
		environment.setAutoImports(true); // add the imports dynamically based on the typeReferences inside the AST nodes.
//		environment.setComplianceLevel(0); // sets the java compliance level.
		
		System.out.println("Run Launcher and fetch model.");
		launcher.run(); // creates model of project
		CtModel model = launcher.getModel(); // returns the model of the project

		//List<CtPackage> pacList = model.getElements(new TypeFilter<CtPackage>(CtPackage.class));
		
		List<CtClass> classList = model.getElements(new TypeFilter<CtClass>(CtClass.class));
		
		Map<String, Package> packagelist = new HashMap<String, Package>();
		Map<String, Method> methMap = new HashMap<String, Method>();
		Map<String, CtInterface> interfaceMap = new HashMap<String, CtInterface>();
		
		List<OBE> listOBE = new ArrayList<OBE>();
		
		for(CtClass cls : classList) {
			//System.out.println(cls.getSimpleName());
			CtPackage pac = cls.getPackage();
			Package myPac;
			Class myClass = new Class(cls.getSimpleName());
			
			try {
				
				//Verification si package deja ajouté
				if(packagelist.containsKey(pac.getSimpleName())) {
					myPac = packagelist.get(pac.getSimpleName());
				}else {
					//Sinon création du package
					myPac = new Package(pac.getSimpleName());
					packagelist.put(pac.getSimpleName(), myPac);
					listOBE.add(myPac);
				}
				//Ajout de la classe dans le package
				myPac.addCLass(myClass);
				listOBE.add(myClass);
				
				//traitement des méthodes de la classe
				Set<CtMethod> methods = cls.getMethods();
				for(CtMethod method : methods) {
					Method myMethod = null;
					
					//Verification si méthode deja ajoutée
					if(methMap.containsKey(method.getSimpleName())) {
						myMethod = methMap.get(method.getSimpleName());
					}else {
						myMethod = new Method(method.getSimpleName());
						methMap.put(method.getSimpleName(), myMethod);
						listOBE.add(myMethod);
					}
					
					
					//Ajout de la signature
					myClass.addMethode(myMethod);
					Signature mySignature = new Signature();
					method.getSignature();
					myMethod.addSignature(mySignature);
					
					
					//Variable locales dans une méthode
					for(CtVariable variable : method.getElements(new TypeFilter<CtVariable>(CtVariable.class))) {
						//System.out.println(cls.getSimpleName()+" : "+method.getSimpleName()+" : "+variable.getSimpleName());
						LocalVariable myVariable = new LocalVariable(variable.getSimpleName());
						myMethod.addLocalVariable(myVariable);
						listOBE.add(myVariable);
						}
					
					
				}
				
				
				
				
				//Attribut d'une classe
				for(CtFieldReference attribut : cls.getDeclaredFields()) {
					//System.out.println(cls.getSimpleName()+" : "+attribut.getSimpleName());
					Attribute myAttribut = new Attribute(attribut.getSimpleName());
					myClass.addAttribut(myAttribut);
					listOBE.add(myAttribut);
				}
				
				
			}catch (NullPointerException e) {
				System.out.println("Package of classe : "+cls.getSimpleName()+" not added");
			}
		}
		
		
		//Ajout des interface dans le package
		List<CtInterface> interfaceList = model.getElements(new TypeFilter<CtInterface>(CtInterface.class));
		for(CtInterface interf : interfaceList) {
			CtPackage pac = interf.getPackage();
			Package myPac;
			Interface myInterface = new Interface(interf.getSimpleName());
			
			//Verification si package deja ajouté
			if(packagelist.containsKey(pac.getSimpleName())) {
				myPac = packagelist.get(pac.getSimpleName());
			}else {
				//Sinon création du package
				myPac = new Package(pac.getSimpleName());
				packagelist.put(pac.getSimpleName(), myPac);
			}
			//Ajout de la classe dans le package
			myPac.addInterface(myInterface);
			listOBE.add(myInterface);
		}
		
		
		
		for(IArtefact arte : getListArtefact(listOBE) ) {
			System.out.println(arte.getNom()+" : "+arte.getIdentifiant());
		}
		
		//System.out.println(packagelist.size());
		System.out.println("FINISH.");
		
	}
	
	
	
	
	//Renvoie liste des Artefacts
	public static ArrayList<IArtefact> getListArtefact(List<OBE> listOBe){
		ArrayList<IArtefact> listRetour = new ArrayList<IArtefact>();
		
		for(OBE obe : listOBe) {
			if(obe instanceof IArtefact) {
				listRetour.add((IArtefact)obe);
			}
		}
		
		return listRetour;
	}
}

