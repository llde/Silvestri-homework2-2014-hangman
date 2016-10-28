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
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**  <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Un oggetto EvilHangman realizza un gioco che è una variante dell'Impiccato che
 * chiamiamo L'Impiccato Diabolico. A differenza dell'Impiccato tradizionale la
 * variante Diabolica non sceglie una singola parola segreta ma all'inizio fissa
 * solamente la lunghezza del  la parola. Il gioco per il giocatore procede
 * (apparentemente) come se giocasse l'Impiccato tradizionale. Ma dietro le quinte
 * il computer fa il possibile per rendere molto più difficile indovinare la parola
 * mantenendo però la coerenza con le risposte date. Per fare ciò, all'inizio il
 * computer invece di scegliere una singola parola parte con tutte le parole della
 * lunghezza fissata e questo lo chiamiamo l'insieme PW delle parole possibili.
 * Ogni volta che il giocatore chiede se un certo carattere x è presente, il computer
 * partiziona PW nei sottoinsiemi rispetto alle posizioni del carattere x. Ad esempio
 * se PW = ["pizza","torre","porta","pazza","perle","torte"] e x = 'a', allora la
 * partizione e' nei tre sottoinsiemi:
 * [["perle","torte","torre"], ["pizza","porta"], ["pazza"]]. Ogni sottoinsieme
 * consiste di esattamente quelle parole in PW che hanno il carattere x nelle stesse
 * posizioni o non è presente in nessuna di esse. A questo punto il computer
 * continua il gioco impostando PW uguale al sottoinsieme di cardinalità maggiore e a
 * parità di cardinalità quello con il minor numero di occorrenze del carattere x.
 * Nell'esempio continuerebbe quindi con PW = ["perle","torte","torre"]. In questo
 * modo il computer cerca di rimanere con il sottoinsieme più grande che è
 * compatibile con le informazioni che ha dato finora sulla parola (o le parole).
 * Il giocatore riesce a vincere solamente se quando tenta di indovinare la parola
 * l'insieme PW contiene una sola parola. Infatti anche se la parola tentata dal
 * giocatore è contenuta in PW ma PW ha almeno due parole, il computer potrà
 * asserire che la parola segreta era un'altra in PW. Per facilitare il testing
 * il computer deve dichiarare la parola che viene prima nell'ordimanete naturale
 * tra quelle in PW eccettuata la parola tentata dal giocatore. Ecco un esempio di
 * sessione di gioco come potrebbe essere prodotta dal gestore della UI testuale
 * {@link hw2.game.ui.TextUIWGames} fornendogli un oggetto EvilHangman:
 <pre>
 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (5)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 7
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
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
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ o _ _ i _ o
 Mancanti: a, e
 Indovina:
 t
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t
 Indovina:
 r
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r
 Indovina:
 s
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r, s
 Indovina:
 b
 Parola: _ o _ _ i _ o
 Mancanti: a, e, t, r, s, b (Hai esaurito il numero di errori)
 Indovina:
 n
 Parola: _ o _ _ i n o
 Mancanti: a, e, t, r, s, b (Hai esaurito il numero di errori)
 Indovina:
 c
 Non hai indovinato, la parola era: mozzino

 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 1
 Massimo numero di errori: 5
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (5)
 2. Lunghezza parola (7)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 9
 Errore, input ammessi: [1, 2, 3]
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 9
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (5)
 2. Lunghezza parola (9)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ _ _ _ _ _ i _ o
 Mancanti: a, e
 Indovina:
 u
 Parola: _ _ u _ _ _ i _ o
 Mancanti: a, e
 Indovina:
 s
 Parola: _ _ u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 r
 Parola: _ r u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 t
 Parola: t r u _ _ _ i _ o
 Mancanti: a, e, s
 Indovina:
 n
 Parola: t r u _ _ _ i n o
 Mancanti: a, e, s
 Indovina:
 c
 Parola: t r u c c _ i n o
 Mancanti: a, e, s
 Indovina:
 h
 HAI INDOVINATO: trucchino

 1. L'IMPICCATO DIABOLICO
 2. Quit
 Digita un intero da 1 a 2: 2
 Fine
 </pre>
 */
