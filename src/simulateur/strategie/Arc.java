package simulateur.strategie;

public class Arc {


    private Noeud A,B;

    private double cout=Double.POSITIVE_INFINITY;

    public Arc(Noeud a, Noeud b, double cout) {
        A = a;
        B = b;
        this.cout = cout;
    }

    public Noeud getA() {
        return A;
    }

    public void setA(Noeud a) {
        A = a;
    }

    public Noeud getB() {
        return B;
    }

    public void setB(Noeud b) {
        B = b;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
}
