package IA;

import java.io.*;
import java.util.*;

public class MoteurInference {
	//Main de test
	/* 
	public static void main(String[] args) {
		MoteurInference test = new MoteurInference();
		System.out.println(test.estSolution("premierCoup"));
	}*/
	
	/************************Attributs*************************/
	private static MoteurInference instance;
	private final String rulesFilePath = "IA/regles/Battleship.rules";
	private ArrayList<Regle> listeRegles = new ArrayList<Regle>();
	private HashSet<String> faitsCourants = new HashSet<String>();
	private int[] dernierCoup = new int[2];
	private int[] avantDernierCoup = new int[2];
	private String consequence;
	
	/**********************Constructeur************************/
	private MoteurInference(){
		ArrayList<String> data = deserialiserFichierRegle();
		for(String s : data){
			listeRegles.add(new Regle(s));
		}
		
		faitsCourants.add("premierCoup");
	}
	
	
	/************************Methodes**************************/
	public int[] calculCoup(char[][] censureGrille){
		
		...
		
		// Application de la regle
		switch(consequence) {
			case "mode(Recherche)":
				// Appel fonction mode recherche
				break;
		}
		
		// Marquer la regle
		
		return new int[2];
	}
	
	
	public static MoteurInference getInstance(){
		if(instance == null){
			instance = new MoteurInference();
		}
		return instance;
	}
	
	private boolean estSolution(String fait){
		return !faitsCourants.contains(fait);
	}
	
	private boolean CoupReussi(int[] coup, char[][] censureGrille){
		assert censureGrille[coup[1]][coup[2]] != 'v' : "Vous etes un cave";
		return censureGrille[coup[1]][coup[2]] != 'o'; 
	}
	
	private ArrayList<String> deserialiserFichierRegle(){
		ArrayList<String> data = new ArrayList<String>();
		Scanner scanner;
		
		try {
			scanner = new Scanner(new File(rulesFilePath));
			
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				data.add(line);
			}
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Fichier "+rulesFilePath+" non trouve : " 
					+ e.getStackTrace()[0]);
		}		
		
		return data;
	}
}
