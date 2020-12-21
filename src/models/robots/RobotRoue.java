package models.robots;

import models.enums.NatureTerrain;
import models.incendie_environnment.Carte;
import models.incendie_environnment.Case;
import simulateur.Constantes;

import static models.enums.NatureTerrain.HABITAT;
import static models.enums.NatureTerrain.TERRAIN_LIBRE;

public class RobotRoue extends Robot {

    public RobotRoue(){
        super();
        this.volReservoirMax=5000;
        this.volReservoir=this.volReservoirMax;
        this.vitesse=80;
        this.vitesse_max=-1;
        this.nbLitreInterventionParS=20;
        this.tempsRemplissage=600;

        this.volumeInterventionUnitaire=100;
    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        switch (nature){
            case EAU:case ROCHE:return 0;
            default:return  this.vitesse;

        }
    }

    @Override
    public boolean peutAtteindre(Case case_) {
        NatureTerrain natCase = case_.getNature();
        if (natCase == TERRAIN_LIBRE || natCase==HABITAT){
            return true;
        }
        return false;

    }


    @Override
    public void remplirReservoir() {
        if (volReservoir == 0 && this.donneeSimulation.getCarte().aCoteEAU(this.position)){
            volReservoir = this.volReservoirMax;
        }
    }

    @Override
    public String getFileName() {
        return Constantes.IMAGE_ROUES;
    }
}