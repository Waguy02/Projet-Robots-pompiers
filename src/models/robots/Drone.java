package models.robots;

import models.enums.NatureTerrain;
import models.incendie_environnment.Case;
import org.w3c.dom.ranges.RangeException;
import simulateur.Constantes;

import static models.enums.NatureTerrain.EAU;


public class Drone extends Robot {

    public Drone(){
        super();

        this.vitesse=100;
        this.vitesse_max=150;

        this.volReservoirMax=10000;
        this.setVolReservoir (this.volReservoirMax);
        this.volumeInterventionUnitaire=this.volReservoirMax;

        this.tempsRemplissage = 1800;
        this.nbLitreInterventionParS = this.volReservoirMax/30;

    }


    @Override
    public double getVitesse(NatureTerrain nature) {
        return this.vitesse;
    }



    @Override
    public boolean peutAtteindre(Case _case) {
        return true;
    }

    @Override
    public boolean peutSeRemplir(Case _case) {

        return  _case.getNature()==NatureTerrain.EAU;
    }


    @Override
    public void remplirReservoir() {
        if (volReservoir == 0 && position.getNature() == EAU){
            volReservoir = volReservoirMax;
        }   

    }


    @Override
    public String getFileName() {
        return Constantes.IMAGE_DRONE;
    }
}