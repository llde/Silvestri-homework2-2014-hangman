package hw2.games;


import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGState;
import hw2.game.WGame;
import hw2.game.util.SimpleParam;
import hw2.game.util.SimpleWGState;
import hw2.game.util.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Un oggetto Hangman realizza un gioco conosciuto come L'Impiccato (in inglese
 * Hangman). Il computer sceglie una parola segreta che il giocatore deve
 * indovinare e mostra al giocatore solamente la sua lunghezza. Il giocatore
 * può chiedere al computer se la parola contiene una lettera. Se è presente nella
 * parola, il computer mostra le posizioni in cui appare, se invece non è presente,
 * il giocatore ha commesso un errore. Il gioco termina o quando il giocatore
 * indovina la parola o quando commette un errore in più rispetto a quelli
 * ammessi. Ad esempio ecco una sessione di gioco come potrebbe essere prodotta
 * dal gestore della UI testuale {@link TextUIWGames} fornendogli un
 * oggetto Hangman:
 <pre>
 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ e
 Mancanti: a
 Indovina:
 i
 Parola: _ _ _ _ i _ e
 Mancanti: a
 Indovina:
 o
 Parola: _ o _ _ i _ e
 Mancanti: a
 Indovina:
 r
 Parola: _ o r _ i r e
 Mancanti: a
 Indovina:
 n
 Parola: _ o r n i r e
 Mancanti: a
 Indovina:
 t
 Parola: _ o r n i r e
 Mancanti: a, t
 Indovina:
 f
 HAI INDOVINATO: fornire

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 1
 Massimo numero di errori: 2
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (2)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ e _ _ e _ e
 Mancanti: a
 Indovina:
 r
 Parola: _ e _ _ e _ e
 Mancanti: a, r (Hai esaurito il numero di errori)
 Indovina:
 t
 Non hai indovinato, la parola era: sebbene

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (2)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 i
 Parola: _ _ _ _ i _ i
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 o
 Parola: _ o _ _ i _ i
 Mancanti: a, e (Hai esaurito il numero di errori)
 Indovina:
 fossili
 Non hai indovinato, la parola era: bottini

 1. L'IMPICCATO
 2. Quit
 Digita un intero da 1 a 2: 2
 Fine
 </pre>
 */
public class Hangman implements WGame {
    /**
     * Crea un {@link hw2.game.WGame} per il gioco dell'Impiccato. Si assume che
     * il file di percorso p contenga una parola per linea. Le parole che saranno
     * usate per giocare sono quelle nel file di lunghezza almeno minWL.
     * Il gioco ha un solo paramtero che è il massimo numero di errori consentiti.
     * I valori del parametro sono [1,2,3,4,5,6,7], quello di default è 6.
     *
     * @param p  percorso di un file di parole
     * @param cs  codifica dei caratteri del file di parole
     * @param minWL  lunghezza minima delle parole
     * @throws IOException se si verifica un errore leggendo il file di parole
     */
    private List<Param<?>> settings = new ArrayList<>();
    private List<String> Parole = new ArrayList<>();
    private String nome = "";
    private String doc = "";
    private String paroladaindovinare = "";
    private String parolatemp = "";
    private String miss = "";
    private String theEnd = "";
    private int erroricommessi = 0;
    private List<Character> erroricont = new ArrayList<>();
    private static WGState giocoinesecuzione = null;   //no game in execution when object is created.

    public Hangman(Path p, Charset cs, int minWL) throws IOException { //Rewrite everythingh with more reusability in mind.
        this.Parole = Utils.lines(p, cs, (s) -> s.length() < minWL);
        this.nome = "L'IMPICCATO";
        giocoinesecuzione = null;
        this.doc = "Trova la parola indovinando una lettera alla volta\nSe credi di aver indovinato immetti l'intera parola";
        Integer[] valoriposs = {1, 2, 3, 4, 5, 6, 7};
        int index = Arrays.asList(valoriposs).indexOf(6);
        Param<Integer> numeroerrori = new SimpleParam<Integer>("Massimo numero di errori", index, valoriposs);
        this.settings.add(numeroerrori);
        Collections.unmodifiableList(this.settings);
    }


