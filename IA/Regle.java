package IA;

import java.util.ArrayList;
import java.util.Arrays;

public class Regle {
	private ArrayList<String> premisses;
	private ArrayList<String> actions;
	
	public static void main(String[] args) {
		Regle regle = new Regle("mode(Destruction),coupReussi,!bateau(coule)"
				+ "=>continuerDirection");
		System.out.println(regle);
		System.out.println(regle.satisfaitCondition("coupReussi"));		// true
		System.out.println(regle.satisfaitConditions(
				"mode(Destruction)","coupReussi"));						// false
		System.out.println(regle.satisfaitConditions(
				"mode(Destruction)","coupReussi","!bateau(coule)"));	// true
	}
	
	public Regle(String data) {		
		String[] regleDecomposee = data.split("=>");
		premisses = new ArrayList<String>(Arrays.asList(
				regleDecomposee[0].split(",")));
		actions = new ArrayList<String>(Arrays.asList(
				regleDecomposee[1].split(",")));
	}
	
	/**
	 * Permet de savoir si un fait correspond à un prémisse d’une règle
	 * @param fait
	 * @return booléen
	 */
	public boolean satisfaitCondition(String fait) {
		return premisses.contains(fait);
	}
	
	/**
	 * Permet de savoir si un ensemble de faits satisfait tous les prémisses 
	 * d’une règle
	 * @param faits
	 * @return booléen
	 */
	public boolean satisfaitConditions(String... faits) {
		return Arrays.asList(faits).containsAll(premisses);
	}

	@Override
	public String toString() {
		String regleString = "";
		for(String s : premisses)
			regleString+=s;
		for(String s : actions)
			regleString+=s;
		return regleString;
	}
}