public class EvilHangman implements WGame {
    private List<Param<?>> settings = new ArrayList<>();
    private List<String> Parole = new ArrayList<>();
    private String nome = "";
    private String doc = "";
    private Set<String> insiemecorrente = new HashSet<>();
    private String parolatemp = "";
    private int erroriposs;
    private String miss = "";
    private String theEnd = "";
    private int erroricommessi = 0;
    private List<String> erroricont = new ArrayList<>();
    private   Set<String>  paroledilugnhezzafissa = null;
    private static WGState giocoinesecuzione = null;   //no game in execution when object is created.
    /** Crea un {@link hw2.game.WGame} per il gioco dell'Impiccato Diabolico. Si
     * assume che il file di percorso p contenga una parola per linea. Le parole che
     * saranno usate per giocare sono quelle nel file di lunghezza almeno minWL.
     * Il gioco ha due paramteri il massimo numero di errori consentiti con valori
     * [1,2,3,4,5,6,7] e default 6 e la lunghezza della parola con valori possibili
     * le lunghezze delle parole nel file (di lunghezza almeno minWL) e valore di
     * default la lunghezza minima.
     * @param p  percorso di un file di parole
     * @param cs  codifica dei caratteri del file di parole
     * @param minWL  lunghezza minima delle parole
     * @throws java.io.IOException se si verifica un errore leggendo il file di parole */
    public EvilHangman(Path p, Charset cs, int minWL) throws IOException {
        this.Parole = Utils.lines(p, cs, (s) -> s.length() < minWL);
        this.nome = "L'IMPICCATO DIABOLICO";
        this.doc = "Trova la parola indovinando una lettera alla volta\nSe credi di aver indovinato immetti l'intera parola";
        giocoinesecuzione = null;
        Integer[] valoriposs = {1, 2, 3, 4, 5, 6, 7};
        Integer[] lunghposs = Utils.lengths(this.Parole);
        int  index1 = Arrays.asList(lunghposs).indexOf(minWL);
        int index = Arrays.asList(valoriposs).indexOf(6);
        Param<Integer> numeroerrori = new SimpleParam<Integer>("Massimo numero di errori", index, valoriposs);
        Param<Integer> lunghezzaparole = new SimpleParam<Integer>("Lunghezza parola", index1, lunghposs);
        this.settings.add(numeroerrori);
        this.settings.add(lunghezzaparole);
        Collections.unmodifiableList(this.settings);
    }

    /** @return il nome del gioco, cioè "L'IMPICCATO DIABOLICO" */
    @Override
    public String name() {
       return this.nome;
    }

    /** @return le informazioni sul gioco, cioè
    <pre>
    Trova la parola indovinando una lettera alla volta
    Se credi di aver indovinato immetti l'intera parola
    </pre> */
    @Override
    public String info() {
        return this.doc;
    }

    /** Ritorna la lista dei parametri che per EvilHangman sono due parametri
     * con le seguenti caratteristiche
     <pre>
     prompt: "Massimo numero di errori"
     values: [1,2,3,4,5,6,7]
     valore di default: 6

     prompt: "Lunghezza parola"
     values: le lunghezze delle parole specificate nel costruttore
     valore di dafault: la minima lunghezza
     </pre>
     * @return la lista dei parametri */
    @Override
    public List<Param<?>> params() {
        return this.settings;
    }

