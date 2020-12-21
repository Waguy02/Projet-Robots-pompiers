package simulateur;

/**
 * L'ensemble des simulateurs. Constantes: Nom de cartes et chemin d'images pour les éléments graphiques
 */

public class    Constantes {


    public static double DEFAULT_TAILLE_CASE=0.1;//La taille des cases en km
    public static final double DUREE_ETAPE=1;//La durée d'une etape en secondes
     public static int RESOLUTION=700;//La résolution graphique



    public static  final String CARTE_SUJET="cartes/carteSujet.map";
    public static  final String CARTE_DESERT="cartes/desertOfDeath-20x20.map";
    public static  final String CARTE_MUSHROOM="cartes/mushroomOfHell-20x20.map";
    public static  final String CARTE_SPIRAL="cartes/spiralOfMadness-50x50.map";

    // Une carte personnalisée par nous  de la carte spiral avec un plus grand nombre de robots (25 robots) : passage à l'échelle
    public static  final String CARTE_CUSTOM="cartes/carte_custom.map";

    public static final String IMAGE_HABITAT = "img/habitat.png";
    public static final String IMAGE_TERRAIN= "img/terrain.jpg";
    public static final String IMAGE_FORET = "img/foret.jpg";
    public static final String IMAGE_ROCHE = "img/roche.jpg";
    public static final String IMAGE_CHENILLES = "img/chenilles.png";
    public static final String IMAGE_DRONE = "img/drone.png";
    public static final String IMAGE_PATTES = "img/pattes.png";
    public static final String IMAGE_ROUES = "img/roues.png";
    public static final String IMAGE_EAU = "img/eau.jpg";
    public static final String IMAGE_INCENDIE = "img/feu.png";


}
