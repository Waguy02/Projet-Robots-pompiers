package simulateur.evenements;

import models.incendie_environnment.Case;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Constantes;

import java.util.ArrayList;

public class EvenementRemplissage extends Evenement {

    public EvenementRemplissage(Robot robot) {
        super(robot);
    }

    @Override
    public void execute() {
        this.robot.setVolReservoir(robot.getVolReservoirMax());
    }

    @Override
    public void calculateDate(long t0) {


        /**
         * On calcule la vitesse de remplissage et à partir de cette dernière.
         *
         */
        double volume_supp=robot.getVolReservoirMax()-robot.getVolReservoir();


        double vitesse_remplissage=robot.getVolReservoirMax()/robot.getTempsRemplissage();


        System.out.println("VIETESS REMPLISSAGE ! "+vitesse_remplissage);
        System.out.println("DUREE REMP !"+volume_supp/vitesse_remplissage);


        Long nombre_etapes=(long)Math.ceil(volume_supp/vitesse_remplissage   /Constantes.DUREE_ETAPE);
        System.out.println("NOMBRE S ETAPES : "+nombre_etapes);

        this.date=(t0+nombre_etapes);

    }



    public String toString(){
        String textnext=this.getNext()==null?"":this.getNext().toString();
        return " Evenement Remplissage , DATE :  "+this.getDate() +" NEXT :"+textnext;
    }
}
