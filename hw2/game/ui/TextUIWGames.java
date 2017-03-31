package hw2.game.ui;

import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGState;
import hw2.game.WGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Gestore per la UI testuale (Interfaccia Utente Testuale) per giochi di tipo
 * {@link hw2.game.WGame}.
 * C'è un unico metodo statico start che fa partire l'esecuzione della UI. Questa
 * presenta all'utente (giocatore) un menu con i possibili giochi e la possibilità di
 * uscire. Quando il giocatore sceglie uno dei giochi digitando il numero della
 * corrispondente voce di menu (con il nome del gioco), se il gioco ha dei parametri
 * viene data la possibilità di modificarne i valori. Altrimenti viene iniziata una
 * nuova partita del gioco scelto. Ecco un esempio di sessione di gioco con due
 * giochi Hangman e EvilHangman:
 <pre>
 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 1
 Massimo numero di errori: 3
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (3)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: a _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 e
 Parola: a _ _ e _ e _ _ _
 Mancanti:
 Indovina:
 o
 Parola: a _ _ e _ e _ _ _
 Mancanti: o
 Indovina:
 i
 Parola: a _ _ e _ e _ _ i
 Mancanti: o
 Indovina:
 r
 Parola: a _ _ e r e _ _ i
 Mancanti: o
 Indovina:
 t
 Parola: a _ _ e r e _ t i
 Mancanti: o
 Indovina:
 s
 Parola: a _ _ e r e s t i
 Mancanti: o
 Indovina:
 b
 Parola: a _ _ e r e s t i
 Mancanti: o, b
 Indovina:
 f
 Parola: a _ _ e r e s t i
 Mancanti: o, b, f (Hai esaurito il numero di errori)
 Indovina:
 v
 Non hai indovinato, la parola era: alzeresti

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 2
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (5)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 2
 Lunghezza parola: 10
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (6)
 2. Lunghezza parola (10)
 3. Inizia a giocare
 Digita un intero da 1 a 3: 3
 Per interrompere il gioco digita $stop
 L'IMPICCATO DIABOLICO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti: a
 Indovina:
 e
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti: a, e
 Indovina:
 i
 Parola: _ _ _ _ _ _ _ i _ _
 Mancanti: a, e
 Indovina:
 o
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e
 Indovina:
 r
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r
 Indovina:
 t
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t
 Indovina:
 l
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t, l
 Indovina:
 p
 Parola: _ o _ _ o _ _ i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 c
 Parola: _ o _ _ o c _ i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 h
 Parola: _ o _ _ o c h i _ o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 n
 Parola: _ o _ _ o c h i n o
 Mancanti: a, e, r, t, l, p (Hai esaurito il numero di errori)
 Indovina:
 m
 Non hai indovinato, la parola era: soffochino

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 1
 Modifica i parametri di gioco o inizia a giocare:
 1. Massimo numero di errori (3)
 2. Inizia a giocare
 Digita un intero da 1 a 2: 2
 Per interrompere il gioco digita $stop
 L'IMPICCATO
 Trova la parola indovinando una lettera alla volta
 Se credi di aver indovinato immetti l'intera parola
 Parola: _ _ _ _ _ _ _ _ _ _
 Mancanti:
 Indovina:
 a
 Parola: _ _ _ _ _ _ _ _ _ a
 Mancanti:
 Indovina:
 e
 Parola: _ _ _ _ _ _ e _ _ a
 Mancanti:
 Indovina:
 i
 Parola: _ i _ i _ _ e _ _ a
 Mancanti:
 Indovina:
 r
 Parola: _ i _ i _ _ e _ _ a
 Mancanti: r
 Indovina:
 t
 Parola: _ i _ i _ _ e t t a
 Mancanti: r
 Indovina:
 c
 Parola: _ i c i c _ e t t a
 Mancanti: r
 Indovina:
 bicicletta
 HAI INDOVINATO: bicicletta

 1. L'IMPICCATO
 2. L'IMPICCATO DIABOLICO
 3. Quit
 Digita un intero da 1 a 3: 3
 Fine
 </pre>
 */
