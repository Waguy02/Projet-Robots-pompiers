package simulateur.evenements;

import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.Simulateur;

public abstract class Evenement implements Comparable<Evenement> {

    /**
     *
     * Lorsqu'on crée un évènement, on lui affecte une date infinie, ce n'est que plus tard que cette valeur est calculée par la méthode calculateDate
     */

    // On stocke une référence vers le simulateur
    protected Simulateur simulateur;
    protected long date=Long.MAX_VALUE-100;

    protected Robot robot;

    public Evenement(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return robot;
    }

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int compareTo(Evenement e) {
        if (this.date > date) {
            return 1;
        }
        if (this.date < e.date) {
            return -1;
        }
        return 0;
    }

    public abstract void execute();

    /**
     *
     * La méthode executeAndUpdateNext execute et met à la jour la date de l'évènement suivant
     */
    public void executeAndUpdateNext(){
        this.execute();

        // Si on vérifie que le feu est eteint
        if(this.verification_feu==true &&this.getNext()!=null){

            Incendie incendie=this.robot.getDonneeSimulation().getIncendies().get(robot.getPosition());
            // Si l'incidie existe et si l'évènement suivant est un evenementintervention
            if( incendie!=null&&this.getNext() instanceof  EvenementIntervention){


                // Si l'incendie est eteint on retire toutes les interventions qui suivent directement l'évènement courant
                if(incendie.estEteint()){
                    Evenement next=this.getNext();
                       Evenement after_next;
                    System.out.println( "Suppression des interventions suivantes car le feu est déjà éteint  (" +incendie+ "), désolé  : "+this.getRobot());

                    do {
                         after_next = next.getNext();
                         this.getSimulateur().getListeEvenements().remove(next);// On retire l'intervention du simulateur;

                        next=after_next;
                        if(next==null) break;


                    }
                    // On supprime toutes les évènements intervention qui suivent l'évènement courant car le feu est eteint
                    while (next instanceof EvenementIntervention);


                    this.setNext(after_next);


                }

            }

        }


        if(this.getNext()!=null)getNext().calculateDate(this.getDate());
    }


    private Evenement next;


    private Incendie incendie_cible;

    public Incendie getIncendie_cible() {
        return incendie_cible;
    }

    public void setIncendie_cible(Incendie incendie_cible) {
        this.incendie_cible = incendie_cible;
    }

    public Evenement getNext() {
        return next;
    }

    public void setNext(Evenement next) {
        this.next = next;
    }

    public Simulateur getSimulateur() {
        return simulateur;
    }

    public void setSimulateur(Simulateur simulateur) {
        this.simulateur = simulateur;
    }

    /**
     *
     * @param t0
     * Permet de calculer la date de fin d'un évnement à partir d'une référence t0
     * Initialement la date  fin d'un evenement instanciée est à +INFINITY.
     * Cette dernière n'est mise à jour que plus tard par la méthode next() de son prédécesseur
     *
     */
    public abstract void calculateDate(long t0);



    // Lorsque cet attribut vaut vrai:
    // Si l'évènement suivant est de type EvenementIntervention, le robot vérifie que l'incendie en la position courante n'est pas éteint,
    // Si c'est le cas, cet évènement est retiré du simulateur et du next de ce déplacement (Autrement dit le robot ne versera plus son eau)

    private    boolean verification_feu=false;




    public boolean isVerification_feu() {
        return verification_feu;
    }

    public void setVerification_feu(boolean verification_feu) {
        this.verification_feu = verification_feu;
    }












}