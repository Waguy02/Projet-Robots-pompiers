package models.incendie_environnment;

import models.enums.*;
import simulateur.Constantes;

import java.util.NoSuchElementException;

import static models.enums.NatureTerrain.EAU;

public class Carte {

	private double tailleCases=Constantes.DEFAULT_TAILLE_CASE;
	private Case[][] cases;

	/**
	 * 
	 * @param nbLignes
	 * @param nbColonnes
	 */
	public Carte(int nbLignes, int nbColonnes,int tailleCases)
	{
		this.cases = new Case[nbLignes][nbColonnes];
		this.tailleCases=tailleCases;
	}

	public int getNbLignes() {
		return cases.length;
	}

	public int getNbColonnes() {
		return cases[0].length;
	}

	public double getTailleCases() {
		return this.tailleCases;
	}


	public void setCase(int lig, int col, Case _case) throws Exception{
		if( lig<0||lig>this.getNbLignes() || col<0|| col>this.getNbColonnes()){

			throw  new Exception("Coordonées de case incorrecte  : ligne :"+lig+ "---colonne :"+col );

		}
		this.cases[lig][col]=_case;


	}

	/**
	 * 
	 * @param lig
	 * @param col
	 */
	public Case getCase(int lig, int col) throws NoSuchElementException

	{
		if( lig<0||lig>this.getNbLignes() || col<0|| col>this.getNbColonnes()){

			throw  new NoSuchElementException("Coordonées de case incorrecte  : ligne :"+lig+ "---colonne :"+col );

		}

		return cases[lig][col];
	}

	/**
	 * 
	 * @param src
	 * @param dir
	 */
	public boolean voisinExiste(Case src, Direction dir) {
		switch(dir)	{
			case NORD:
				return (src.getLigne()>0);

			case SUD:
				return (src.getLigne()<this.getNbLignes()-1);

			case EST:
				return (src.getColonne()<this.getNbColonnes()-1);

			case OUEST:
				return (src.getColonne()>0);

				default:return false;

		}
	}

	/**
	 *
	 * @param src
	 * @param dir
	 * @return
	 * @throws Exception
	 */

	public Case getVoisin(Case src, Direction dir) throws Exception{
		if (!voisinExiste(src, dir)){
			throw new Exception("la case voisine n'existe pas!");
		}
		int lig = src.getLigne();
		int col = src.getColonne();
		switch(dir)	{
			case NORD:
				return this.cases[lig-1][col];

			case SUD:
				return this.cases[lig+1][col];

			case EST:
				return this.cases[lig][col+1];

			case OUEST:
				return this.cases[lig][col-1];

			default:
				return null;
		}


		}

	public Case[][] getCases() {
		return cases;
	}


	/** @return si une case est située à côté d'une case de nature EAU
	 * */

	public  boolean aCoteEAU(Case pos){
		for (Direction dir: Direction.values()){
			if (this.voisinExiste(pos, dir)){
				Case vois;
				try {
					 vois = this.getVoisin(pos, dir);
				}
				catch (Exception e){
					return false;
				}



				if (vois.getNature() == EAU) {
					return true;
				}
			}
		}
		return false;
	}
}

