package IA;

import java.io.*;
import java.util.*;

public class MoteurInference {
	//Main de test

	public static void main(String[] args) {
	}

	/************************Attributs*************************/
	private static MoteurInference instance;
	private final String rulesFilePath = "IA/regles/Battleship.rules";
	private ArrayList<Regle> listeRegles = new ArrayList<Regle>();
	private ArrayList<String> faitsCourants = new ArrayList<String>();
	private int[] dernierCoup = new int[2];
	private int[] avantDernierCoup = new int[2];
	private int[] pointImpact = new int[2];
	private String consequence;
	private int[][] SecteurRecherche = new int[2][2];
	private ArrayList<int[]> directionsPossibles =  new ArrayList<int[]>();


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


		if(!faitsCourants.contains("premierCoup")){
			if(faitsCourants.contains("mode(Destruction)")){
				miseAJourDirPossibles(censureGrille);
			}
			miseAJourBaseFait(censureGrille);
		}
		int i =0;
		for(String s : faitsCourants){
			System.out.println("base de fait "+i+" : "+s);
			i++;
		}

		int[] coupRetour = new int[2];
		Regle r = elireRegle();

		if(r!=null){
			consequence = r.getActions().toString();


			System.out.println("consequence : "+consequence);

			// Application de la regle
			switch(consequence) {
			case "mode(Recherche)":
				// Appel fonction mode recherche
				faitsCourants.clear();
				coupRetour = genererCoupRetourModeRecherche(censureGrille);
				faitsCourants.add(consequence);
				break;
			case "mode(Destruction)":
				// Appel fonction mode destruction
				faitsCourants.clear();
				pointImpact = dernierCoup.clone();
				basculModeDestruction();
				coupRetour = genererCoupRetourModeDestruction();
				faitsCourants.add(consequence);
				break;
			case "testerCoup":
				// tester les positions restantes
				coupRetour = genererCoupRetourModeDestruction();
				break;
			case "changeSecteur":
				// changer secteur
				coupRetour = genererCoupRetourModeRecherche(censureGrille);
				break;
			}

			avantDernierCoup = dernierCoup.clone();
			dernierCoup = coupRetour.clone();
			System.out.println("coup : "+coupRetour[0] + " "+coupRetour[1]);
		}

