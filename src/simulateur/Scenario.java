package simulateur;

import models.enums.Direction;
import models.incendie_environnment.Case;
import models.incendie_environnment.DonneeSimulation;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.evenements.Evenement;
import simulateur.evenements.EvenementDeplacement;
import simulateur.evenements.EvenementIntervention;
import simulateur.evenements.EvenementRemplissage;
import simulateur.strategie.CalculateurChemin;
import simulateur.strategie.CheminRobot;

import java.util.*;

/**
 * Cette classe permet de matérialiser les scénarios
 * Un scénario est constitué d'une séquence d'évènements et d'un dictionnaire d'initialisation permettant de connaitre les
 * evenements dont les dates de debut sont connues pour faciliter l'opération de initialise
 */
public class Scenario {


    public List<Evenement> evenements;
 /**
     *Permet de stocker les évènements initiaux d'un scénario
     */
    public HashMap<Evenement,Long> initialisations=new HashMap<>();

    public void initialise(){
        for(Evenement evenement:this.evenements){
            Long event_t0=this.initialisations.get(evenement);

            if(event_t0==null)evenement.setDate(Long.MAX_VALUE-100);
            else{
                evenement.calculateDate(event_t0);
            }
        }
    }


    public Scenario(List<Evenement> evenements) {
        this.evenements = evenements;
    }


    /**
     * Déplacement du premier robot 4 fois vers le nord
     *
     * @param donneeSimulation
     * @return
     */
    public static Scenario scenario_0(DonneeSimulation donneeSimulation){

    Robot robot=donneeSimulation.getRobots().get(0);
    Case initialPosition=robot.getPosition();
    List<Evenement> events=new ArrayList<>();
        Direction[] dirs= {Direction.NORD,Direction.NORD,Direction.NORD,Direction.NORD};
        List<EvenementDeplacement> list0=EvenementDeplacement.genererSequenceDeplacement(robot,Arrays.asList(dirs),0);
        events.addAll(list0);

        /**
         * On calcule la date du premier déplacmeent
         */
        events.get(0).calculateDate(0);





        Scenario scenario1 =new Scenario(events);
        HashMap<Evenement,Long> inits=new HashMap<>();
        inits.put(events.get(0),(long)0);
        scenario1.initialisations=inits;
        scenario1.initialise();;





        return scenario1;



    }


    /**
     * Déplacer le robot et éteindre les flammes
     * @param donneeSimulation
     * @return
     */
    public static Scenario scenario_1(DonneeSimulation donneeSimulation){
        Robot robot=donneeSimulation.getRobots().get(1);
        List<Evenement> events=new ArrayList<>();

        /***
         * On effectue les déplacements jusqu'au point de remplissage
         */

        events.add(new EvenementDeplacement(robot,Direction.NORD));


        events.add(new EvenementIntervention(robot,robot.getVolReservoir()));


        Direction[] dirs1= {Direction.SUD,Direction.OUEST,Direction.OUEST};
        List<EvenementDeplacement> sequence_deplacement_1=EvenementDeplacement.genererSequenceDeplacement(robot,Arrays.asList(dirs1),Long.MAX_VALUE-100);
        events.addAll(sequence_deplacement_1);// On ajoute les déplacements au scénario



        EvenementRemplissage eventRemplissage=new EvenementRemplissage(robot);
        events.add(eventRemplissage); // On ajoute le remplissage à la liste des evenements à retourner

        Direction[] dirs2= {Direction.EST,Direction.NORD};
        List<EvenementDeplacement> sequence_deplacements_2=EvenementDeplacement.genererSequenceDeplacement(robot,Arrays.asList(dirs2),Long.MAX_VALUE-100);
        events.addAll(sequence_deplacements_2);



        Incendie incendie2=donneeSimulation.getIncendies().get(robot.getPosition());

        events.add(new EvenementIntervention(robot,robot.getVolReservoir()));





        events.get(0).calculateDate(0);


        for(int i=0;i<events.size()-1;i++)events.get(i).setNext(events.get(i+1));


        Scenario scenario2=new Scenario(events);
        scenario2.initialisations.put(events.get(0),(long)0);


        return scenario2;







    }


    /**
     * Permet de tester le calcul des plus courts chemins
     *
     * @param donneeSimulation
     * @return
     */
    public static Scenario scenario_2(DonneeSimulation donneeSimulation,int lig,int col){

        Robot robot=donneeSimulation.getRobots().get(0);
        Robot robot2=donneeSimulation.getRobots().get(1);
        Robot robot3=donneeSimulation.getRobots().get(2);
        CheminRobot cr=null,cr2=null,cr3=null;
        Case destination=robot.getDonneeSimulation().getCarte().getCase(lig,col);
        List<Evenement> evenements=new ArrayList<>();
        Scenario scenario2;

        try {
             cr = CalculateurChemin.generateSequencePPC(robot, destination, 0);
             cr2=CalculateurChemin.generateSequencePPC(robot2,destination,0);
             cr3=CalculateurChemin.generateSequencePPC(robot3,destination,0);

             System.out.println(cr2.toString());
             evenements=cr.getEvents();
             evenements.addAll(cr2.getEvents());
             evenements.addAll(cr3.getEvents());


             scenario2=new Scenario(evenements);
             try {
                 scenario2.initialisations.put(evenements.get(0), (long) 0);
                 scenario2.initialisations.put(cr2.getEvents().get(0), (long) 0);
                 scenario2.initialisations.put(cr3.getEvents().get(0), (long) 0);
             }catch (Exception e){


             }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            scenario2=new Scenario(evenements);
        }

        return  scenario2;



    }



}
