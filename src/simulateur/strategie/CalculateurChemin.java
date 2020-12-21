package simulateur.strategie;

import models.enums.Direction;
import models.incendie_environnment.Carte;
import models.incendie_environnment.Case;
import models.robots.Robot;

import simulateur.evenements.Evenement;
import simulateur.evenements.EvenementDeplacement;

import java.security.cert.Extension;
import java.util.*;


/**
 * Classe contenant toutes les méthodes associées au calcul de plus court chemin.
 */
public class CalculateurChemin {


    /**
     * @param robot
     * @param destination
     * @return Séquence d'évenements de déplacement élémenataire pour un chemin donnée;
     */




    public static CheminRobot generateSequencePPC(Robot robot,Case destination,long t0) throws  Exception
    {
        System.out.println("\n \n Generating min path from :"+robot.getPosition()+" to "+destination);
        Carte carte=robot.getDonneeSimulation().getCarte();



        ArrayList<Case>  chemin_optimal=genererPlusCourtChemin(robot,destination);

        if(chemin_optimal.isEmpty())throw new Exception("Chemin vide");

        ArrayList<Evenement> sequenceDeplacement=new ArrayList<>();

        Case A,B;

        for (int i=0;i<chemin_optimal.size()-1;i++){



            A=chemin_optimal.get(i);
            B=chemin_optimal.get(i+1);

            for(Direction direction:Direction.values()){


                if (carte.voisinExiste(A,direction)&&carte.getVoisin(A,direction)==B){

                    sequenceDeplacement.add(new EvenementDeplacement(robot,direction));
                    if(i>0){
                        sequenceDeplacement.get(i-1).setNext(sequenceDeplacement.get(i));
                    }



                }
            }



        }

        CheminRobot cr=new CheminRobot(robot,robot.getPosition(),destination);

        if(!sequenceDeplacement.isEmpty()){
            sequenceDeplacement.get(0).calculateDate(t0);
        }


        cr.setEvents(sequenceDeplacement);
        cr.setCases_chemin(chemin_optimal);
        calculateDistanceTime(cr);

        if(cr.getDistance_totale()==0)throw new Exception("Impossible d'obtenir ce chemin");


        return  cr;




    }


    /**
     *
     *
     * @param robot
     * @param chemin
     * @param t0
     * @return  Une séquence de déplacmeents associé au chemin à  parcourir par le robot à partir de la date référence T0
     * @throws Exception
     */

    public  static CheminRobot generateSequenceDeplacement(Robot robot, ArrayList<Case> chemin,long t0) throws Exception
    {

        Carte carte=robot.getDonneeSimulation().getCarte();
        ArrayList<Case>  chemin_optimal=chemin;
        Case destination=chemin_optimal.get(chemin_optimal.size()-1);
        ArrayList<Evenement> sequenceDeplacement=new ArrayList<>();

        Case A,B;


        for (int i=0;i<chemin_optimal.size()-1;i++){



            A=chemin_optimal.get(i);
            B=chemin_optimal.get(i+1);

            for(Direction direction:Direction.values()){


                if (carte.voisinExiste(A,direction)&&carte.getVoisin(A,direction)==B){

                    sequenceDeplacement.add(new EvenementDeplacement(robot,direction));
                    if(i>0){
                        sequenceDeplacement.get(i-1).setNext(sequenceDeplacement.get(i));
                    }



                }
            }



        }

        CheminRobot cr=new CheminRobot(robot,robot.getPosition(),destination);

        if(!sequenceDeplacement.isEmpty()&&t0>=0){
            sequenceDeplacement.get(0).calculateDate(t0);
        }


        cr.setEvents(sequenceDeplacement);
        cr.setCases_chemin(chemin_optimal);
        calculateDistanceTime(cr);



        return  cr;


    }


