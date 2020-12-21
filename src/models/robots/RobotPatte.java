package models.robots;

import models.enums.NatureTerrain;
import models.incendie_environnment.Case;
import simulateur.Constantes;

import static models.enums.NatureTerrain.EAU;

public class RobotPatte extends Robot {



    public RobotPatte(){
        super();

        this.volReservoirMax= Integer.MAX_VALUE-10000; // On soutrait 100000 pour éviter les dpassements de capacité qui conduiraient à des resultats d'additions négatifs

        this.volReservoir =this.volReservoirMax;
        this.vitesse=30;
        this.vitesse_max=-1;
        this.tempsRemplissage=0;
        this.nbLitreInterventionParS=10;
        this.volumeInterventionUnitaire=10;
    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        switch (nature){
            case ROCHE:return 10;
            default:return this.vitesse;
        }
    }

    @Override
    public boolean peutAtteindre(Case case_) {
        NatureTerrain natCase = case_.getNature();
        if (natCase == EAU) {
            return false;
        }
        return true;
    }


    @Override
    public void deverserEau (int vol){
        //volReservoir -= vol;
        }

    public void remplirReservoir() {
        }

    @Override
    /**
     * Le volume du réservoir de ce robot reste infini donc il n'est pas modifié suite à une intervention
     */
    public void setVolReservoir(int vol){

    }

    @Override
    public String getFileName() {
        return Constantes.IMAGE_PATTES;
    }

    public boolean estVide(){
        return false;
    }


}