    /**
     * @return il nome del gioco, cioè "L'IMPICCATO"
     */
    @Override
    public String name() {
        return this.nome;
    }

    /**
     * @return le informazioni sul gioco, cioè
     * <pre>
     * Trova la parola indovinando una lettera alla volta
     * Se credi di aver indovinato immetti l'intera parola
     * </pre>
     */
    @Override
    public String info() {
        return this.doc;
    }

    /**
     * Ritorna la lista dei parametri che per Hangman ha un solo parametro con le
     * seguenti caratteristiche:
     * <pre>
     * prompt: "Massimo numero di errori"
     * values: [1,2,3,4,5,6,7]
     * valore di default: 6
     * </pre>
     *
     * @return la lista dei parametri (con un solo parametro)
     */
    @Override
    public List<Param<?>> params() {
        return this.settings;
    }

    /**
     * Inizia un nuovo gioco scegliendo una parola random dalla lista di parole
     * specificate nel costruttore.
     * ATTENZIONE La scelta della parola deve essere effettuata tramite il metodo
     * {@link hw2.game.util.Utils#choose(java.util.Collection)}.
     * La stringa dello stato iniziale deve consistere nel nome del gioco, la
     * stringa di info e poi
     * <pre>
     * Parola: _ _ _ _ _ _ _
     * Mancanti:
     * Indovina:
     * </pre>
     * Dove il numero di underscore '_' è uguale alla lunghezza della parola da
     * indovinare. Ovviamente l'azione del WGState deve essere
     * {@link hw2.game.Action#CONTINUE}. Il numero di errori consentito è
     * inizializzato al valore del corrispondente parametro.
     *
     * @return lo stato iniziale di un nuovo gioco di Hangman.
     * @throws java.lang.IllegalStateException se c'è già un gioco attivo
     */
    @Override
    public WGState newGame() {  //Gestire eccezione e istanza
        if(giocoinesecuzione != null) throw new IllegalStateException("Gioco già in esecuzione");
        theEnd = Utils.choose(this.Parole);
        this.paroladaindovinare = "";
        this.miss = "";
        this.erroricommessi = 0;
        this.erroricont = new ArrayList<>();   //Bug solved
        this.parolatemp = "";
        for (char ch : theEnd.toCharArray()) {
            this.paroladaindovinare += ch + " ";
        }
        paroladaindovinare = paroladaindovinare.trim();
        for (char ch : this.paroladaindovinare.toCharArray()) {
            if (ch == ' ') parolatemp += " ";
            else parolatemp += "_";
        }
        parolatemp = parolatemp.trim();
        String statoinit = this.name() + "\n" + this.doc + "\n" + "Parola: " + parolatemp + "\nMancanti:" + miss + "\nIndovina:" +"\n";
        WGState stato = new SimpleWGState(Action.CONTINUE, statoinit);
        giocoinesecuzione = stato;
        return stato;
    }