    /**
     *
     * @param robot
     * @param destination
     * @return Génère un plus cours chemin  à partir de la position du robot jusqu"à  la destination
     * @throws Exception
     */
    public static ArrayList<Case> genererPlusCourtChemin(Robot robot, Case destination) throws Exception{

        ArrayList<Case> PPC=new ArrayList();
        Graph<Case> graph=graphFromRobot(robot);

        System.out.println(graph.getNoeud(destination));

        ArrayList<Noeud> shortedPast=graph.getMinPath(graph.getNoeud(robot.getPosition()),graph.getNoeud(destination));

        for(Noeud noeud:shortedPast){

            PPC.add( (Case) noeud.getLabel());
        }


    return  PPC;



    }


    /**
     *
     * @param robot
     * @return Le chemin le plus court vers un point de remplissage lorsque ce dernier existe
     * à l'aide d'un parcours en profondeur; Retounre   null le cas contraire
     *
     */
    public static ArrayList<Case> cheminOptimalRemplissage(Robot robot){

        boolean terminated=false;


        Graph graph=null;
        try {
             graph = graphFromRobot(robot);
        }
        catch (Exception e){
            System.out.println("Erruer lors de la création du graphe :"+e.getMessage());
        }

        Noeud initialPosition=graph.getNoeud(robot.getPosition());
        Arc arc_0=new Arc(initialPosition,initialPosition,0);
        Chaine initiale=new Chaine(arc_0);
        PriorityQueue<Chaine>  liste_parcours=new PriorityQueue<>();

        // LinkedList<Chaine> liste_parcours=new LinkedList<>();

        liste_parcours.add(initiale);

        //Les noeuds par lesquels on peut pas passer
        HashSet<Noeud> noueds_marques=new HashSet<>();


        // Pour chaque noeud, la distance à partir de la racine;
        HashMap<Noeud,Double> distance_noeuds=new HashMap<>();

        while(!terminated){


            Chaine chaine_min=liste_parcours.peek();

            Noeud head=chaine_min.getFin();

            if((robot.peutSeRemplir((Case)head.getLabel()))){

                ArrayList<Case> parcours_min=new ArrayList<>();

                 Chaine current_chaine=chaine_min;
                 while (current_chaine!=null){
                     parcours_min.add((Case)current_chaine.head.getB().getLabel());
                     current_chaine=current_chaine.prec;                 }

                Collections.reverse(parcours_min);


                return parcours_min;
            }

            else{
                // On crée de nouvelles chaines à partir la chaine minimale courante



                ArrayList<Arc> good_adjencies=new ArrayList<>();
                for(Object obj:head.getAdjencies()){
                    Arc arc=(Arc)obj;
                    if( noueds_marques.contains(arc.getB()))continue;

                    if(distance_noeuds.containsKey(arc.getB())){

                        double current_dist=distance_noeuds.get(arc.getB());
                        //Si le noeud a pu être atteint en une durée inférieure
                        if(chaine_min.cout+arc.getCout()>=current_dist)continue;
                    }

                        distance_noeuds.put(arc.getB(),chaine_min.cout+arc.getCout());

                        good_adjencies.add(arc);

                }


                ArrayList<Chaine> extensions=chaine_min.extensions(good_adjencies);


                if(!extensions.isEmpty()){


                    liste_parcours.addAll(extensions);
                }

                else{
                    //Si il n'y a pas d'extensioon , le noeud courant doit etre banni (impasse)
                    noueds_marques.add(chaine_min.getFin());

                    if(liste_parcours.isEmpty()){
                        terminated=true;
                    }
                }

                liste_parcours.remove(chaine_min);



            }

        }


        // On retourne null si on a pas trouver le chemin optimal
        System.out.println("Pas d'initinéraies jusqu'a l'eau");
        return  null;



    }




