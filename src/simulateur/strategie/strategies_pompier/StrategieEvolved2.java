package simulateur.strategie.strategies_pompier;

import models.incendie_environnment.Case;
import models.incendie_environnment.DonneeSimulation;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.evenements.EvenementDeplacement;
import simulateur.evenements.EvenementIntervention;
import simulateur.strategie.CalculateurChemin;
import simulateur.strategie.ChefPompier;
import simulateur.strategie.CheminRobot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static simulateur.strategie.ChefPompier.getIncendiesVacants;
import static simulateur.strategie.ChefPompier.getRandomIncendieVacant;
import static simulateur.strategie.ChefPompier.getRandomRobot;

public class StrategieEvolved2 implements StrategiePompier {

    // La fréquence de l'affectation des robots
    long step;

    // la date initiale des périodes
    long date_0=1;

    boolean tous_les_incendies_eteints=false; // Stocke la valeur vrai si tous les incendies ont été eteints et stoppe les executions


    public StrategieEvolved2(long date_0, long step) {
        this.step = step;
    }


    public void execute(ChefPompier chefPompier, long date_courante) {
        if(tous_les_incendies_eteints)return;

        System.out.println("Date courante :"+date_courante);
        // La date courante ne correspond pas à la bonne période, on sort de la boucle
        if(!this.check_etape(date_courante))return;


        Simulateur simulateur=chefPompier.getSimulateur();
        DonneeSimulation donneeSimulation=simulateur.getDonneeSimulation();




        Robot robot_elu=null;

        Incendie incendie=null;
        int ctr=0;


        // On récupère tous les incendies non éteints
        ArrayList<Incendie> incendies_candidats= new ArrayList<>(donneeSimulation.getIncendiesNonEteint().values());

        if(incendies_candidats.isEmpty()){
            this.tous_les_incendies_eteints=true;

            // On vide la liste des évènements du simulateur car il n' y a plus d'incendies

            simulateur.getListeEvenements().clear();
            System.out.println("Tous les incendies ont été eteints : Felicitations");
            return;
        }

        do {

            // Aucun incendie candidat
            if(incendies_candidats.isEmpty()){
                break;
            }

            int index=(int)Math.random()*incendies_candidats.size();
            incendie=incendies_candidats.get(index);

            boolean robot_founded = false;

            ArrayList<Robot> robots_candidats=new ArrayList<>();
            for(Robot r:donneeSimulation.getRobots()) {
                // Si le robot est libre, non vide et la position de l'incendie n'est pas dans la liste des positions inatteignables du robot
                if(!r.est_occupe(simulateur)&&!r.estVide() &&!(r.getPosition_unreachable().contains(incendie.getPosition()))){

                    robots_candidats.add(r);
                }

            }



            // On récupère le robot qui atteindra la position de l'incendie ainsi que le chemin associé
            CheminRobot cheminRobotOptimal=cheminRobotOptimal(robots_candidats,incendie,date_courante);


            if(cheminRobotOptimal==null){
                System.out.println("Aucun chemin optimal;");
                robot_elu=null;
            }
            else{
                robot_elu=cheminRobotOptimal.getRobot();
            }


            if (robot_elu == null) {
                System.out.println("Aucun robot n'est diponble Pour cet incendie  \n" );
                incendies_candidats.remove(incendie);
                incendie=null;
                continue;

            }



            try {
                /**
                 * On récupère la séquence précédemment créée
                 */
                CheminRobot cr=cheminRobotOptimal;
                System.out.println("Distnace/durée du voyaege : "+cr.getDistance_totale()+" : "+cr.getDuree_totale());
                List<Evenement> events=cr.getEvents();

                // On génère la séquence d'intervention avec la vérification de feu eteint activée

                int volume_inter=Math.min(incendie.getNbLitres(),robot_elu.getVolReservoir()); // On détermine le volume à verser

                List<EvenementIntervention> sequence_intervention=EvenementIntervention.genererSequenceIntervention(robot_elu,volume_inter,true,true,-1);
                // On passe la valeur -1 pour ne pas calculer la date de la première intervention;

                if(!sequence_intervention.isEmpty()) {
                    EvenementIntervention intervention1 = sequence_intervention.get(0); // On récupère la première intervention
                    EvenementIntervention intervention_last = sequence_intervention.get(sequence_intervention.size() - 1); //on récupère la dernière intervention

                    // On active le mode auto_remplissage sur la dernière intervention
                    intervention_last.setAuto_remplissage(true);



                    if (events.isEmpty()) {
                        intervention1.calculateDate(simulateur.getDateCourante());
                    } else {
                        events.get(events.size() - 1).setNext(intervention1);// on lit la première intervention au premier déplacement
                    }
                    events.addAll(sequence_intervention); // On ajoute la séquence d'évènements intervention au simulateur

                }
                simulateur.ajouterListEvenement(events);



            }
            catch (Exception e){

                e.printStackTrace();
                System.out.println("ERREUR LORS DE LA GENEATOIN DE LA SEQUENCE" +e.getMessage());


            }

        }
        while(incendie==null);


        if(incendie==null){
            return;
        }

        System.out.println("INCENDIE PRIS EN CHARGE : "+incendie+" par le robot"+robot_elu );




    }

    @Override
    public void reset() {
        this.tous_les_incendies_eteints=false;
    }


    /**
     *
     * @param robot_candidats
     * @param incendie
     * @return Le robot optimal pour un incendie et un ensemble de robots candidats;
     */
    private CheminRobot cheminRobotOptimal(ArrayList<Robot> robot_candidats,Incendie incendie,long t0){

        // On génère les chemins robot pour chacun des robots



        ArrayList<CheminRobot > cheminRobotsList=new ArrayList<>();

        for(Robot robot:robot_candidats){

            try {
                CheminRobot cr = CalculateurChemin.generateSequencePPC(robot, incendie.getPosition(), t0);
                for(Evenement event:cr.getEvents()){
                    event.setIncendie_cible(incendie);
                }
                cheminRobotsList.add(cr);
            }
            catch(Exception e){
                System.out.println("Erreur : le robot " +robot +" ne peut pas atteindre l'incendie : "+ e.getMessage());
                robot.getPosition_unreachable().add(incendie.getPosition());// La position de l'incidie n'est pas atteignable par ce robot
            }
        }

        if (cheminRobotsList .isEmpty())return  null;


        CheminRobot cr_min=cheminRobotsList.get(0);
        for(CheminRobot cr:cheminRobotsList){
            if(cr.getDistance_totale()<cr_min.getDistance_totale()){
                cr=cr_min;
            }

        }

        return  cr_min;

    }



    private  boolean check_etape(long date_courante){
        return (date_courante-this.date_0)%this.step==0;
    }
}