    /**
     * Accetta la stringa s del giocatore. Prima di tutto riduce s in minuscolo.
     * Se la stringa è vuota o contiene caratteri non alfabetici, la ignora e
     * ritorna lo stesso stato precedente. Se consiste di un solo carattere,
     * controlla se è presente nella parola segreta. Se è presente evidenzia le
     * posizioni in cui appare sostituendo gli underscore (come nell'esempio
     * mostrato sopra). Se non è presente e non era già stato digitato, lo aggiunge
     * ai caratteri mancanti e decrementa gli errori consentiti. Se gli errori
     * consentiti sono diventati zero, lo comunica nella stringa di stato (come
     * nell'esempio mostrato sopra). Se gli errori consentiti erano zero già prima di
     * commettere l'ultimo errore il gioco termina comunicando l'azione
     * {@link hw2.game.Action#END}. Se consiste di 2 o più caratteri, confronta la
     * stringa fornita con la parola segreta e comunica l'esito del gioco nella
     * stringa di stato (come nell'esempio mostrato sopra), l'azione è
     * {@link hw2.game.Action#END} e il gioco è terminato. In tutti gli altri casi
     * l'azione è {@link hw2.game.Action#CONTINUE}.
     *
     * @param s la mossa del giocatore
     * @return il nuovo stato di gioco
     * @throws java.lang.IllegalStateException se non c'è un gioco attivo
     */
    @Override
    public WGState player(String s) {   //To BE completed
        if(giocoinesecuzione == null) throw new IllegalStateException("Gioco non in esec");
        s = s.toLowerCase().trim();
        int[] ispresent = new int[0];
        String vecchiostato = "";
        String nuovostato = "";
        if (this.erroricommessi >= (Integer) this.params().get(0).get()) {
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() - 1) + " (Hai esaurito il numero di errori)" + "\nIndovina:" + "\n";
        }
        else if(miss.length() > 1) {
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() - 1) + "\nIndovina:" + "\n";
        }
        else{
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss + "\nIndovina:" + "\n";
        }
        if (!Utils.isAlphaLowercase(s) || s.length() == 0) {
            return new SimpleWGState(Action.CONTINUE, vecchiostato);
        }
        if (s.length() > 1) {
            if (s.equalsIgnoreCase(theEnd)) {
                parolatemp = theEnd;
                String termineg = "HAI INDOVINATO: " + theEnd +"\n\n";
                giocoinesecuzione = null;
                return new SimpleWGState(Action.END, termineg);
            }
            else{
                String nonbuono = "Non hai indovinato, la parola era: " + theEnd + "\n";
                giocoinesecuzione = null;
                return new SimpleWGState(Action.END, nonbuono);
            }
        }
        else {
            for (int i = 0; i < paroladaindovinare.length(); i++) {
                char el = paroladaindovinare.charAt(i);
                if (el == s.charAt(0)) {
                    ispresent = Arrays.copyOf(ispresent, ispresent.length + 1);
                    ispresent[ispresent.length - 1] = i;
                }
            }
        }
        if (ispresent.length > 0) {
            for (int i : ispresent) {
                char[] temparr = parolatemp.toCharArray();
                temparr[i] = paroladaindovinare.charAt(i);
                parolatemp = String.valueOf(temparr);
                if(parolatemp.equalsIgnoreCase(paroladaindovinare)){
                    String termineg = "HAI INDOVINATO: " + theEnd +"\n";
                    giocoinesecuzione = null;
                    return new SimpleWGState(Action.END, termineg);
                }
            }
        }
        else{
            if(!erroricont.contains(s.charAt(0))) {
                miss += " " + s + ",";
                this.erroricommessi++;
                this.erroricont.add(s.charAt(0));
                if (this.erroricommessi > (Integer) this.params().get(0).get()) {
                    String nonbuono = "Non hai indovinato, la parola era: " + theEnd + "\n";
                    giocoinesecuzione = null;
                    return new SimpleWGState(Action.END, nonbuono);
                }
            }
            else{
                return new SimpleWGState(Action.CONTINUE, vecchiostato);
            }
        }
        if (this.erroricommessi >= (Integer) this.params().get(0).get()) {
            nuovostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() - 1) + " (Hai esaurito il numero di errori)" + "\nIndovina:" + "\n";
        }
        else if(miss.length() > 1) {
            nuovostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() - 1) + "\nIndovina:" + "\n";
        }
        else {
            nuovostato = "Parola: " + parolatemp + "\nMancanti:" + miss + "\nIndovina:" + "\n";
        }
        return new SimpleWGState(Action.CONTINUE, nuovostato);
    }

    /** Termina prematuramente l'eventuale gioco attivo */
    @Override
    public void abort() {
        giocoinesecuzione = null;
    }
}

