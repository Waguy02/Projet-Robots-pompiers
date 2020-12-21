package simulateur.strategie;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Matérialise un graphe dont les noeuds sont de type T
 * @param <T>
 */
public class Graph<T> {

    private HashMap<T,Noeud<T>> noeuds=new HashMap<>();

    private List arcs=new ArrayList();


    public void ajouterNoeud(Noeud<T> noeud){
        this.noeuds.put(noeud.getLabel(),noeud);
    }

    public boolean ajouterArc(Arc arc){

        if(!(this.noeuds.containsKey(arc.getA().getLabel())&&this.noeuds.containsKey(arc.getB().getLabel())))return false;

        this.arcs.add(arc);
        return true;
    }


    public Noeud<T> getNoeud(T id){
        return this.noeuds.get(id);
    }

    public HashMap<T, Noeud<T>> getNoeuds() {
        return noeuds;
    }

    public void setNoeuds(HashMap<T, Noeud<T>> noeuds) {
        this.noeuds = noeuds;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public void setArcs(List<Arc> arcs) {
        this.arcs = arcs;
    }




    public ArrayList<Arc> getArcVoisin(Noeud<T> A){
        ArrayList<Arc> result=new ArrayList<>();

        for(Arc arc :this.getArcs()){
            if(arc.getA()==A){
                result.add(arc);
            }


        }
        return  result;


    }



    public void setNoeudAdjencies(){
        for( Noeud noeud:this.getNoeuds().values()){

            noeud.setAdjencies(getArcVoisin(noeud));
        }


    }

    /**
     *
     * @param depart
     * @param destination
     * @return le plus court chemin à l'aide de l'aide de l'algo de djisktra
     */
    public ArrayList<Noeud> getMinPath(Noeud depart,Noeud destination){
        ArrayList<Noeud> shortestPath=new ArrayList<>();
        if(!(this.getNoeuds().containsKey(depart.getLabel())&&this.noeuds.containsKey(destination.getLabel()))){

            return  shortestPath;

        }
        for(Noeud noeud:this.noeuds.values()){
            noeud.setDistance(Double.POSITIVE_INFINITY/2);
        }


        /***
         * Implémentation de djisktra pour les plus courts chemins
         */



       depart.setDistance(0);
        Set<Noeud> settledNodes=new HashSet<>();
        Set<Noeud> unsettledNodes=new HashSet<>();


        unsettledNodes.add(depart);



        while(unsettledNodes.size()!=0){

            /**
             *
             */
            Noeud currentNoeud=min_noeud(unsettledNodes);

            unsettledNodes.remove(currentNoeud);

            for(Object o: currentNoeud.getAdjencies()){
                Arc arc=(Arc)o;

                double poids=arc.getCout();

                if(!settledNodes.contains(arc.getB())){

                    calculateMinimumDistance(arc.getB(), poids, currentNoeud);
                    unsettledNodes.add(arc.getB());

                }

            }
            settledNodes.add(currentNoeud);








        }

        destination.getShortestPath().add(destination);
        return destination.getShortestPath();



    }




    private Noeud min_noeud(Collection<Noeud> noeuds){

        Noeud noeud_min=noeuds.iterator().next();
        for(Noeud noeud:noeuds){
            if(noeud.getDistance()<noeud_min.getDistance())noeud_min=noeud;
        }
        return noeud_min;

    }


    private void calculateMinimumDistance(Noeud currentNoeud,     double poids, Noeud sourceNoeud) {
        double sourceDistance = sourceNoeud.getDistance();
        if (sourceDistance + poids < currentNoeud.getDistance()) {
            currentNoeud.setDistance(sourceDistance + poids);
            ArrayList<Noeud> shortestPath = new ArrayList(sourceNoeud.getShortestPath());
            shortestPath.add(sourceNoeud);
            currentNoeud.setShortestPath(shortestPath);
        }
    }



}
