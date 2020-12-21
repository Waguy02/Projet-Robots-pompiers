package models.incendie_environnment;

import models.robots.*;
import simulateur.Simulateur;

import java.util.*;

/***
 * Classe qui encapsule l'ensemble des données de simulation (La carte , les incendies et les robots
 *
 *
 *
 */
public class DonneeSimulation {

	private HashMap<Case,Incendie> incendies=new HashMap<>();
	private Carte carte;
	private ArrayList<Robot> robots=new ArrayList();

	/**
	 *
	 * @return La liste des robots
	 */
	public ArrayList<Robot> getRobots(){
		return this.robots;
	}

	/**
	 *
	 * @return La liste des robots (une hashmap)
	 */
	public HashMap<Case, Incendie> getIncendies() {
		return incendies;
	}

	/**
	 *
	 * @return La liste des incendies encore allumés
	 */
	public HashMap<Case,Incendie> getIncendiesNonEteint(){

		HashMap<Case,Incendie> result=new HashMap<>();
		for(Incendie incendie:this.getIncendies().values())if(!incendie.estEteint())result.put(incendie.getPosition(),incendie);
		return  result;

	}



	public Carte getCarte() {
		return this.carte;
	}


	/**
	 * L'attribut Lecteur permet de lire les données pour faciliter la réinitialisation du simulateur
	 */


	/**
	 *
	 * @param carte
	 * @param incendies
	 * @param robots
	 */
	public DonneeSimulation(Carte carte, HashMap<Case, Incendie> incendies, ArrayList<Robot> robots) {
		this.incendies = incendies;
		this.carte = carte;
		this.robots = robots;
		for (Robot robot:this.robots){
			robot.setDonneeSimulation(this);

		}

	}


    public DonneeSimulation() { }


	/**
	 *
	 * @return Vrai si les robots appartiennent tous à la carte et Faux sinon (Incohérence de positionnement)
	 */
	public boolean verifierPositionRobots(){
		List<Case> cartes_cases=new ArrayList();
		for (int i=0;i<this.carte.getNbLignes();i++)for (int j=0;j<this.carte.getNbColonnes();j++) cartes_cases.add(this.carte.getCases()[i][j]);


		boolean check=true;
		for (Robot robot:this.robots){

			if(!cartes_cases.contains(robot.getPosition())){

				check=false;
				break;
			}



		}

		return check;


	}


}







