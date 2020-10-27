package analyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Access;
import model.Attribute;
import model.Class;
import model.IArtefact;
import model.Inheritance;
import model.Interface;
import model.Invocation;
import model.LocalVariable;
import model.Method;
import model.OBE;
import model.Package;
import model.Signature;
import spoon.Launcher;
import spoon.MavenLauncher;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtInterface;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtVariable;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
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
		Map<String, Class> mapClass = new HashMap<String, Class>();
		Map<String, Attribute> mapAttributs = new HashMap<String, Attribute>();
		List<Invocation> listInvocs = new ArrayList<Invocation>(); 
		
		
		List<OBE> listOBE = new ArrayList<OBE>();
		
		
		for(CtClass cls : classList) {
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
				
				//Ajout de la classe dans la liste des OBE
				listOBE.add(myClass);
				mapClass.put(cls.getSimpleName(), myClass);
				
				
				//Traitement de la super Classe
				Class classSup = null;
				if(cls.getSuperclass() != null) {
					if(!mapClass.containsKey(cls.getSuperclass().toString())) {
						classSup = new Class(cls.getSuperclass().toString());
						mapClass.put(cls.getSuperclass().toString(), classSup);
						listOBE.add(classSup);
					}else {
						classSup = mapClass.get(cls.getSuperclass().toString());
					}
					
					Inheritance myHeritage = new Inheritance();
					myHeritage.setSousClasse(myClass);
					myHeritage.setSuperClasses(classSup);
					
					myClass.addSuperClasse(myHeritage);
					classSup.addSousClasse(myHeritage);
				}
				
					
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
						myMethod.setClasseMere(myClass);
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
						mapAttributs.put(myAttribut.getNom(), myAttribut);
						listOBE.add(myAttribut);
					}
					
				
			}catch (NullPointerException e) {
				System.out.println("Package of classe : "+cls.getSimpleName()+" not added");
			}
		}
		
		
		
		for(CtClass cls : classList) {
			Set<CtMethod> methods = cls.getMethods();
			for(CtMethod method : methods) {
				for(CtInvocation invocation : method.getElements(new TypeFilter<CtInvocation>(CtInvocation.class))) {
					String mthName = invocation.toString();
					
					String[ ] mthNames = mthName.split("\\.");
					if(!mthName.equals("") && mthNames.length < 3 && methMap.containsKey(method.getSimpleName())) {
						if(mthNames.length > 1) {
							mthName = mthNames[1];
						}else if(mthNames.length > 0){
							mthName = mthNames[0];
						}
						mthName = mthName.split("\\(")[0];
						//System.out.println(method.getSimpleName()+"  =>  "+mthName);
						
						
						
						Method myMethodecandidate = null;
						if(!methMap.containsKey(mthName)) {
							myMethodecandidate = new Method(mthName);
							methMap.put(mthName, myMethodecandidate);
							listOBE.add(myMethodecandidate);
						}else{
							myMethodecandidate = methMap.get(mthName);
						}
						
						Invocation myInvocation = new Invocation();
						myInvocation.setAppellante(myMethodecandidate);
						
						Method myMethod = methMap.get(method.getSimpleName());
						myInvocation.setAppellee(myMethod);
						myMethod.addInvocations(myInvocation);
						listInvocs.add(myInvocation);
						//System.out.println("Invocation : "+method.getSimpleName()+" ==> "+mthName);
						
					}else {
						//System.out.println("problème avec === "+method.getSimpleName());
					}

					
				}
				for(CtReference acces : method.getElements(new TypeFilter<CtReference>(CtReference.class))) {
					//System.out.println(acces.getSimpleName());
					if(mapAttributs.containsKey(acces.getSimpleName()) && methMap.containsKey(method.getSimpleName())) {
						Attribute myAttribut = mapAttributs.get(acces.getSimpleName());
						Access myAcces = new Access();
						myAcces.setAttr(myAttribut);
						myAcces.setMet(methMap.get(method.getSimpleName()));
					}
				}
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
		
		
		
		for(Class cls : mapClass.values()) {
			for(Attribute attr : cls.getAttribute()) {
				for(Access acc : attr.getAccess()) {
					Method mm = acc.getMet();
					System.out.println(cls.getNom()+"   =>   "+mm.getClasseMere().getNom());
					
				}
			}
		}
		
		for(Invocation inv : listInvocs) {
			System.out.println("Invocation : "+inv.getAppellante().getNom()+" ==> "+inv.getAppellee().getNom());
		}
		
		for(Attribute atr : mapAttributs.values()) {
			//System.out.println();
		}
		
		
		
		
		
		System.out.println("\n \n ========================================================== \n");
		
		for(IArtefact arte : getListArtefact(listOBE) ) {
			System.out.println(arte.getClass()+" ( "+arte.getNom()+" )");
		}
		
		
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

