package io;


import models.enums.NatureTerrain;
import models.incendie_environnment.*;
import models.robots.*;

import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;



/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affichées.
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des méthodes, inspirées de celles présentes
 * (ou non), qui CREENT les objets au moment adéquat pour construire une
 * instance de la classe DonneesSimulation à partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une méthode qui crée et retourne un objet
 * contenant toutes les données lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {


    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */
    public static DonneeSimulation lire(String fichierDonnees)
        throws FileNotFoundException, DataFormatException {



        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        Carte carte=lecteur.lireCarte();
        HashMap map_incendies=lecteur.lireIncendies(carte);
        ArrayList<Robot> map_robots=lecteur.lireRobots(carte);


        scanner.close();


        DonneeSimulation donneeSimulation=new DonneeSimulation(carte,map_incendies,map_robots);

        if(!donneeSimulation.verifierPositionRobots()) throw  new DataFormatException("Position de robot invalide ( Limite de la carte dépasée)");

        System.out.println("\n == Lecture terminee");



        return  donneeSimulation;
    }





    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees (String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte lireCarte() throws DataFormatException {
        Carte carte;
        ignorerCommentaires();


        try {


            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();


            int tailleCases = scanner.nextInt();	// en m



            carte=new Carte(nbLignes,nbColonnes,tailleCases);
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);




            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    Case _case=lireCase(lig, col);
                    //_case.setTaille(carte.getTailleCases());

                    try {
                        carte.setCase(lig, col, _case);
                    }
                    catch(Exception ex){

                        System.out.println("Coordonnées incorrecte de case");
                    }



                }
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle






    return carte;



    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private Case lireCase(int lig, int col) throws DataFormatException {
        Case _case=new Case(lig,col);

        ignorerCommentaires();
        System.out.print("Case (" + lig + "," + col + "): ");
        String chaineNature = new String();
        //		NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            //			NatureTerrain nature = NatureTerrain.valueOf(chaineNature);

            verifieLigneTerminee();

            System.out.print("nature = " + chaineNature);
            NatureTerrain nature=NatureTerrain.valueOf(chaineNature);
            _case.setNature(nature);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }

        System.out.println();

        return _case;
    }


    /**
     * Lit et affiche les donnees des incendies.
     */


    private HashMap<Case,Incendie> lireIncendies(Carte carte) throws DataFormatException {
        ignorerCommentaires();
        HashMap<Case,Incendie> map=new HashMap<>();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                Incendie incendie=lireIncendie(i,carte);

                map.put(incendie.getPosition(),incendie);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }

        return map;
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private Incendie lireIncendie(int i,Carte carte) throws DataFormatException {


        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        Incendie incendie;
        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }

             incendie=new Incendie(carte.getCase(lig,col),intensite);

            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
        return incendie;
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private ArrayList<Robot> lireRobots(Carte carte) throws DataFormatException {
        ArrayList<Robot> mapRobot=new ArrayList();

        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                Robot r=lireRobot(i,carte);

                mapRobot.add(r);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
        return mapRobot;
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private Robot lireRobot(int i,Carte carte) throws DataFormatException {
        ignorerCommentaires();
        Robot robot;

        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            String type = scanner.next();

            System.out.print("\t type = " + type);

            robot=this.createRobotInstance(type);
            Case position_robot=carte.getCase(lig,col);
            robot.setPosition(position_robot);



            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                int vitesse = Integer.parseInt(s);
                try {
                    robot.setVitesse(vitesse);
                }
                catch(Exception e){
                    System.out.println(e.toString());
                }

                System.out.print(vitesse);
            }
            verifieLigneTerminee();

            System.out.println();



        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
        return robot;
    }



    private Robot createRobotInstance(String type) throws  NoSuchElementException{

        switch (type){
            case "ROUES":
                return new RobotRoue();


            case "CHENILLES":
                return new RobotChenille();


            case "PATTES":
                return new RobotPatte();


            case "DRONE":
                return new Drone();


            default :
                throw new NoSuchElementException("Type de robot incorrect non pris en compte");


        }







    }


    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
