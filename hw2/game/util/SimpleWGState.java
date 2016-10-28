package hw2.game.util;

import hw2.game.Action;
import hw2.game.WGState;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Implementazione dell'interfaccia {@link hw2.game.WGState}. */
public class SimpleWGState implements WGState {
    /** Crea un WGState con le caratteristiche specificate.
     * @param a  azione
     * @param s  stringa di stato */
    private Action act = null;
    private String str = null;

     public SimpleWGState(Action a, String s) {
         this.act = a;
         this.str = s;
    }

    /** @return l'azione specificata nel costruttore */
    @Override
    public Action action() {
        return this.act;
    }

    /** @return La stringa di stato specificata nel costruttore */
    @Override
    public String state() {
        return this.str;
    }
}

