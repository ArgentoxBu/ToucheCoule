package IA;

import java.io.*;
import java.util.*;

public class MoteurInference {
	
	/************************Attributs*************************/
	private final String rulesFilePath = "IA/regles/Battleship.rules";
	private ArrayList<Regle> listeRegles;
	
	/**********************Constructeur************************/
	public MoteurInference(){
		ArrayList<String> data = deserialiserFichierRegle();
		for(String s : data){
			listeRegles.add(new Regle(s));
		}
	}
	
	/************************Methodes**************************/
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
			System.out.println("Fichier "+rulesFilePath+" non trouve : "+e.getStackTrace()[0]);
		}		
		
		return data;
	}
}