public class TextUIWGames {
 /**
  * Inzia una sessione di gioco tramite interfaccia testuale che permette di
  * giocare con i giochi specificati in games. Quando una partita è in corso, il
  * giocatore può interromperla digitando $stop. In tal caso il gestore invocherà
  * il metodo {@link hw2.game.WGame#abort()} del gioco.
  * <p>
  * La UI deve gestire il menu per la scelta del gioco e quello (o quelli) per la
  * modifica dei valori degli eventuali parametri di gioco. Inoltre deve prendere
  * l'input digitato dal giocatore e comunicarlo al gioco e visualizzare lo stato
  * della partita ritornato dal gioco. Come nell'esempio mostrato sopra.
  * <p>
  * Si consiglia di leggere l'input sempre come intera linea.
  *
  * @param games i giochi disponibili
  */
    public static void start(WGame... games) {  //print o println questo è il dilemma
        WGState giocoex = null;
        String UI = "";
        int index = 0;
        int sel;
        Scanner TUI = new Scanner(System.in);
        for (WGame game : games) {
            index++;
            UI += index + ". " + game.name().trim() + "\n";
        }
        UI += ((index + 1) + ". Quit\n" + "Digita un intero da 1 a " + (index + 1)) + ":\n";
        String ReservedUI = UI;
        while (true) {
            System.out.println(UI);
        try {
            sel = Integer.valueOf(TUI.nextLine());
        }
        catch (Exception e){
            String Akatosh = "";
            for(int y = 1; y <= index + 1; y++){
                Akatosh += " " + y + ",";
            }
            String errore = "Errore, input ammessi: " + "["  + Akatosh.trim().substring(0, Akatosh.length() - 2) + "]";
            System.out.println(errore);
            UI = "Digita un intero da 1 a " + (index + 1) + ":\n";
            continue;
        }
        if (sel == games.length + 1){
            System.out.println("Fine");
            break;
        }
        if(sel > games.length +1){
            String Akatosh = "";
            for(int y = 1; y <= index + 1; y++){
                Akatosh += " " + y + ",";
            }
            String errore = "Errore, input ammessi: " + "["  + Akatosh.trim().substring(0, Akatosh.length() - 2) + "]";
            System.out.println(errore);
            UI = "Digita un intero da 1 a " + (index + 1) + ":\n";;
            continue;
        }
        List<Param<?>> settings = games[sel - 1].params();
        String Paramet = "";
        int indexa = 0;
        int nogenerate = 0;
        String ReservedParam = "";
        while (true) {
            if(nogenerate == 0) {
                Paramet += "Modifica i parametri di gioco o inizia a giocare:" + "\n";
                indexa = 0;
                if(!settings.isEmpty()) {
                    for (Param<?> par : settings) {
                        indexa++;
                        Paramet += indexa + ". " + par.prompt().trim() + " (" + par.get() + ")" + "\n";
                    }
                }
                Paramet += ((indexa + 1) + ". Inizia a giocare" + "\n" + "Digita un intero da 1 a " + (indexa + 1) + ":\n");
                ReservedParam = Paramet;
            }
            System.out.println(Paramet);
            Paramet = ReservedParam;
            int sela = 0;
            try {
                sela = Integer.valueOf(TUI.nextLine());
                if(sela > indexa + 1) throw  new  NumberFormatException();
            }
            catch(NumberFormatException e){
                String Akatosh = "";
                nogenerate = 1;
                for(int y = 1; y <= indexa + 1; y++){
                    Akatosh += " " + y + ",";
                }
                String errore = "Errore, input ammessi: " + "["  + Akatosh.trim().substring(0, Akatosh.length() - 2) + "]";
                System.out.println(errore);
                Paramet = "Digita un intero da 1 a " + (indexa +1) + ":\n";
                continue;
            }
            if (sela == settings.size() + 1) {
                System.out.println("Per interrompere il gioco digita $stop\n");
                giocoex = games[sel - 1].newGame();
                break;
            } else if (sela <= settings.size()) {
                if(sela <= 0) continue;
                System.out.println(settings.get(sela - 1).prompt() + ":");
                List<?> pare = settings.get(sela -1).values();
                List<String> templist= new ArrayList<>();
                for(int i = 0; i < pare.size(); i++){
                    templist.add(String.valueOf(pare.get(i)));
                }
                int setind = -2;
                while(true) {
                    String newval = TUI.nextLine();
                    setind = templist.indexOf(newval);
                    if (setind < 0) {
                        System.out.println("Errore, input ammessi: " + pare.toString());
                        continue;
                    }
                    else break;
                }
                settings.get(sela - 1).set(setind);
                indexa = 0;
                Paramet = "";
            }
        }
        while (giocoex != null) {
            System.out.println(giocoex.state());
            String var = TUI.nextLine();
            if(var.equalsIgnoreCase("$stop")) {
                games[sel - 1].abort();
                System.out.println("Gioco interrotto");
                UI = ReservedUI;
                giocoex = null;
                break;
            }
            giocoex = games[sel - 1].player(var);
            if (giocoex.action().equals(Action.END)) {
                System.out.println(giocoex.state());
                UI = ReservedUI;
                giocoex = null;
                break;
            }
        }
        }
    }
}