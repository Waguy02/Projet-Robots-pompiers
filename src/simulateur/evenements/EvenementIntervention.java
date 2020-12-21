package simulateur.evenements;

import models.enums.Direction;
import models.incendie_environnment.Case;
import models.incendie_environnment.DonneeSimulation;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Constantes;
import simulateur.Simulateur;
import simulateur.strategie.CalculateurChemin;
import simulateur.strategie.CheminRobot;

import java.util.ArrayList;
import java.util.List;

public class EvenementIntervention extends  Evenement{

    /* Lorsque cet attribut vaut true: le reservoir est vide à l'issue d'une intervention, le robot génère une séquence de déplacements
    suivie d'une remplissage au point de remplissage (berge ou point d'eau le plus proche) */
    private boolean auto_remplissage=false;




    private int volume_intervention;

    private Incendie incendie_courant;


    public boolean isAuto_remplissage() {
        return auto_remplissage;
    }

    public void setAuto_remplissage(boolean auto_remplissage) {
        this.auto_remplissage = auto_remplissage;
    }

    @Override
    public void execute() {
        DonneeSimulation ds=this.robot.getDonneeSimulation();
        Incendie incendie=ds.getIncendies().get(this.robot.getPosition());

        if(incendie==null)return;
        incendie.setNbLitres(incendie.getNbLitres()-this.volume_intervention);




        System.out.println("BILAN DE L INTERVENTION NOMBRE DE LITRE RESTANT POUR L INCENDIE :"+incendie.getNbLitres());
        this.robot.setVolReservoir(robot.getVolReservoir()-this.volume_intervention);



        // SI le robot a un reservoir vide  et que l'autoremplissage est activé le robot génère une séquence de déplacement remplissage;
        // ON modifie la liste chainée en créant un décalage

        if(this.auto_remplissage&&this.robot.estVide()){
            try {
                // Si il existe un plus court chemin vers un point de remplissage
                ArrayList<Case> ppc=CalculateurChemin.cheminOptimalRemplissage(this.robot);

                System.out.println("Mode autoRemplissage : en route pour le point de remplissage( "+this.robot+")");
                if (ppc != null) {

                    Simulateur simulateur=this.getSimulateur();

                    CheminRobot cr = CalculateurChemin.generateSequenceDeplacement(this.robot, ppc, -1);
                    // On passe la valeur -1 pour ne pas calculer la date du premier déplacement qui sera lié à l'intervention courante

                    // On récupère l'évènement suivant (s'il existe ou pas) l'intervention courante
                    Evenement next_of_this=this.getNext();


                    simulateur.ajouterListEvenement(cr.getEvents());



                    EvenementRemplissage event_remplissage=new EvenementRemplissage(this.robot);

                    simulateur.ajouteEvenement(event_remplissage); // On crée le remplissage

                    if(cr.getEvents().isEmpty())this.setNext(event_remplissage);

                    else{
                        this.setNext(cr.getEvents().get(0)); // On ajoute la séquence de déplacement
                        cr.getEvents().get(cr.getEvents().size()-1).setNext(event_remplissage); // On le positionne après la séquence de déplacement
                    }


                    event_remplissage.setNext(next_of_this);



                }

            }catch (Exception e){
                System.out.println("Le robot "+this.robot +" ne peut aller se remplir : "+e.getMessage());
                e.printStackTrace();

            }


        }

    }

    @Override
    public void calculateDate(long t0) {

        double vitesse_intervention=this.robot.getNbLitreInterventionParS();

        double duree_intervention=this.volume_intervention/vitesse_intervention;


        long nombre_etapes=(long)Math.ceil(duree_intervention/Constantes.DUREE_ETAPE);

        this.date=t0+nombre_etapes;

    }

    public EvenementIntervention(Robot robot, int vol) {
        super(robot);

        /**
         * On calcule le volume d'intervention en fonction de la capacité d'intervention unitaire du robot
         * et du volume du réservoir
         */
        this.volume_intervention=robot.calculerVolumeIntervention(vol);


    }

    public String toString(){


        return " Evenement intervention par " +this.getRobot().toString() +" à la date :"+this.getDate()+" volume : "+this.volume_intervention;

    }

    /**
     *
     * @param robot
     * @param volume_total
     * @param autoRemplissage permet d'activer ou non l'autoRemplissage sur chacune des interventions unitaires
     * @param verification_feu permet d'activer la vérification automatique de l'extension de feu à la fin de chaque intervention
     *        telle que définie dans la méthode executeAndupdateNext du simulateur
     * @param t0
      *@return Permet de générer une séquence d'évènements unitaires permettant de controler les dates de fins
      * et de limiter les gaspillages d'eau. On génère en quelque sorte une séquence de petis arrosages
     */
    public static List<EvenementIntervention> genererSequenceIntervention (Robot robot,int volume_total, boolean verification_feu,boolean autoRemplissage,long t0){

        List<EvenementIntervention> list_interventions =new ArrayList<>();

        int volume_unitaire=robot.getVolumeInterventionUnitaire();

        int volume_verse=0;

        EvenementIntervention intervention=null;

        while(volume_verse<volume_total) {
            int volume_restant=volume_total-volume_verse;

            int volume_intervention;
            if (volume_restant<volume_unitaire) {
                 volume_intervention=volume_restant; // On crée une intervention unitaire sur le volume restant
            }
            else{
                volume_intervention=volume_unitaire; // On crée une intervention untaire de volume unitaire

            }

            EvenementIntervention new_intervention = new EvenementIntervention(robot, volume_intervention);
            new_intervention.setAuto_remplissage(autoRemplissage);// On active l'autoRemplissage le cas écheant
            if(intervention!=null)intervention.setNext(new_intervention); // On lie les interventions entre elles

            intervention=new_intervention;
            intervention.setVerification_feu(verification_feu); // On vérifiera si le feu est eteint à l'issu de l'intervention si verification_feu vaut true

            volume_verse+=volume_intervention;

            list_interventions.add(intervention);

        }




        if(t0>=0)list_interventions.get(0).calculateDate(t0);

        System.out.println("Interventions générées pour l'incendie " +" : "+list_interventions.size()+" interventions unitaires");
        return list_interventions;


    }


}
