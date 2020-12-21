import gui.GUISimulator;
import io.LecteurDonnees;
import models.incendie_environnment.DonneeSimulation;
import simulateur.Constantes;
import simulateur.Scenario;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestScenarios {

   public static void main(String[] args){

//    testScenario_0(Constantes.CARTE_SUJET);
//    testScenario_1(Constantes.CARTE_SUJET);
    testScenario_2(Constantes.CARTE_DESERT);

   }

    /**
     * Permet de tester le scenario 1 décrit dans Scenario.Java sur uen carte dont le chemin est chemin_carte
     * @param chemin_carte
     */
    public static void testScenario_0(String chemin_carte){
        try {
            DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
            GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
            Simulateur simulateur=new Simulateur(ds,gui);

            Scenario scenario=Scenario.scenario_0(ds);
            simulateur.ajouterListEvenement(scenario.evenements);
            simulateur.setListeInitialisationsEvenements(scenario.initialisations);

            System.out.println("Fin de la création du scénario :");

            System.out.println("Séquence d'évenemnts");

            for( Evenement event:simulateur.getListeEvenements()){
                System.out.println(event.toString());


            }



            simulateur.draw();


        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }


    public static void testScenario_1(String chemin_carte){
        try {
            DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
            GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
            Simulateur simulateur=new Simulateur(ds,gui);
            Scenario scenario=Scenario.scenario_1(ds);
            simulateur.ajouterListEvenement(scenario.evenements);
            simulateur.setListeInitialisationsEvenements(scenario.initialisations);

//            System.out.println("Fin de la création du scénario :");
//
//            System.out.println("Séquence d'évenemnts");
//
//            for( Evenement event:simulateur.getListeEvenements()){
//                System.out.println(event.toString());
//
//
//            }
//


            simulateur.draw();


        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }


    public static void testScenario_2(String chemin_carte){
        try {
            DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
            GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
            Simulateur simulateur=new Simulateur(ds,gui);

            Scenario scenario=Scenario.scenario_2(ds,ds.getCarte().getNbLignes()-1,2);
            simulateur.ajouterListEvenement(scenario.evenements);
            simulateur.setListeInitialisationsEvenements(scenario.initialisations);


            System.out.println("\n Séquence d'évènements");

            for( Evenement event:simulateur.getListeEvenements()){
                System.out.println(event.toString());


            }



            simulateur.draw();


        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }



}
