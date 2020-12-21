package models.robots;

import models.IGraphic;
import models.incendie_environnment.*;
import models.enums.*;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.strategie.CalculateurChemin;

import java.util.ArrayList;
import java.util.HashSet;


public abstract class Robot implements IGraphic,Resetable{


	Case position;
	protected int volReservoir;

	protected double vitesse;
	protected double vitesse_max;
	protected int tempsRemplissage;


	protected double nbLitreInterventionParS;

	protected int volumeInterventionUnitaire;

	protected DonneeSimulation donneeSimulation;

	protected int volReservoirMax;


	public void setVolReservoir(int volReservoir) {
		if(this.volReservoir==0);
		this.volReservoir = volReservoir;
	}

	public double getNbLitreInterventionParS() {
		return nbLitreInterventionParS;
	}

	public int getTempsRemplissage() {
		return tempsRemplissage;
	}

	public int getVolReservoirMax() {
		return volReservoirMax;
	}

	public void setVolReservoirMax(int volReservoirMax) {
		this.volReservoirMax = volReservoirMax;
	}

	public DonneeSimulation getDonneeSimulation() {
		return donneeSimulation;
	}

	public void setDonneeSimulation(DonneeSimulation donneeSimulation) {
		this.donneeSimulation = donneeSimulation;
	}

	public Case getPosition() {
		return this.position;
	}

	/**
	 * @param target
	 */
	public void setPosition(Case target) {
		if(this.position==null)this.initialPosition=target;
		this.position = target;
	}

	public int getVolReservoir() {
		return this.volReservoir;
	}


	/**
	 * @param nature
	 * @return
	 */
	public abstract double getVitesse(NatureTerrain nature);

	public void setVitesse(double vitesse) throws Exception {

		if (vitesse < 0) throw new Exception("Vitesse négative non tolérée");
		if (this.vitesse_max != -1 && vitesse > this.vitesse_max)
			throw new Exception("Vitesse supérieure à la vitesse maximale ");

		this.vitesse = vitesse;
	}


	public abstract boolean peutAtteindre(Case _case);

	/**
	 * @param vol
	 */

	public void deverserEau(int vol) throws Exception {
		if (volReservoir < vol) {
			throw new Exception("le réservoir ne contient pas assez d'eau");
		}
		volReservoir -= vol;

	}

	/**
	 *
	 * @param _case
	 * @return vrai si le robot peut se remplir sur la case considérée et faux sinon
	 * Sa définition par défaut est celle des robots terrestres
	 */
	public boolean  peutSeRemplir(Case _case){

		Carte carte=this.getDonneeSimulation().getCarte();
		for (Direction direction:Direction.values()){

			if(carte.voisinExiste(_case,direction)){
				try {
					if (carte.getVoisin(_case, direction).getNature()==NatureTerrain.EAU)return true;
				}
				catch (Exception e){
			System.out.println("Erreur de check de test de remplissage :"+e.getMessage());
				}
			}


		}
		return false;


	}

	public abstract void remplirReservoir();


	/**
	 * La position initiale du robot utilisée lors de l'appel restart du simulateur
	 */
	private Case initialPosition;

	/**
	 * Le volume initial contenu dans le robot, différent du volume maximal
	 */


	public void reset(){
		this.position=initialPosition;
		this.volReservoir=this.volReservoirMax;
	}


	/**
	 *
	 * @param position
	 * @return le plus Court chemin d'un robot jusqu'à la case position. retourne null si il n'y  a pas de chemin.
	 * Cette méthode est moins couteuse qu'une méthode booleenne qui aurait nécessité deux appels de la méthode de calcul
	 * de plus court chemin
	 */
	public ArrayList<Case> ppc_vers(Case position){
		try {

			ArrayList<Case> ppc = CalculateurChemin.genererPlusCourtChemin(this, position);
			if(ppc.isEmpty())return null;
			else  return ppc;

		}
		catch (Exception e){
			System.out.println("Erreur dans le  calcul de ppc :"+e);
			e.printStackTrace();
			return  null;
		}

	}



	public ArrayList<Case> ppc_remplissage(){

		return CalculateurChemin.cheminOptimalRemplissage(this);
	}








	/**
	 *
	 * @return Vrai si le simulateur compte un évènement en attente pour le robot et faux sinon
	 */
	public boolean est_occupe(Simulateur simulateur){

		for (Evenement evenement:simulateur.getListeEvenements()){

			if(evenement.getRobot()==this){
				return true;
			}
		}

		return false;

	}

	/**
	 *
	 * @return VRAI si le reservoir est vide;
	 */
	public boolean estVide(){
	return this.getVolReservoir()<=0;
	}


	/**
	 *
	 * @param volume
	 * @return le volume à verser effectivement en fonction du volume d'intervention unitaire et du volume de reservoir
	 */
	public  int calculerVolumeIntervention(int volumeRequis){
			if(volumeRequis==0)return 0;
			 if(volumeRequis>=this.volReservoir)return this.volReservoir;

			int currentVolume=this.volumeInterventionUnitaire;


			while((currentVolume<volumeRequis)&&(currentVolume<=this.volReservoir+this.volumeInterventionUnitaire)){

			currentVolume+=this.volumeInterventionUnitaire;

			}




		return currentVolume;
	}

	public int getVolumeInterventionUnitaire() {
		return volumeInterventionUnitaire;
	}



	// Cette attribut stocke toutes les cases que le robot ne peut pas atteindre sur la carte pour limiter les
	// recherches inutiles de plus courts chemin (Mémoire du robot)
	private HashSet<Case> position_unreachable=new HashSet<>();

	public HashSet<Case> getPosition_unreachable() {
		return position_unreachable;
	}
}