public class Bateau{
	
	private String nom;
	private char code;
	private char code_touche;
	private int num_tableau;
	private int longeur;
	private int etat;
	
	public Bateau(String nom, char code, char code_touche, int num_tableau, int longeur){
		
		this.nom = nom;
		this.code = code;
		this.code_touche = code_touche;
		this.num_tableau = num_tableau;
		this.longeur = longeur;
		this.etat = longeur;
		
	}
	
	public String getNom(){ return nom; }
	public char getCode(){ return code; }
	public char getCodeTouche(){ return code_touche; }
	public int getNumTableau(){ return code; }
	public int getLongeur(){ return longeur; }
	public int getEtat(){ return etat; }
	public void hitShip(){ etat--; }
	
}