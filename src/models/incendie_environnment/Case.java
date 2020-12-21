package models.incendie_environnment;

import models.IGraphic;
import models.enums.*;
import simulateur.Constantes;

public class Case implements IGraphic {
    
	private int ligne;
	private int colonne;

	private double taille=Constantes.DEFAULT_TAILLE_CASE;
	private NatureTerrain nature;



	public int getLigne() {
		return this.ligne;
	}

	public int getColonne() {
		return this.colonne;
	}

	public NatureTerrain getNature() {
		return this.nature;
	}


    public void setLigne(int ligne) {
        this.ligne = ligne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public void setNature(NatureTerrain nature) { this.nature = nature; }

    /**
     *
     * @param ligne
     * @param colonne
     */
    public Case(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }

    public Case(int ligne, int colonne, NatureTerrain nature) {
        this(ligne,colonne);
        this.nature = nature;
    }



    @Override
    public String getFileName() {
        switch (this.getNature()){
            case TERRAIN_LIBRE:return Constantes.IMAGE_TERRAIN;
            case ROCHE :return Constantes.IMAGE_ROCHE;
            case EAU:return Constantes.IMAGE_EAU;
            case FORET:return Constantes.IMAGE_FORET;
            case HABITAT:return Constantes.IMAGE_HABITAT;




        }
        return null;
    }

    @Override
    public Case getPosition() {
        return this;
    }

    public String toString(){

        return "Case ("+this.getLigne()+","+this.getColonne()+")";
    }

    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }
}