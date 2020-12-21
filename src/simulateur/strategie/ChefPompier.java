package simulateur.strategie;

import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.evenements.EvenementIntervention;
import simulateur.strategie.strategies_pompier.StrategiePompier;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Classe matérialisant un chef pompier, centre de décisions
 */
public class ChefPompier {


    /**
     * Le simulateur
     */
    private Simulateur simulateur;


    /**
     * La stratégie associée au chef pompier permet de définir les actions qu'il va exécuter à la date de la méthode execute()
     */
    private StrategiePompier strategie;

    public ChefPompier(Simulateur simulateur, StrategiePompier strategie) {
        this.simulateur = simulateur;

        this.strategie =strategie;

    }


    /**
     * La méthode Run est appelée par le simulateur associée au chef pompier et execute la stratégie de ce dernier en fonction de la date courante
     */
    public void run(){

            this.strategie.execute(this,this.simulateur.getDateCourante());
    }

    public Simulateur getSimulateur() {
        return simulateur;
    }

    /**
     *
     * On initialise la stratégie 1 décrite dans le Sujet (affectation aléatoire de robot à un incendie)
     *
     */


    /**
     *
     * @return La liste des incendies encore non affectés sur la carte d'un simulateur
     */
    public static ArrayList<Incendie> getIncendiesVacants(Simulateur simulateur){

        HashSet<Incendie> incendiesVacants=new HashSet<>(simulateur.getDonneeSimulation().getIncendiesNonEteint().values());



        ArrayList<Evenement> evenementsFuturs=simulateur.getListeEvenements();


        for(Evenement evenement :evenementsFuturs){

            if (evenement instanceof EvenementIntervention){

                /**
                 * On récupère l'évènement correspondant
                 */
                Incendie incendie=simulateur.getDonneeSimulation().getIncendies().get( ((EvenementIntervention)evenement).getRobot().getPosition());

                incendiesVacants.remove(incendie); }
        }




        ArrayList<Incendie> incList= new ArrayList<>(incendiesVacants);

        for (int i=0;i<incList.size();i++)if(incList.get(i).estEteint())incList.remove(incList.get(i));

        return incList;
    }


    /**
     *
     * @return un incendie encore non affecté au hasard
     */
    public static  Incendie getRandomIncendieVacant(Simulateur simulateur){

        ArrayList<Incendie> incendies=getIncendiesVacants(simulateur);
        if(incendies.isEmpty()){
            return null;
        }

        int index= (int)Math.floor(Math.random()*incendies.size());
        return  incendies.get(index);

    }






    /**
     *
      * @return un robot au hasard sur la carte
     */
    public static  Robot getRandomRobot(Simulateur simulateur)
    {
            int index=(int)Math.floor(simulateur.getDonneeSimulation().getRobots().size()*Math.random());


        return simulateur.getDonneeSimulation().getRobots().get(index);

    }




    // Permet de réinitialiser la stratégie du chef Pompier
    public void reset(){

        this.strategie.reset(); // Délégation
    }



}
