    package simulateur.evenements;

    import models.enums.Direction;
    import models.incendie_environnment.Carte;
    import models.incendie_environnment.Case;
    import models.incendie_environnment.DonneeSimulation;
    import models.incendie_environnment.Incendie;
    import models.robots.Robot;
    import simulateur.Constantes;

    import java.util.ArrayList;
    import java.util.List;

    public class EvenementDeplacement extends Evenement {



        public EvenementDeplacement(Robot robot,Direction direction) {

            super(robot);
            this.direction = direction;


        }

        @Override
        public void execute() {
            DonneeSimulation ds=this.robot.getDonneeSimulation();
            Carte carte=ds.getCarte();
            Case activePosition=this.robot.getPosition();


            if(this.getIncendie_cible()!=null){
                Incendie incendie=this.getIncendie_cible();

                if(incendie.estEteint()){
                    Evenement current=this;

                    while(current!=null){
                        this.simulateur.getListeEvenements().remove(current);
                        current=current.getNext();
                    }



                }
            }



            try {
                if (carte.voisinExiste(activePosition, this.direction)) {
                    Case voisin=carte.getVoisin(activePosition,this.direction);
                    if(!robot.peutAtteindre(voisin))throw  new Exception("Erreur, Le robot ne peut pas atteindre ce type de terrain");


                    robot.setPosition(voisin);
                    // System.out.println("Depalcement de  "+activePosition.toString()+" vers "+robot.getPosition().toString());

                    /**
                     * On calcule la date du déplacement suivant s'il existe
                     */



                }else{
                    throw  new Exception("Erreur Le robot est sorti de la carte");

                }



            }
            catch(Exception e){

                System.out.println(e.getMessage());
            }

        // On vérifie si l'évènement suivi est un de type intervention et on fait les opérations nécessaires

        }


        private Direction direction;





        /**
         * Permet de calculer la date de fin d'un déplacement unitaire à partir d'une date courante t0
         * @param t0
         */

        @Override
        public void calculateDate(long t0){


            double vitesse=this.robot.getVitesse(this.robot.getPosition().getNature());
            if(vitesse==0){
                /**
                 * Surface inatteignable :
                 */
                date=this.date;
            }

            /**
             * Le pas de temps en secondes
             */


            double distance_deplacement=robot.getPosition().getTaille();

            double duree_deplacement=(distance_deplacement/vitesse)*3600;

            Long nombre_etapes=(long) Math.ceil(duree_deplacement/Constantes.DUREE_ETAPE);



            long t_event=t0+nombre_etapes;

            this.date=t_event;

        }


        /**
         *
         *
         * @param robot
         * @param directions
         * @param t0
         * @return Une séquence de déplacements d'un robot dans une suite de direction donnée dans directions.
         * Par exemple directions=[NORD,SUD,OUEST]correspond à un deplacement vers le nord suvi d'un déplacement vers le sud suivi d'un déplacmeent vers l'ouest".
         * Si l'un des déplacements n'est pas réalisable la séquence n'est pas créée
         * @throws Exception
         */
        public static List<EvenementDeplacement> genererSequenceDeplacement (Robot robot,List<Direction> directions,long t0){


            List<EvenementDeplacement> list=new ArrayList<>();
            EvenementDeplacement current=null;
            for (Direction direction : directions) {

                EvenementDeplacement event = new EvenementDeplacement(robot, direction);
                if(current!=null)current.setNext(event);
                current=event;
                list.add(event);


            }

            /**
             * On calcule la date du premier déplacement
             */

            if(t0>=0)list.get(0).calculateDate(t0);

            return list;


        }

        public String toString(){
            Carte carte=robot.getDonneeSimulation().getCarte();

            return  "Evenement Deplamceent de "+this.getRobot()+" Date :" +(this.date==Long.MAX_VALUE?"Non déterminée":this.date)
                    +" ; DIRECTION : "+direction.toString();
        }



    }
