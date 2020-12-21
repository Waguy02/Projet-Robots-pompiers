package models.incendie_environnment;

import models.IGraphic;
import models.robots.Resetable;
import simulateur.Constantes;

public  class Incendie implements IGraphic,Resetable {

	private Case position;
	private int nbLitres;

	/**
	 *
	 * @param position
	 * @param nbLitres
	 */
	public Incendie(Case position, int nbLitres) {
		this.position = position;
		this.nbLitres = nbLitres;
		this.initialNbLitres=nbLitres;
	}


	public Case getPosition() {
		return position;
	}

	/**
	 *
	 * @param position
	 */

	public void setPosition(Case position) {
		this.position = position;
	}

	public int getNbLitres() {
		return nbLitres;
	}

	public void setNbLitres(int nbLitres) {
		this.nbLitres = nbLitres;
	}


	@Override
	public String getFileName() {


		if(this.nbLitres<=0)return "";// Les incendies éteints ne sont pas affichés

		return Constantes.IMAGE_INCENDIE;
	}

	/**
	 * L'intensité initiale de l'intensité (Nombre de litres nécessaires pour l'éteindre)
	 */
	private int initialNbLitres;

	@Override
	public void reset() {
		this.nbLitres=this.initialNbLitres;
	}

	/**
	 *
	 * @return
	 */
	public boolean estEteint(){
		return  this.getNbLitres()<=0;

	}


	public String toString(){
		return "Incendie d'intensité :"+this.getNbLitres()+" en "+this.getPosition().toString();
	}
}