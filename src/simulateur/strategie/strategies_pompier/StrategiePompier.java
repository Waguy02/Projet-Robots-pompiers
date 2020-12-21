package simulateur.strategie.strategies_pompier;

import simulateur.strategie.ChefPompier;

/**
 * L'interface des StrategiePompier du chef pompier
 */
public interface StrategiePompier {

    // L'execution de la stratégie
    public void execute(ChefPompier chefPompier,long date_courante);


    // La réinitialisation de la stratégie (variables dépendant de l'état des simulations)
    public void reset();

}