    /**
     *
     *
     * @param robot
     * @return Un graphe dont les noeuds sont les cases et les coûts des arcs sont les durées de déplacememt sur  la case d'arrivée
     * <=>Cout( A-B) = Taille_case/vitesse_robot(B)
     */
    private static  Graph<Case> graphFromRobot(Robot robot) throws  Exception{
        Graph<Case> graph=new Graph<Case>();
        Carte carte=robot.getDonneeSimulation().getCarte();

        for (int i=0;i<carte.getNbLignes();i++)for(int j=0;j<carte.getNbColonnes();j++)
        {
            Case currentCase=carte.getCase(i,j);

            Noeud<Case> noeud=new Noeud<>(carte.getCase(i,j),poids(currentCase,robot));

            graph.ajouterNoeud(noeud);

        ;}

        for (int i=0;i<carte.getNbLignes();i++){
            for(int j=0;j<carte.getNbColonnes();j++){

                Case currentCase=carte.getCase(i,j);
                Noeud A=graph.getNoeud(currentCase);


                        for(Direction direction:Direction.values()){
                        if(carte.voisinExiste(currentCase,direction)) {


                            Case voisin= carte.getVoisin(currentCase,direction);

                            if(robot.peutAtteindre(voisin)) {
                                Noeud B = graph.getNoeud(voisin);

                                double pB = voisin.getTaille() / (robot.getVitesse(voisin.getNature()));

                                /** On prend le coût d'un arc égal à la durée de temps passé sur la case de destination de ce dernier
                                 * */
                                Arc arc = new Arc(A, B, pB);
                                graph.ajouterArc(arc);

                            }

                        }



                    }
                }
        }
        graph.setNoeudAdjencies();





        return  graph;

    }


    /**
     *
     * @param position
     * @param robot
     * @return Le poids d'un noeud comme temps de parcours du robot sur ce noeud
     */
    private static double poids (Case position,Robot robot){
        return  position.getTaille()/(robot.getVitesse(position.getNature()));

    }


    /***
     * Permet de mettre à jour un chemin robot en calculant sa longueur totale et la durée totale
     * @param cr
     */
    public static void calculateDistanceTime(CheminRobot cr){

        ArrayList<Case> chemin=cr.getCases_chemin();
        Robot robot=cr.getRobot();
        double distance=0;
        double time=0;

        for(int i =0;i<chemin.size()-1;i++){

            Case A=chemin.get(i);
            Case B=chemin.get(i+1);


            distance+=A.getTaille();

            time+=A.getTaille()/robot.getVitesse(A.getNature());


        }
        cr.setDistance_totale(distance);
        cr.setDuree_totale(time);




    }







}


class Chaine implements Comparable<Chaine>{

    // public ArrayList<Noeud> noeuds = new ArrayList<>();

    public Chaine prec;
    public Arc head;
    public double cout = 0;


    // retourne vrai si la chaine est un noeud et faux sinon
    public boolean est_racine(){
        return this.prec==null;
    }




    public Chaine() {

    }

    public Chaine(Arc head) {

        this.prec=null;
        this.head=head;
    }


    public Chaine(Arc head,Chaine prec) {

        this.prec=prec;
        this.head=head;
    }

    public Noeud getDebut() {
        if(this.est_racine())return  this.head.getA();
        else return  this.prec.getDebut();


    }


    public Noeud getFin() {
        return  this.head.getB();
    }


    public void ajouterArc(Arc arc) {
        if (arc.getA() != this.getFin()) return;

        this.prec=this.clone();
        this.head=arc;

        this.cout += arc.getCout();
    }
    private  boolean contains(Noeud noeud){

        if(this.est_racine()){
            return this.head.getB()==noeud;
        }
        if(this.head.getB()==noeud)return  true;
        else return  prec.contains(noeud);

    }


    public Chaine clone() {
        Chaine copy = new Chaine();
        copy.head=this.head;;
        copy.cout = this.cout;
        copy.prec=this.prec;


        return copy;
    }


    public ArrayList<Chaine> extensions(Collection<Arc> arcs) {
        ArrayList<Chaine> extensions = new ArrayList<>();
        for (Arc arc : arcs) {
            if(!(arc.getA()==this.getFin()))continue;

            Noeud n=arc.getB();

            // on vérifie si le noeud est déjà dans la chaine
            if(this.contains(n))continue;;


                Chaine chaine = this.clone();
                chaine.ajouterArc(arc);
                extensions.add(chaine);




        }
        return extensions;

    }


    @Override
    public int compareTo(Chaine o) {
        if(this.cout>o.cout)return 1;
        if(this.cout<o.cout)return -1;
        return 0;
    }



}