    /** Inizia un nuovo gioco con una lunghezza specificata dal valore del
     * corrispondente parametro. La stringa dello stato iniziale deve consistere
     * nel nome del gioco, la stringa di info e poi
     * <pre>
     Parola: _ _ _ _ _ _ _
     Mancanti:
     Indovina:
     * </pre>
     * Dove il numero di underscore '_' è uguale alla lunghezza della parola da
     * indovinare. Ovviamente l'azione del WGState deve essere
     * {@link hw2.game.Action#CONTINUE}. Il numero di errori è inizializzato con il
     * valore del corrispondente parametro.
     * @return lo stato iniziale di un nuovo gioco di EvilHangman.
     * @throws java.lang.IllegalStateException se c'è già un gioco attivo */
    @Override
    public WGState newGame() {
        if(giocoinesecuzione != null) throw new IllegalStateException("Gioco già in esecuzione");
        Set<String> Dagon = Utils.stringsOfLength(this.Parole, ((Integer) this.params().get(1).get()));
     //   theEnd = Utils.choose(Dagon);
        this.miss = "";
        this.erroricommessi = 0;
        this.erroriposs = (Integer)  this.params().get(0).get();
        this.erroricont = new ArrayList<>();
        this.parolatemp = "";
        for (int i = 0; i <= (Integer) this.params().get(1).get() -1; i++) {
            parolatemp += " _";
        }
        this.insiemecorrente = Dagon;
        parolatemp = parolatemp.trim();
        String statoinit = this.nome + "\n" + this.doc + "\n" + "Parola: " + parolatemp + "\nMancanti:" + miss + "\nIndovina:" +"\n";
        WGState stato = new SimpleWGState(Action.CONTINUE, statoinit);
        giocoinesecuzione = stato;
        return stato;
    }

