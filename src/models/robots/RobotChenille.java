package models.robots;

import models.enums.NatureTerrain;
import models.incendie_environnment.Carte;
import models.incendie_environnment.Case;
import simulateur.Constantes;

import static models.enums.NatureTerrain.EAU;
import static models.enums.NatureTerrain.ROCHE;

public class RobotChenille extends Robot {

    public RobotChenille(){
        super();
        this.volReservoirMax=2000;
        this.volReservoir=this.volReservoirMax;
        this.vitesse=60;
        this.vitesse_max=80;
        this.nbLitreInterventionParS=100/8;
        this.volumeInterventionUnitaire=100;
        this.tempsRemplissage=300;

    }

    @Override
    public double getVitesse(NatureTerrain nature) {
        switch (nature){
            case FORET:return this.vitesse/2;
            case EAU:case ROCHE:return 0;
            default:return this.vitesse;


        }
    }

    @Override
    public boolean peutAtteindre(Case case_) {
        NatureTerrain natCase = case_.getNature();
        if (natCase == EAU || natCase==ROCHE){
            return false;
        }
        return true;

    }


    @Override
    public void remplirReservoir() {
        if (volReservoir == 0 && this.donneeSimulation.getCarte().aCoteEAU(this.position)){
            volReservoir = volReservoirMax;
        }


    }

    @Override
    public String getFileName() {
        return Constantes.IMAGE_CHENILLES;
    }
}