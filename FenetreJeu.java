import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;

// ****************************************************************	
// Class d'une fenetre de jeu de "BattleShip"
// Elle implements la classe ActionListener
// ****************************************************************	

class FenetreJeu extends JFrame implements ActionListener {

	// Déclaration des composants de la fenetre
	// ********************************************************************

	private static final String DEFAULT_PATH_IMG = "/images/";

	private Image MesImages[][];
	private char GrilleVirtuelle[][];
	private JButton GrilleAfficher[][];

	private Bateau listeBateau[];
	private boolean Vision = true;
	private int NbCoup = 0;

	JLabel MonLabel;
	JPanel MonPaneau;
	JButton MonBouton;
	JButton MonBoutonVision;

	private ImageIcon test;

	private boolean terminer = false;

	// ********************************************************************
	// Constructeur de la fenetre
	// ********************************************************************

	public FenetreJeu() {

		int i = 0;
		int j = 0;

		// Initialisation de la grille de jeu virtuel
		// (a = cuirasse ! b = torpilleur ! c = sous-marin ! d = croiseur ! e =
		// porteavion)
		// (f = cuirasseT ! g = torpilleurT ! h = sous-marinT ! i =
		// croiseur_touche ! j = porteavion_touche)
		// (o = ocean) ! (v = vide)
		// *************************************************************************************************

		GrilleVirtuelle = new char[10][10];

		for (i = 0; i < 10; i++) {
			for (j = 0; j < 10; j++) {
				GrilleVirtuelle[i][j] = 'v';
			}
		}

		// Initialisation de la liste contenant les informations sur les bateaux
		// ************************************************************************

		listeBateau = new Bateau[5];

		listeBateau[0] = new Bateau("Cuirasse", 'a', 'f', 1, 2);
		listeBateau[1] = new Bateau("Torpilleur", 'b', 'g', 2, 3);
		listeBateau[2] = new Bateau("Sous-Marin", 'c', 'h', 3, 3);
		listeBateau[3] = new Bateau("Croiseur", 'd', 'i', 4, 4);
		listeBateau[4] = new Bateau("Porte-Avion", 'e', 'j', 5, 5);

		// Initialisation des composant
		// ****************************************************************

		MonLabel = new JLabel("Les messages de jeu seront affichés ici...");
		MonLabel.setBackground(Color.red);

		MonPaneau = new JPanel();
		MonBouton = new JButton("Jouer");
		MonBoutonVision = new JButton("Vision Partielle");

		MonPaneau.setLayout(null);

		// Initialisation des images
		// ****************************************************************

		MesImages = new Image[6][2];

		MesImages[1][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "cuirasse.jpg"));
		MesImages[1][1] = Toolkit.getDefaultToolkit().getImage(
				getClass()
						.getResource(DEFAULT_PATH_IMG + "cuirasse_touche.jpg"));
		MesImages[2][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "torpilleur.jpg"));
		MesImages[2][1] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(
						DEFAULT_PATH_IMG + "torpilleur_touche.jpg"));
		MesImages[3][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "sousmarin.jpg"));
		MesImages[3][1] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(
						DEFAULT_PATH_IMG + "sousmarin_touche.jpg"));
		MesImages[4][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "croiseur.jpg"));
		MesImages[4][1] = Toolkit.getDefaultToolkit().getImage(
				getClass()
						.getResource(DEFAULT_PATH_IMG + "croiseur_touche.jpg"));
		MesImages[5][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "porteavion.jpg"));
		MesImages[5][1] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(
						DEFAULT_PATH_IMG + "porteavion_touche.jpg"));
		MesImages[0][0] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "ocean.jpg"));
		MesImages[0][1] = Toolkit.getDefaultToolkit().getImage(
				getClass().getResource(DEFAULT_PATH_IMG + "vide.jpg"));

		// Création de la grille de jeu réelle
		// ****************************************************************

		int pos_x = 50;
		int pos_y = 50;

		GrilleAfficher = new JButton[10][10];

		for (i = 0; i < 10; i++) {
			for (j = 0; j < 10; j++) {

				GrilleAfficher[i][j] = new JButton();
				GrilleAfficher[i][j].setSize(65, 60);
				GrilleAfficher[i][j].setLocation(pos_x, pos_y);
				MonPaneau.add(GrilleAfficher[i][j]);

				pos_x = pos_x + 65;
			}

			pos_x = 50;
			pos_y = pos_y + 60;
		}

		placerBateau();

		afficherGillePartielle();

		// Déclaration des objets dont nous écouterons les évènements
		// ****************************************************************

		MonBouton.addActionListener(this);
		MonBoutonVision.addActionListener(this);

		// Initialisation des dimensions et positions des objets graphiques
		// ****************************************************************

		setSize(950, 720);

		MonBouton.setBounds(750, 600, 150, 50);
		MonBoutonVision.setBounds(750, 500, 150, 50);
		MonLabel.setBounds(10, 10, 250, 30);

		// Ajout des composants sur le paneau graphique de la fenetre
		// ****************************************************************

		(getContentPane()).add(MonPaneau);

		MonPaneau.add(MonLabel);
		MonPaneau.add(MonBouton);
		MonPaneau.add(MonBoutonVision);

		// Affichage de la fenetre et configuration du titre
		// ****************************************************************

		setTitle("Battaille Navale - Fenêtre de jeu");
		show();

		// Permet de terminer quand la fenetre se ferme (X)
		// ****************************************************************

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		// ****************************************************************

	}

	// ****************************************************************
	// Méthode permettant de gérer les événements sur la fenetre
	// ****************************************************************

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == MonBouton) {
			IA_jouer_un_coup();
			if (!terminer)
				NbCoup++;
		}

		if (e.getSource() == MonBoutonVision) {
			BasculerVision();
		}

	}

	// ****************************************************************
	// Démarrage de la fenêtre de jeu
	// ****************************************************************

	public static void main(String[] args) {

		FenetreJeu GUI = new FenetreJeu();
	}

	// ****************************************************************
	// Méthode d'affichage de la grille de jeu (complète)
	// ****************************************************************

	public void afficherGilleComplete() {

		Image tmp;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				switch (GrilleVirtuelle[i][j]) {

				case 'v': {
					tmp = MesImages[0][1];
					break;
				}
				case 'o': {
					tmp = MesImages[0][0];
					break;
				}
				case 'a': {
					tmp = MesImages[1][0];
					break;
				}
				case 'b': {
					tmp = MesImages[2][0];
					break;
				}
				case 'c': {
					tmp = MesImages[3][0];
					break;
				}
				case 'd': {
					tmp = MesImages[4][0];
					break;
				}
				case 'e': {
					tmp = MesImages[5][0];
					break;
				}
				case 'f': {
					tmp = MesImages[1][1];
					break;
				}
				case 'g': {
					tmp = MesImages[2][1];
					break;
				}
				case 'h': {
					tmp = MesImages[3][1];
					break;
				}
				case 'i': {
					tmp = MesImages[4][1];
					break;
				}
				case 'j': {
					tmp = MesImages[5][1];
					break;
				}
				default: {
					tmp = MesImages[0][1];
				}

				}

				GrilleAfficher[i][j].setIcon(new ImageIcon(tmp));
			}
		}

	}

	// ****************************************************************
	// Méthode d'affichage de la grille de jeu (Censurée)
	// ****************************************************************

	public void afficherGillePartielle() {

		Image tmp;
		char GrilleTemp[][];
		GrilleTemp = CensureGrille();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				switch (GrilleTemp[i][j]) {

				case 'v': {
					tmp = MesImages[0][1];
					break;
				}
				case 'o': {
					tmp = MesImages[0][0];
					break;
				}
				case 'f': {
					tmp = MesImages[1][1];
					break;
				}
				case 'g': {
					tmp = MesImages[2][1];
					break;
				}
				case 'h': {
					tmp = MesImages[3][1];
					break;
				}
				case 'i': {
					tmp = MesImages[4][1];
					break;
				}
				case 'j': {
					tmp = MesImages[5][1];
					break;
				}
				default: {
					tmp = MesImages[0][1];
				}

				}

				GrilleAfficher[i][j].setIcon(new ImageIcon(tmp));
			}
		}

	}

	// ****************************************************************
	// Méthode qui joue un coup (la mécanique du coup)
	// ****************************************************************

	private boolean jouer(int pos_x, int pos_y) {

		if (!terminer) {

			for (int i = 0; i < 5; i++) {

				if (GrilleVirtuelle[pos_x][pos_y] == listeBateau[i].getCode()) {

					GrilleVirtuelle[pos_x][pos_y] = listeBateau[i]
							.getCodeTouche();
					listeBateau[i].hitShip();

					if (listeBateau[i].getEtat() > 0) {
						MonLabel.setText(listeBateau[i].getNom()
								+ " a été touché...");
					} else {
						MonLabel.setText(listeBateau[i].getNom()
								+ " a été coulé...");
					}

					terminer = verifWIN();

					return true;
				}
			}

			if (GrilleVirtuelle[pos_x][pos_y] == 'v') {

				GrilleVirtuelle[pos_x][pos_y] = 'o';
				MonLabel.setText("Le tir est tombé dans l'océan");
				return true;
			}
		} else {

			MonLabel.setText("La partie est terminée");
			return false;

		}

		return false;
	}

	// ****************************************************************
	// Méthode qui place aléatoirement les bateaux sur la grille
	// ****************************************************************

	private void placerBateau() {

		// Placer les 5 bateaux
		// ********************************

		for (int i = 0; i < 5; i++) {

			// Décide au hasard de placer le bateau à la vertical ou horizontal
			// *******************************************************************

			int vertical_horizontal = getRandomNum(0, 1);

			int case_depart_x = 0;
			int case_depart_y = 0;

			boolean ok = false;

			// Decide de la position de départ, vérifie l'espace et place le
			// bateau
			// **********************************************************************

			while (!ok) {

				ok = true;

				// SI VERTICAL
				// *******************************************

				if (vertical_horizontal == 0) {

					// Générer au hasard la position de départ du bateau
					// *********************************************************

					case_depart_x = getRandomNum(0, 9);
					case_depart_y = getRandomNum(0,
							(9 - (listeBateau[i].getLongeur() + 1)));

					int j = 0;

					// Vérification de l'espace disponible pour placer le bateau
					// *************************************************************

					for (j = case_depart_y; j < (case_depart_y + listeBateau[i]
							.getLongeur()); j++) {

						if (GrilleVirtuelle[case_depart_x][j] != 'v') {
							ok = false;
						}
					}

					// S'il y a de l'espace, on place le bateau dans les cases
					// *************************************************************

					if (ok) {

						for (j = case_depart_y; j < (case_depart_y + listeBateau[i]
								.getLongeur()); j++) {

							GrilleVirtuelle[case_depart_x][j] = listeBateau[i]
									.getCode();
						}
					}

					// SI HORIZONTAL
					// *******************************************

				} else {

					// Générer au hasard la position de départ du bateau
					// *********************************************************

					case_depart_x = getRandomNum(0,
							(9 - (listeBateau[i].getLongeur() + 1)));
					case_depart_y = getRandomNum(0, 9);

					int j = 0;

					// Vérification de l'espace disponible pour placer le bateau
					// *************************************************************

					for (j = case_depart_x; j < (case_depart_x + listeBateau[i]
							.getLongeur()); j++) {

						if (GrilleVirtuelle[j][case_depart_y] != 'v') {
							ok = false;
						}
					}

					// S'il y a de l'espace, on place le bateau dans les cases
					// *************************************************************

					if (ok) {

						for (j = case_depart_x; j < (case_depart_x + listeBateau[i]
								.getLongeur()); j++) {

							GrilleVirtuelle[j][case_depart_y] = listeBateau[i]
									.getCode();
						}
					}
				}
			}
		}

	}

	// ****************************************************************
	// Méthode qui génère aléatoirement un nombre entre minNum et maxNum
	// ****************************************************************

	private int getRandomNum(int minNum, int maxNum) {

		int MonChiffre = maxNum + 1;

		while (MonChiffre > maxNum) {

			MonChiffre = (int) (minNum + (Math.random() * (maxNum + 1)));
		}

		return MonChiffre;
	}

	// ****************************************************************
	// Méthode qui vérifie si la partie est terminée
	// ****************************************************************

	private boolean verifWIN() {

		boolean gagne = true;

		for (int i = 0; i < 5; i++) {

			if (listeBateau[i].getEtat() > 0) {
				gagne = false;
			}
		}

		return gagne;
	}

	// ************************************************************************
	// Méthode qui détermine de manière "Intelligente" le prochain coup à jouer
	// ************************************************************************

	private void IA_jouer_un_coup() {

		if (!terminer) {

			boolean ok = false;

			char ConnaissanceAgent[][];
			ConnaissanceAgent = CensureGrille();

			// ************** Section à modifier commence ici *************

			while (!ok) {

				int pos_x = getRandomNum(0, 9);
				int pos_y = getRandomNum(0, 9);

				ok = jouer(pos_x, pos_y);
			}

			// ************** Section à modifier termine ici *************

			afficherGillePartielle();

		} else {

			MonLabel.setText("La partie est terminée (" + NbCoup + " coups)");
		}
	}

	// ********************************************************************
	// Méthode permettant de voir ou sont placer les bateaux sur la grille
	// ********************************************************************

	private void BasculerVision() {

		if (Vision) {
			MonBoutonVision.setText("Vision Complete");
			afficherGilleComplete();
			Vision = false;
		} else {
			MonBoutonVision.setText("Vision Partielle");
			afficherGillePartielle();
			Vision = true;
		}

	}

	// ************************************************************************
	// Méthode qui renvoie la grille de jeu censuré (seul les infos visibles)
	// ************************************************************************

	private char[][] CensureGrille() {

		char Tableau[][];
		Tableau = new char[10][10];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (GrilleVirtuelle[i][j] != 'a'
						&& GrilleVirtuelle[i][j] != 'b'
						&& GrilleVirtuelle[i][j] != 'c'
						&& GrilleVirtuelle[i][j] != 'd'
						&& GrilleVirtuelle[i][j] != 'e') {
					Tableau[i][j] = GrilleVirtuelle[i][j];
				} else {
					Tableau[i][j] = 'v';
				}
			}
		}

		return Tableau;
	}

}