    /** Accetta la stringa s del giocatore. Prima di tutto riduce s in minuscolo.
     * Se la stringa è vuota o contiene caratteri non alfabetici, la ignora e
     * ritorna lo stesso stato precedente. Se consiste di un solo carattere, esegue
     * la procedura spiegata nel javdoc della classe per aggiornare PW. Se è il
     * carattere è presente nelle parole di PW aggiornato, evidenzia le posizioni in
     * cui appare sostituendo gli underscore (come nell'esempio mostrato sopra). Se
     * non è presente e non era già stato digitato, lo aggiunge ai caratteri mancanti
     * e decrementa gli errori consentiti. Se gli errori consentiti sono diventati
     * zero, lo comunica nella stringa di stato (come nell'esempio mostrato sopra).
     * Se gli errori consentiti erano zero già prima di commettere l'ultimo errore il
     * gioco termina comunicando l'azione {@link hw2.game.Action#END}. Se consiste di
     * 2 o più caratteri, controlla se la parola è in PW se è l'unica parola in PW il
     * giovcatore ha indovinato, altrimenti o non è in PW o PW ha almeno due parole,
     * il giocatore ha perso e il computer comunica che la parola era quella in PW
     * eccettuata quela tentata dal giocatore che viene prima nell'ordinamento
     * naturale delle stringhe. In entrambi i casi comunica l'esito del gioco nella
     * stringa di stato (come nell'esempio mostrato sopra), l'azione è
     * {@link hw2.game.Action#END} e il gioco è terminato. In tutti gli altri casi
     * l'azione è {@link hw2.game.Action#CONTINUE}.
     * @param s  la mossa del giocatore
     * @return il nuovo stato di gioco
     * @throws java.lang.IllegalStateException se non c'è un gioco attivo */
    @Override
    public WGState player(String s) { //Implementazione per il diabolico
        if (giocoinesecuzione == null) throw new IllegalStateException("Gioco non in esecuzione");
        String  t = s.toLowerCase().trim();
        int[] ispresent = new int[0];
        String vecchiostato = "";
        String nuovostato = "";
        if (this.erroricommessi >= (Integer) this.params().get(0).get()) {
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() -1) + " (Hai esaurito il numero di errori)" + "\nIndovina:" + "\n";
        } else if (miss.length() > 1) {
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() -1) + "\nIndovina:" + "\n";
        } else {
            vecchiostato = "Parola: " + parolatemp + "\nMancanti:" + miss + "\nIndovina:" + "\n";
        }
        if (!Utils.isAlphaLowercase(t) || t.length() == 0) {
            return new SimpleWGState(Action.CONTINUE, vecchiostato);
        }
        if(t.length() > 1) {
            if (!this.insiemecorrente.contains(t)) {
                this.theEnd = this.insiemecorrente.stream().sorted().collect(Collectors.toList()).get(0);
                String nonbuono = "Non hai indovinato, la parola era: " + theEnd + "\n";
                giocoinesecuzione = null;
                return new SimpleWGState(Action.END, nonbuono);
            } else if (this.insiemecorrente.contains(t)) {
                if (this.insiemecorrente.size() > 1) {
                    this.theEnd = this.insiemecorrente.stream().filter((elim) -> !elim.equals(t)).collect(Collectors.toList()).get(0);
                    String nonbuono = "Non hai indovinato, la parola era: " + theEnd + "\n";
                    giocoinesecuzione = null;
                    return new SimpleWGState(Action.END, nonbuono);
                } else {
                    this.theEnd = t;
                    String termineg = "HAI INDOVINATO: " + theEnd + "\n\n";
                    giocoinesecuzione = null;
                    return new SimpleWGState(Action.END, termineg);

                }
            }
        }
        if(t.length() == 1){
            BiPredicate<String,String>  pred2 = (s1,s2) -> { //Ottimizzare ulteriormente
                if(s1.indexOf(t) != s2.indexOf(t)) return false;
                if((!s1.contains(t))&&(!s2.contains(t))) return true;
                String t1 = s1.replace(t, "");
                String t2 = s2.replace(t, "");
                if(t1.length() != t2.length()) return false;
                int i = s1.indexOf(t);
                while(i < s1.length()) {
                    if(s1.indexOf(t , i +1) != s2.indexOf(t , i +1)) return false;
                    if(s1.indexOf(t, i+1) < 0 && s2.indexOf(t, i + 1) < 0) return true;
                    i = s1.indexOf(t, i+1);
                }
                return false;
            };
            List<Set<String>> temp = Utils.partition(this.insiemecorrente, pred2);
            List<Set<String>> Figux = temp.stream().sorted(Comparator.comparingInt(Set<String>::size).reversed()).collect(Collectors.toList());
            int lungh = Figux.get(0).size();
            Figux = Figux.stream().filter((m) -> m.size() == lungh).collect(Collectors.toList());
            int index = 0;
            int card = -2;
            if(Figux.size() == 1) {
                this.insiemecorrente = Figux.get(0);
            }
            else{
                for(Set<String>  el : Figux){
                    for(String str : el){
                        if(card == -2) {
                            card = str.indexOf(t);
                        }
                        else{
                            if(card > str.indexOf(t)){
                                card = str.indexOf(t);
                                index++;
                            }
                        }
                        break;
                    }
                }
            }
            this.insiemecorrente = Figux.get(index);
            this.insiemecorrente = this.insiemecorrente.stream().sorted().collect(Collectors.toSet());
            String x =  this.insiemecorrente.stream().findFirst().get();
            int indicemal = 0;
            while(true){
                indicemal = x.indexOf(t, indicemal);
                if(indicemal < 0) break;
                ispresent = Arrays.copyOf(ispresent, ispresent.length + 1);
                ispresent[ispresent.length - 1] = indicemal;
                indicemal++;
            }
            if(ispresent.length == 0){
                if(!this.erroricont.contains(t)){
                    this.miss += " " + t +",";
                    this.erroricont.add(t);
                    this.erroricommessi++;
                }
            }
            if(ispresent.length > 0){
                char[] temparr = this.parolatemp.toCharArray();
                for(int el: ispresent){
                    temparr[(el)*2]  = t.charAt(0);
                }
                this.parolatemp = String.valueOf(temparr);
            }
            if(erroricommessi > erroriposs){
                this.theEnd = this.insiemecorrente.stream().sorted().collect(Collectors.toList()).get(0);
                String nonbuono = "Non hai indovinato, la parola era: " + theEnd + "\n";
                giocoinesecuzione = null;
                return new SimpleWGState(Action.END, nonbuono);
            }
            if(erroricommessi == erroriposs){
                nuovostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() -1) + " (Hai esaurito il numero di errori)" + "\nIndovina:" + "\n";
            }
            else {
                nuovostato = "Parola: " + parolatemp + "\nMancanti:" + miss.substring(0, miss.length() -1) + "\nIndovina:" + "\n";
            }
            return new SimpleWGState(Action.CONTINUE, nuovostato);
        }

        return new SimpleWGState(Action.CONTINUE, vecchiostato);
    }
       /** Termina prematuramente l'eventuale gioco attivo */
    @Override
    public void abort() {
        giocoinesecuzione = null;
    }
}