		return coupRetour;
	}


	private void miseAJourBaseFait(char[][] censureGrille) {
		if(faitsCourants.contains("mode(Recherche)")){
			if(CoupReussi(dernierCoup, censureGrille)){
				faitsCourants.add("coupReussi");
				faitsCourants.remove("coupRate");
				System.out.println("coup reussi");
			}
			else
			{
				faitsCourants.remove("coupRate");
				faitsCourants.add("coupRate");
				System.out.println("coup rate");
			}
		}
		if(faitsCourants.contains("mode(Destruction)")){
			if(directionsPossibles.size() > 0 && 
					!faitsCourants.contains("coupPossible")){
				faitsCourants.add("coupPossible");
				faitsCourants.remove("!coupPossible");
			}
			if(directionsPossibles.size() == 0 &&
					!faitsCourants.contains("!coupPossible")){
				faitsCourants.remove("coupPossible");
				faitsCourants.add("!coupPossible");
			}
		}
	}


	private void miseAJourDirPossibles(char[][] censureGrille) {
		directionsPossibles.remove(dernierCoup);
		if(avantDernierCoup != null){
			if(CoupReussi(dernierCoup, censureGrille) 
					&& CoupReussi(avantDernierCoup, censureGrille)){
				if(avantDernierCoup[0] == dernierCoup[0]){
					directionsPossibles.remove(
							new int[]{pointImpact[0]+1,pointImpact[1]});
					directionsPossibles.remove(
							new int[]{pointImpact[0]-1,pointImpact[1]});
				}
				else 
				{
					directionsPossibles.remove(
							new int[]{pointImpact[0],pointImpact[1]+1});
					directionsPossibles.remove(
							new int[]{pointImpact[0],pointImpact[1]-1});
				}
			}
		}
		if(CoupReussi(dernierCoup, censureGrille)){
			if(pointImpact[0] == dernierCoup[0]){
				directionsPossibles.add(new int[]{dernierCoup[0],dernierCoup[1]
						+((dernierCoup[1]-pointImpact[1])
								/Math.abs(dernierCoup[1]-pointImpact[1]))});
			}
			else 
			{
				directionsPossibles.add(new int[]{dernierCoup[0]
						+((dernierCoup[0]-pointImpact[0])
								/Math.abs(dernierCoup[0]-pointImpact[0]))
								,dernierCoup[1]});
			}
		}		
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
		assert censureGrille[coup[0]][coup[1]] != 'v' : "Erreur";
		return censureGrille[coup[0]][coup[1]] != 'o'; 
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
			String[] temp = new String[faitsCourants.size()];
			temp = faitsCourants.toArray(temp);
			if(r.satisfaitConditions(temp)){
				return r;
			}
		}
		return null;
	}

	private int[] genererCoupRetourModeRecherche(char[][] censureGrille){
		int[] coord = new int[2];

		int[] a = new int[2];
		a[0] = 0;
		a[1] = 4;
		int[] b = new int[2];
		b[0] = 5;
		b[1] = 9;

		if(Arrays.equals(SecteurRecherche[0], a)){
			SecteurRecherche[0] = b.clone();
		}
		else{
			SecteurRecherche[0] = a.clone();
			if(Arrays.equals(SecteurRecherche[1], a)){
				SecteurRecherche[1] = b.clone();
			}
			else
			{
				SecteurRecherche[1] = a.clone();
			}
		}

		do{
			coord[0] = getRandomNum(SecteurRecherche[0][0], 
					SecteurRecherche[0][1]);
			coord[1] = getRandomNum(SecteurRecherche[1][0], 
					SecteurRecherche[1][1]);
		}while(censureGrille[coord[0]][coord[1]] != 'v');

		return coord;

	}

	private void basculModeDestruction(){
		directionsPossibles.clear();
		int[] tempCoord = new int[2];
		if(dernierCoup[0]+1 < 10){
			tempCoord[0] = dernierCoup[0]+1;
			tempCoord[1] = dernierCoup[1];
			directionsPossibles.add(new int[]{tempCoord[0],tempCoord[1]});
		}
		if(dernierCoup[0]-1 >= 0){
			tempCoord[0] = dernierCoup[0]-1;
			tempCoord[1] = dernierCoup[1];
			directionsPossibles.add(new int[]{tempCoord[0],tempCoord[1]});
		}
		if(dernierCoup[1]+1 < 10){
			tempCoord[0] = dernierCoup[0];
			tempCoord[1] = dernierCoup[1]+1;
			directionsPossibles.add(new int[]{tempCoord[0],tempCoord[1]});
		}
		if(dernierCoup[1]-1 >= 0){
			tempCoord[0] = dernierCoup[0];
			tempCoord[1] = dernierCoup[1]-1;
			directionsPossibles.add(new int[]{tempCoord[0],tempCoord[1]});
		}	
		System.out.println("direction possible en pos0 "+directionsPossibles.get(0)[0]+" "+directionsPossibles.get(0)[1]);
	}

	private int[] genererCoupRetourModeDestruction(){
		int[] coupRetour = new int[2];
		coupRetour[0] = directionsPossibles.get(0)[0];
		coupRetour[1] = directionsPossibles.get(0)[1];
		
		int i =0;
		for(int[] s : directionsPossibles){
			System.out.println("dir possible "+i+" : "+s[0]+" "+s[1]);
			i++;
		}
		directionsPossibles.remove(0);
		
		return coupRetour;
	}

	private int getRandomNum(int minNum, int maxNum) {

		int MonChiffre = maxNum + 1;

		while (MonChiffre > maxNum) {

			MonChiffre = (int) (minNum + (Math.random() * (maxNum + 1)));
		}

		return MonChiffre;
	}
}
