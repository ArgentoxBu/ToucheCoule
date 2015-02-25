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
	private final String rulesFilePath = "IA/regles/Battleship.rules";
	private ArrayList<Regle> listeRegles = new ArrayList<Regle>();
	private HashSet<String> faitsCourants = new HashSet<String>();
	
	/**********************Constructeur************************/
	public MoteurInference(){
		ArrayList<String> data = deserialiserFichierRegle();
		for(String s : data){
			listeRegles.add(new Regle(s));
		}
		
		faitsCourants.add("premierCoup");
	}
	
	/************************Methodes**************************/
	private boolean estSolution(String fait){
		return !faitsCourants.contains(fait);
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
