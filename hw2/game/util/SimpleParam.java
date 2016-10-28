package hw2.game.util;

import hw2.game.Param;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Implementazione dell'interfaccia {@link hw2.game.Param}.
 * @param <T>  il tipo dei valori del parametro */
public class SimpleParam<T> implements Param<T> {
    /** Crea un Param con le caratteristiche specificate.
     * @param p  stringa di prompt
     * @param i  indice del valore iniziale relativo all'array vals
     * @param vals  array dei possibili valori del parametro */
    private String prompt = "";
    private T valore;
    private T[] possval;
    private  int indval = -1;
    private  List<T> listavalori;

    public SimpleParam(String p, int i, T...vals) {
        this.prompt = p;
        this.possval = vals;
        this.listavalori = Collections.unmodifiableList(Arrays.asList(vals));
        this.valore = possval[i];
        this.indval = i;
    }

    /** @return la stringa di prompt specificata nel costruttore */
    @Override
    public String prompt() {
        return this.prompt;
    }

    /** @return la lista dei possibili valori specificati nel costruttore */
    @Override
    public List<T> values() {
        return this.listavalori;
    }

    /** Imposta il valore del parametro tramite l'indice nella lista dei valori
     * ritornata dal metodo {@link hw2.game.util.SimpleParam#values()}.
     * @param i  indice del valore nella lista dei valori
     * @throws IllegalStateException se l'indice è fuori range */
    @Override
    public void set(int i) {
        try {
            this.valore = this.possval[i];
            this.indval = i;
        }
        catch (IndexOutOfBoundsException e){
            throw new IllegalStateException("Indice " + i);
        }
    }

    /** @return il valore del parametro */
    @Override
    public T get() {
        return this.valore;
    }
}

