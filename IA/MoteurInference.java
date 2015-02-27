package IA;

import java.io.*;
import java.util.*;

public class MoteurInference {
	//Main de test

	public static void main(String[] args) {
        int[][] SecteurRecherche = new int[2][2];
        int[] a = new int[2];
        a[0] = 1;
        a[1] = 5;
		System.out.println();
	}
	
	/************************Attributs*************************/
	private static MoteurInference instance;
	private final String rulesFilePath = "IA/regles/Battleship.rules";
	private ArrayList<Regle> listeRegles = new ArrayList<Regle>();
	private HashSet<String> faitsCourants = new HashSet<String>();
	private int[] dernierCoup = new int[2];
	private int[] avantDernierCoup = new int[2];
	private String consequence;
    private int[][] SecteurRecherche = new int[2][2];
	
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
        int[] coupRetour = new int[2];
		
		consequence = elireRegle().getActions().toString();
		
		// Application de la regle
		switch(consequence) {
			case "mode(Recherche)":
				// Appel fonction mode recherche
				break;
            case "mode(Destrution)":
                // Appel fonction mode destruction
                break;
            case "changeDirection":
                // changerDirection
                break;
            case "continuerDirection":
                // continuerDirection
                coupRetour[0] = avantDernierCoup[0]*(1+(dernierCoup[0]-1));
                coupRetour[1] = avantDernierCoup[1]*(1+(dernierCoup[1]-1));
                break;
            case "changeSecteur":
                // changer secteur
                genererCoupRetourNouveauSecteur();
                break;
		}

        avantDernierCoup = dernierCoup;
        dernierCoup = coupRetour;
		return coupRetour;
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

    private Regle elireRegle(){
        ArrayList<Regle> tempListeRegles = new ArrayList<Regle>(listeRegles);

        for(Regle r : tempListeRegles){
            for(String s : faitsCourants){
                if(!r.getPremisses().contains(s)){
                    tempListeRegles.remove(r);//marquer la regle
                }
            }
        }
        if(tempListeRegles.size() == 1){
            return tempListeRegles.get(0);
        }
        else
        {
            //A modifier si on a plusieurs regles avec les memes premisses
            return null;
        }
    }

    private int[] genererCoupRetourNouveauSecteur(){
        int[] a = new int[2];
        a[0] = 1;
        a[1] = 5;
        int[] b = new int[2];
        b[0] = 6;
        b[1] = 10;

        if(dernierSecteurRecherche)
    }
}
