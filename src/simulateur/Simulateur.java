package simulateur;

import gui.GUISimulator;
import gui.ImageElement;
import gui.Simulable;
import models.IGraphic;
import models.incendie_environnment.Case;
import models.incendie_environnment.DonneeSimulation;
import models.incendie_environnment.Incendie;
import models.robots.Robot;
import simulateur.evenements.Evenement;
import simulateur.strategie.ChefPompier;

import java.util.*;

public class Simulateur implements Simulable{

    /***
     * Utilisation des collections, définition d'une methode compareTo() dans la classe Evenement
     */

    /**d
     *GestionnaireEvenement contient la liste ordonnée des évenements du simulateur (ordonnées suivant la date d'execution)
     *
     *
     * private GestionnaireEvenement gestionnaireEvenement= new GestionnaireEvenement()
     */







    public Simulateur(DonneeSimulation donneeSimulation,GUISimulator gui){

        this.donneeSimulation=donneeSimulation;

        this.guiSimulator=gui;

        this.guiSimulator.setSimulable(this);


    }


    /**
     * La liste d'évènements à exécuter
     */
    private ArrayList<Evenement> listeEvenements = new ArrayList<>();

    /**
     * La liste des évènements déjà executés
     */
    private List<Evenement> listeEvenementsExecutes =new ArrayList<>();


    /**
     * La liste des initialisations des évènements permettant de faire le restart
     * Pour chaque entrée <Evenement e, Long date> de cette liste, date correspond à la date d'initialisation de l'événement
     *
     */
    private HashMap<Evenement,Long> listeInitialisationsEvenements = new HashMap<>();




    public ArrayList<Evenement> getListeEvenements() {
        return listeEvenements;
    }

    public void ajouteEvenement(Evenement e) {
        listeEvenements.add(e);
        e.setSimulateur(this);
    }


    /**
     * Permet d'ajouter un evenement à la liste des évènements
     * @param list
     */
    public void ajouterListEvenement(List<Evenement> list){
        for (Evenement event:list){
            this.ajouteEvenement(event);
        }
    }


    @Override
    /**
     * Permet d'executer l'étape suivante du simulateur; Si le simulateur possède un chef pompier, ce dernier s'execute
     */
    public void next() {
        this.incrementeDate();

        if(this.chefPompier!=null)this.chefPompier.run();//On execute la stratégie du chef pompier si ce derneir exsite


        if (!this.simulationTerminee()) {
            System.out.println("Date courante :"+this.getDateCourante());
            boolean peut_executer=true;
            while(peut_executer) {
                /***
                 * MISE A JOUR DE L'ORDRE :
                 */
                Evenement e= listeEvenements.iterator().next();
                for(Evenement event: listeEvenements){  if (event.getDate()<e.getDate()){e=event;}
                }
                if (e.getDate() <this.date_courante) {
                    e.executeAndUpdateNext();
                    listeEvenements.remove(e); // Retrait de l'évènement de la liste d'évenements à éxecuter
                    listeEvenementsExecutes.add(e);// Ajout de l'évenement dans la liste d'évènements déjà exécutés
                    System.out.println("\n ETAT DES EVENEMENTS : \n("+"Courant :"+e+"\nSuivant :"+((e.getNext()!=null)?e.getNext():"Non défini") );
                }else{
                       peut_executer=false; }
                    if(this.simulationTerminee()){
                       System.out.println("Simulation terminée");
                       break;
                   }
            }

        }
        draw();

    }

    @Override
    public void restart()
    {
        /***
         * Il faut enregistrer l'etat initial du simulateur (les positions des robots...)
         * et on revient à cet état initial
         */
        System.out.println("Restarting");
        try {

            /**
             * Réinitialisation des robots et des incendies
             */

            for (Robot robot:this.donneeSimulation.getRobots()){
                robot.reset();;
            }
            for (Incendie incendie:this.donneeSimulation.getIncendies().values()){
                incendie.reset();
            }

            this.date_courante=0;

            /**
             * S'il n y' a pas de chef pompier, on réinitiliase les anciens évènements
             */
            if(this.chefPompier!=null){
                this.chefPompier.reset();//On réinitiale le chef pompier
                this.listeEvenementsExecutes.clear();
                this.listeEvenements.clear();

            }
            else{
                /***
                 * Réinitialisation des dates
                 */
                for(Evenement e:this.listeEvenements){
                    if(this.listeInitialisationsEvenements.containsKey(e)){
                        e.calculateDate(this.listeInitialisationsEvenements.get(e));

                    }
                    else{
                        e.setDate(Long.MAX_VALUE-100);
                    }

                }



            }

            this.draw();

        }
        catch (Exception e){
            System.out.println("Erreur de lors de la réinitilisation de la simulation : "+e.getMessage());

        }
        /**
         *
         *
         */
    }

    private GUISimulator guiSimulator;

    private long date_courante;
    DonneeSimulation donneeSimulation;

    private void incrementeDate(){
        this.date_courante++;
    }
    private boolean simulationTerminee() {
        return (listeEvenements.isEmpty());
    }


    /**
     * Affiche tous les éléments de la donnée simulation
     */
    public void draw()
    {


        this.guiSimulator.reset();



        for(int i=0;i<this.donneeSimulation.getCarte().getNbLignes();i++){

            for(int j=0;j<this.donneeSimulation.getCarte().getNbColonnes();j++){


                Case _case =this.donneeSimulation.getCarte().getCases()[i][j];


                this.drawElement(this.guiSimulator,_case);

            }


        }


        for(Robot robot:this.donneeSimulation.getRobots()){

            this.drawElement(this.guiSimulator,robot);

        }

           for(Incendie incendie:this.donneeSimulation.getIncendies().values()){

            this.drawElement(this.guiSimulator,incendie);
        }




    }


    /**
     * Permet de dessiner un élément graphique (Implémentant IGraphic)
     *
     * @param guiSimulator
     * @param element, un element affichable IGraphic telle qu'un robot, une case ou un incendie
     */
    public  void drawElement(GUISimulator guiSimulator, IGraphic element) {

        String file = element.getFileName();
        Case _case = element.getPosition();
        int x = _case.getLigne(), y = _case.getColonne();

        double nb_lignes=this.donneeSimulation.getCarte().getNbLignes(),nb_col=this.donneeSimulation.getCarte().getNbColonnes();

        int taille_x=(int)(Constantes.RESOLUTION/(1.2*nb_lignes)),taille_y=(int)(Constantes.RESOLUTION/(1.05*nb_col));

        ImageElement item = new ImageElement(y * taille_y, x * taille_x, file,
                taille_y, taille_x, null);

        guiSimulator.addGraphicalElement(item);


    }




    private ChefPompier chefPompier=null;

    public void setChefPompier(ChefPompier chefPompier) {
        this.chefPompier = chefPompier;
    }






    public List<Evenement> getListeEvenementsExecutes() {
        return listeEvenementsExecutes;
    }

    public HashMap<Evenement, Long> getListeInitialisationsEvenements() {
        return listeInitialisationsEvenements;
    }

    public void setListeInitialisationsEvenements(HashMap<Evenement, Long> listeInitialisationsEvenements) {
        this.listeInitialisationsEvenements = listeInitialisationsEvenements;
    }



    public long getDateCourante(){
        return this.date_courante;
    }

    public DonneeSimulation getDonneeSimulation() {
        return donneeSimulation;
    }

    public void setDonneeSimulation(DonneeSimulation donneeSimulation) {
        this.donneeSimulation = donneeSimulation;
    }

    /**
     *
     * @return La liste des robots disponibles (qui ne sont pas affectés à un évènement)
     */


}
