import gui.GUISimulator;
import io.LecteurDonnees;
import models.incendie_environnment.DonneeSimulation;
import models.robots.Robot;
import simulateur.Constantes;
import simulateur.Scenario;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.strategie.CalculateurChemin;
import simulateur.strategie.ChefPompier;
import simulateur.strategie.CheminRobot;
import simulateur.strategie.strategies_pompier.StrategieEvolved1;
import simulateur.strategie.strategies_pompier.StrategiePompier;
import simulateur.strategie.strategies_pompier.StrategieTriviale;

import javax.sound.midi.Sequence;
import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

// Permet de tester la stratégie évoluée
public class TestStrategie2 {


    public static void main (String [] args){
        String chemin_carte=Constantes.CARTE_SUJET ;// Test avec notre carte custom (Passage à l'échelle)
//        String chemin_carte=Constantes.CARTE_MUSHROOM; // Test avec la carte mushRoom

        try {

            DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
            GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
            Simulateur simulateur=new Simulateur(ds,gui);


                /**
             * La stratégie évoluée avec une période de 1 et une date initiale égale à 0; La période de la stratégie est de 1 seconde;
             */
            StrategiePompier strategiePompier=new StrategieEvolved1(1,1);


            simulateur.setChefPompier(new ChefPompier(simulateur,strategiePompier));



        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }



}
