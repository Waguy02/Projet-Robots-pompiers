package simulateur.strategie;

import models.incendie_environnment.Case;
import models.robots.Robot;
import simulateur.evenements.Evenement;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe  permet d'encapsuler toutes les informations sur un chemin de robot  à partir d'une origine jusqu'à une destination
 * Les informations concernées sont : la distance totale, la séquence d'évènements Déplacements associée, la durée et la distance totale.
 *
 */
public class CheminRobot{

    Case origine,destination;
    Robot robot;

    double distance_totale;
    ArrayList<Case> cases_chemin;
    List<Evenement> events;
    double duree_totale;


    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public CheminRobot(Robot robot, Case origine, Case destination) {
        this.origine = origine;
        this.destination = destination;
        this.robot=robot;


    }

    public String toString(){

        String str=  "CHEMIN_ROBOT";
        String detail_noeuds="";

        for(Case _case:this.getCases_chemin()){
            detail_noeuds+="-->"+_case.toString();


        }
        str+="\n"+detail_noeuds
        +"\n Duree :"+this.duree_totale
        +"\n Distance : "+this.distance_totale;
        ;


        return str+"\n"+"\n";
    }


    public Case getOrigine() {
        return origine;
    }

    public void setOrigine(Case origine) {
        this.origine = origine;
    }

    public Case getDestination() {
        return destination;
    }

    public void setDestination(Case destination) {
        this.destination = destination;
    }

    public double getDistance_totale() {
        return distance_totale;
    }

    public void setDistance_totale(double distance_totale) {
        this.distance_totale = distance_totale;
    }

    public ArrayList<Case> getCases_chemin() {
        return cases_chemin;
    }

    public void setCases_chemin(ArrayList<Case> cases_chemin) {
        this.cases_chemin = cases_chemin;
    }

    public List<Evenement> getEvents() {
        return events;
    }

    public void setEvents(List<Evenement> events) {
        this.events = events;
    }

    public double getDuree_totale() {
        return duree_totale;
    }

    public void setDuree_totale(double duree_totale) {
        this.duree_totale = duree_totale;
    }
}
