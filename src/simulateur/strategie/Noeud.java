package simulateur.strategie;

import java.util.ArrayList;
import java.util.List;

public class Noeud<T> {

    private T label;

    private double poids;

    private ArrayList<Arc> adjencies=new ArrayList<>();

    public ArrayList<Arc> getAdjencies() {
        return this.adjencies;
    }

    public void setAdjencies(ArrayList<Arc> adjencies) {
        this.adjencies = adjencies;
    }

    public Noeud(T label, double poids) {
        this.label = label;
        this.poids = poids;
    }

    public T getLabel() {
        return (T)label;
    }

    public void setLabel(T label) {
        this.label = label;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }


    private  double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Noeud> shortestPath=new ArrayList<>();

    public ArrayList<Noeud> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(ArrayList<Noeud> shortestPath) {
        this.shortestPath = shortestPath;
    }
}
