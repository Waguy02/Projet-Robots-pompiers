
import gui.GUISimulator;
import io.LecteurDonnees;
import models.incendie_environnment.DonneeSimulation;
import simulateur.Constantes;
import simulateur.Simulateur;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.zip.DataFormatException;

public class TestLecteurDonnees {



    public static void main(String[] args) {



        test(Constantes.CARTE_MUSHROOM);

    }
    public static void test(String chemin_carte){
     try {
         DonneeSimulation ds= LecteurDonnees.lire(chemin_carte);
         GUISimulator gui=new GUISimulator(Constantes.RESOLUTION,Constantes.RESOLUTION,Color.white);
         Simulateur simulateur=new Simulateur(ds,gui);


            simulateur.next();



        } catch (FileNotFoundException e) {
            System.out.println("fichier " + chemin_carte + " inconnu ou illisible");
        } catch (DataFormatException e) {
            System.out.println("\n\t**format du fichier " + chemin_carte + " invalide: " + e.getMessage());
        }
    }



}

