package simulateur.strategie.strategies_pompier;

import models.incendie_environnment.Case;
import models.incendie_environnment.DonneeSimulation;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.evenements.EvenementIntervention;
import simulateur.strategie.CalculateurChemin;
import simulateur.strategie.ChefPompier;
import simulateur.strategie.CheminRobot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static simulateur.strategie.ChefPompier.getIncendiesVacants;
import static simulateur.strategie.ChefPompier.getRandomIncendieVacant;
import static simulateur.strategie.ChefPompier.getRandomRobot;

// Permet de tester la stratégie triviale
public class StrategieTriviale implements StrategiePompier {

    /**
     * La date a partir de laquelle les instructions sont périodiquement envoyées
     */
    private long date_0;

    /**
     * La fréquence d'executions
     */
    private long step;


    boolean tous_les_incendies_eteints=false;//Stocke la valeur vrai si tous les incendies ont été eteints et stoppe les executions

    public StrategieTriviale(long date_0,long step){
        this.date_0=date_0;
        this.step=step;

    }
    
    HashSet<Robot> robots_indisponibles=new HashSet<>();
    
    
    @Override
    public void execute(ChefPompier chefPompier, long date_courante) {

        if(tous_les_incendies_eteints)return; // Si tous les incendies ont été eteints, on ne fait rien

        System.out.println("Date courante :"+date_courante);



        // La date courante ne correspond pas à la bonne période, on sort de la boucle
        if(!this.check_etape(date_courante))return;

        Simulateur simulateur=chefPompier.getSimulateur();
        DonneeSimulation donneeSimulation=simulateur.getDonneeSimulation();

        if(donneeSimulation.getIncendiesNonEteint().isEmpty()){
            this.tous_les_incendies_eteints=true;
            simulateur.getListeEvenements().clear();
            System.out.println("Tous les incendies ont été eteints : Felicitations");
            return;
        }



        HashSet<Incendie> liste_essai_incendie=new HashSet<>();
        Robot robot_elu=null;
        ArrayList<Case> ppc = null;
        Incendie incendie=null;
        int ctr=0;


        // On commence par chercher un incendie
        do {

            if (liste_essai_incendie.size() ==getIncendiesVacants(simulateur).size()){

                break;
            }

            incendie= getRandomIncendieVacant(simulateur); // On prend un incendie vacant au hasard


            if (liste_essai_incendie.contains(incendie)) {


                incendie=null; // On ne peut pas choisir cet incidie
                continue;
            }


            if (incendie == null) break;
            liste_essai_incendie.add(incendie);




            boolean robot_founded = false;

            HashSet<Robot> liste_essai_robots = new HashSet<>();


            do {

                boolean tous_indisponibles = true;
                for (Robot r : donneeSimulation.getRobots()) {
                    if (!robots_indisponibles.contains(r) && !liste_essai_robots.contains(r)){
                        tous_indisponibles=false;
                       
                        break;
                    }


                }

                if (tous_indisponibles) {

                    robot_elu = null;
                    break;
                }

                robot_elu =getRandomRobot(simulateur) ; // On prend un robot au hasard

                if (robot_elu == null){

                    break;}


                    // Si le robot a été déjà été essayé pour les incendie
                if (liste_essai_robots.contains(robot_elu) || this.robots_indisponibles.contains(robot_elu)) continue;
                liste_essai_robots.add(robot_elu);


                if (robot_elu.est_occupe(simulateur)) {
                    System.out.println(robot_elu + " est occupé");
                    continue;
                }

                // On récupère le plus court chemin vers l'incendie
                ppc = robot_elu.ppc_vers(incendie.getPosition());


                // Si le robot est vide, il devient inutilisable
                if (robot_elu.estVide()) {
                    System.out.println(robot_elu + " a un reservoir vide, il ne pourra plus être utilisé");
                    this.robots_indisponibles.add(robot_elu);
                    continue;
                }


                // S'il n' y a pas de plus court chemin vers l'incendie, on  continue la boucle
                if (ppc == null) {
                    System.out.println(robot_elu + "ne peut se rendre en " + incendie.getPosition());
                    continue;
                }

                robot_founded = true;

            } while (robot_founded == false);

            if (robot_elu == null) {
                System.out.println("Aucun robot n'est diponible pour cet incendie \n" );
                incendie=null; // On  ne peut pas gérer cet incendie
            }
        }
        while(incendie==null);


        if(incendie==null){
            return;
        }

        System.out.println("INCENDIE PRIS EN CHARGE : "+incendie+" par le robot"+robot_elu );

        try {
            /**
             * On génère la séquence de déplacements à partir des plus courts chemins ppc
             * calculé précédemment, et on ajoute l'intervention sur le feu.
             */
            CheminRobot cr=CalculateurChemin.generateSequenceDeplacement(robot_elu, ppc, simulateur.getDateCourante());
            List<Evenement> events=cr.getEvents();

            // On crée l'intervention en fonction de l'intensité de l'incendie
            EvenementIntervention intervention=new EvenementIntervention(robot_elu,incendie.getNbLitres());
            if(events.isEmpty()){
                intervention.calculateDate(simulateur.getDateCourante());
            }
            else {
                events.get(events.size() - 1).setNext(intervention);
            }
            events.add(intervention);
            simulateur.ajouterListEvenement(events);



        }
        catch (Exception e){

            e.printStackTrace();
            System.out.println("ERREUR LORS DE LA GENEATION DE LA sequence" +e.getMessage());


        }





    }


    /**
     *
     * @return un boolean qui permet de vérifier si la date du simulateur est un multiple du nombre d'étapes
     * (Conditions d'exécution)
     */



    private  boolean check_etape(long date_courante){
        return (date_courante-this.date_0)%this.step==0;
    }



    @Override
    public void reset() {
        this.tous_les_incendies_eteints=false;
        this.robots_indisponibles.clear(); // On vide la liste des robots indisponibles
    }

}
