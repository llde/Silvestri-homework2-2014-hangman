package hw2.game.ui;


import hw2.game.Param;
import hw2.game.WGame;

import java.nio.charset.MalformedInputException;
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
public class TextUIGames {
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
        String PrimalUi = "";
        int index = 0;
        for(WGame game : games){
            PrimalUi += (index +1) + ". " + game.name().trim() + "\n";
            index +=1;
        }
        PrimalUi += (index + 1) + ". Quit\n";
        String insertUi = "Digita un intero da 1 a " + (index +1) + ":\n";
        Scanner in = new Scanner(System.in);
        System.out.print(PrimalUi);
        while(true){
            System.out.println(insertUi);
            try {
                Integer t = Integer.valueOf(in.nextLine());
                if (t > index + 1 || t == 0) {
                    throw new NumberFormatException();
                }
                if(t == index +1){
                    System.out.println("Fine");
                    break;
                }
                WGame game = games[t-1];
                String par =  "Modifica i parametri di gioco o inizia a giocare:" + "\n";
                int indexPar = 0;
                if(!game.params().isEmpty()){
                    for(Param<?> param : game.params())
                        par += (indexPar +1) + ". " + param.prompt() + "(" + param.get() + ")\n";
                    indexPar++;
                }
                par+= (indexPar +1) + ". Inizia a giocare\n";
                System.out.println(par);
                String p = "Digita un intero da 1 a " + (indexPar +1);
                while(true){
                    System.out.println(p);
                    try{
                        int param = Integer.valueOf(in.nextLine());
                        if(param > indexPar+1) throw new NumberFormatException();
//TODO param and game.
                    }catch (NumberFormatException e){
                        //TODO method
                        String error = "Errore, input ammessi: [$to_subs]";
                        String sub = "";
                        for(int i = 1; i <= indexPar +1;i++){
                            sub += " " + i + ",";
                        }
                        error = error.replace("$to_subs", sub.trim().substring(0, sub.length() -2));
                        System.out.println(error);
                        continue;
                    }

                }
            }
            catch(NumberFormatException e){
                String error = "Errore, input ammessi: [$to_subs]";
                String valid = "";
                for(int i=1; i<=index+1;i++){
                    valid += " " + i + ",";
                }
                error = error.replace("$to_subs", valid.trim().substring(0, valid.length() -2));
                System.out.println(error);
                continue;
            }
        }

    }
}
