import gui.GUISimulator;
import io.LecteurDonnees;
import models.incendie_environnment.DonneeSimulation;
import simulateur.Constantes;
import simulateur.Scenario;
import simulateur.Simulateur;
import simulateur.evenements.Evenement;
import simulateur.strategie.ChefPompier;
import simulateur.strategie.strategies_pompier.StrategiePompier;
import simulateur.strategie.strategies_pompier.StrategieTriviale;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestStrategie1 {


    public static void main (String [] args){
        String chemin_carte=Constantes.CARTE_MUSHROOM;
        int n=5;
        try {

            DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
            GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
            Simulateur simulateur=new Simulateur(ds,gui);

            /**
             * La stratégie triviale avec une période de 5 et une date initiale égale à 0;
             */
            StrategiePompier strategiePompier=new StrategieTriviale(1,5);


            simulateur.setChefPompier(new ChefPompier(simulateur,strategiePompier));



        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }



}
