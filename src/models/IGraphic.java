package models;

import models.incendie_environnment.Case;

/***
 * Interface des elements représentatbles sur l'interface
 */
public interface IGraphic {

    /**
     *
     * @return Le fichier à partir duquel est chargée l'image
     */
    public String getFileName();

    /***
     *
     *
     * @return La position de l'élement : une Case
     */
    public Case getPosition();
}
