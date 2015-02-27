package IA;

import java.util.ArrayList;
import java.util.Arrays;

public class Regle {
	
//	main de test
	
//	public static void main(String[] args) {
//		Regle regle = new Regle("mode(Destruction),coupReussi,!bateau(coule)"
//				+ "=>continuerDirection");
//		System.out.println(regle);
//		System.out.println(regle.satisfaitCondition("coupReussi"));		// true
//		System.out.println(regle.satisfaitConditions(
//				"mode(Destruction)","coupReussi"));						// false
//		System.out.println(regle.satisfaitConditions(
//				"mode(Destruction)","coupReussi","!bateau(coule)"));	// true
//	}
	
	/************************Attributs*************************/
	private ArrayList<String> premisses;
	private String actions;
	
	/**********************Constructeur************************/
	public Regle(String data) {		
		String[] regleDecomposee = data.split("=>");
		premisses = new ArrayList<String>(Arrays.asList(
				regleDecomposee[0].split(",")));
		actions = (regleDecomposee[1]);
	}
	
	/************************Methodes**************************/
	/**
	 * Permet de savoir si un fait correspond � un pr�misse d�une r�gle
	 * @param fait
	 * @return bool�en
	 */
	public boolean satisfaitCondition(String fait) {
		return premisses.contains(fait);
	}
	
	/**
	 * Permet de savoir si un ensemble de faits satisfait tous les pr�misses 
	 * d�une r�gle
	 * @param faits
	 * @return bool�en
	 */
	public boolean satisfaitConditions(String... faits) {
		return Arrays.asList(faits).containsAll(premisses);
	}

	@Override
	public String toString() {
		String regleString = "";
		for(String s : premisses)
			regleString+=s;
			regleString+=actions;
		return regleString;
	}

    public ArrayList getPremisses(){ return this.premisses;}
    public String getActions(){ return this.actions;}
}
