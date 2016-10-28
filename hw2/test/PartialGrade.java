package hw2.test;

import hw2.game.Action;
import hw2.game.Param;
import hw2.game.WGState;
import hw2.game.WGame;

import hw2.game.ui.TextUIWGames;
import hw2.game.util.SimpleParam;
import hw2.game.util.SimpleWGState;
import hw2.game.util.Utils;
import hw2.games.EvilHangman;
import hw2.games.Hangman;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiPredicate;

/** <B>QUESTA È LA VERSIONE COMPLETA</B>
 * <br>
 *  Per calcolare il punteggio parziale dell'homework eseguire il main di questa
 * classe. Il punteggio è limitato ai primi 25 punti, i rimanenti 15 saranno
 * assegnati da un'altro grade eseguito dopo la consegna dal docente.
 * <br>
 * ATTENZIONE: I file di testo per il grade (e il test) devono essere in una
 * directory di nome files contenuta nella working directory.
 * <br>
 * <b>ATTENZIONE: NON MODIFICARE IN ALCUN MODO QUESTA CLASSE.</b> */
public class PartialGrade {
    public static void main(String[] args) {
        test();
    }







    private static void test() {
        System.setOut(new PrintStream(OUTPUT));
        System.setIn(INPUT);
        OUTPUT.setThread(Thread.currentThread());
        OUTPUT.standardOutput(true);
        INPUT.setThread(Thread.currentThread());
        INPUT.standardInput(true);
        testing = true;
        testWord = null;
        totalScore = 0;
        boolean ok = test_isAlphaLowercase(0.5f, 2000);
        if (ok) ok = test_lines(1f, 8000);
        if (ok) ok = test_lengths(1f, 3000);
        if (ok) ok = test_stringsOfLength(0.5f, 5000);
        if (ok) ok = test_partition(2f, 15000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H1, TW_H1, 3, 15000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H2, TW_H2, 3, 15000);
        if (ok) ok = test_ui_H("parole2.txt", SESSION_H3, TW_H3, 3, 15000);
        if (ok) ok = test_ui_E("parole.txt", SESSION_E1, 3, 15000);
        if (ok) ok = test_ui_E("parole2.txt", SESSION_E2, 4, 15000);
        if (ok) ok = test_ui_HE("parole.txt", "parole2.txt", SESSION_HE, TW_H4, 4, 15000);
        System.out.println("Punteggio parziale: "+totalScore);
        if (ok) ok = test_lines_2(1f, 10000);
        if (ok) ok = test_lengths_2(1f, 10000);
        if (ok) ok = test_stringsOfLength_2(1f, 10000);
        if (ok) ok = test_partition_2(2f, 25000);
        if (ok) ok = test_ui_HE_2("parole3.txt", "parole3.txt", SESSION_HE2, TW_H5, 2, 20000);
        if (ok) ok = test_ui_HE_2("parole3.txt", "parole3.txt", SESSION_HE3, TW_H6, 3, 20000);
        if (ok) ok = test_ui_HED("parole3.txt", "parole3.txt", SESSION_HED, "", 5, 20000);
        System.out.println("Punteggio totale: "+totalScore);
        testing = false;
    }

    private static void checkFile(String fName) {
        Path p = Paths.get("files", fName);
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        try {
            String msg = "Check "+fName+"\n";
            List<String> lines = java.nio.file.Files.readAllLines(p);
            msg += "Number of lines: "+lines.size()+"\n";
            int countNoAlpha = 0, countEmpty = 0;
            for (String l : lines) {
                if (l.isEmpty()) countEmpty++;
                for (int i = 0 ; i < l.length() ; i++)
                    if (alpha.indexOf(l.charAt(i)) < 0) {
                        countNoAlpha++;
                        break;
                    }
            }
            msg += "Number empty lines: "+countEmpty+"  Number no alpha lines: "+countNoAlpha+"\n";
            System.out.println(msg);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private static String errOut(String corrOut, String outputs) {
        corrOut = corrOut.trim().toLowerCase();
        outputs = outputs.trim().toLowerCase();
        String[] ctt = corrOut.split("\\s+");
        String[] tt = outputs.split("\\s+");
        for (int i = 0 ; i < ctt.length && i < tt.length ; i++) {
            if (!Objects.equals(ctt[i], tt[i])) {
                String cs = String.join(" ", Arrays.copyOfRange(ctt, Math.max(0, i - 5), Math.min(ctt.length, i + 6)));
                String s = String.join(" ", Arrays.copyOfRange(tt, Math.max(0, i - 5), Math.min(tt.length, i + 6)));
                return "\""+cs+"\"" + "  !=  " + "\""+s+"\"";
            }
        }
        return "";
    }

    private static boolean test_lines_2(float sc, int ms) {
        return runTest("Test lines 2", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole3.txt");
                List<String> list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 4);
                if (list.size() != 1809) return new Result("ERRORE "+list.size() + " != " + 1809);
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() < 9);
                if (list.size() != 198949) return new Result("ERRORE "+list.size() + " != " + 198949);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_lengths_2(float sc, int ms) {
        return runTest("Test lengths 2", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole.txt");
                List<String> lines = java.nio.file.Files.readAllLines(p);
                Integer[] lens = Utils.lengths(lines);
                Integer[] cl = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
                if (!Arrays.equals(lens, cl))
                    return new Result("ERRORE "+Arrays.toString(cl)+" != "+Arrays.toString(Arrays.copyOf(lens, Math.min(lens.length, cl.length + 5))));
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_stringsOfLength_2(float sc, int ms) {
        return runTest("Test stringsOfLength 2", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole.txt");
                List<String> lines = java.nio.file.Files.readAllLines(p);
                Set<String> set = Utils.stringsOfLength(lines, 1);
                if (!(set != null && eqLen(set, 1) && included(set, S1) && set.size() == S1.size()))
                    return new Result("ERRORE "+S1+" != "+set);
                set = Utils.stringsOfLength(lines, 2);
                if (!(set != null && eqLen(set, 2) && included(set, S2) && set.size() == S2.size()))
                    return new Result("ERRORE "+S2+" != "+set);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_partition_2(float sc, int ms) {
        return runTest("Test partition 2", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole.txt");
                List<String> lines = java.nio.file.Files.readAllLines(p);
                lines.removeIf(s -> s.contains("a"));
                BiPredicate<String,String> pred = (s1,s2) -> {
                    if (s1.length() < 4 && s2.length() < 4) return s1.length() == s2.length();
                    else if (s1.length() < 4 || s2.length() < 4) return false;
                    else {
                        for (int i = 0 ; i < 4 ; i++)
                            if (s1.charAt(i) != s2.charAt(i))
                                return false;
                        return true;
                    }
                };
                List<Set<String>> list = Utils.partition(lines, pred);
                Set<String> set = new HashSet<>(lines);
                int n = 0;
                for (Set<String> s : list) {
                    if (!included(s, lines)) return new Result("ERRORE insieme non incluso nella collezione di input");
                    for (String s1 : s)
                        for (String s2 : s)
                            if (!pred.test(s1, s2)) return new Result("ERRORE insieme contiene stringhe non equivalenti");
                    n += s.size();
                }
                if (n != set.size()) return new Result("ERRORE cardinalità");
                for (String s : lines) {
                    boolean found = false;
                    for (Set<String> ss : list)
                        if (ss.contains(s)) found = true;
                    if (!found) return new Result("ERRORE stringa non presente in nessun insieme");
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result();
        });
    }

    private static boolean test_ui_HE_2(String fnameH, String fnameE, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman e EvilHangman", sc, ms, () -> {
            try {
                testWord = tw;
                INPUT.setContent(sessionToString(session));
                OUTPUT.getBuffer();
                TextUIWGames.start(new Hangman(Paths.get("files", fnameH), StandardCharsets.UTF_8, 7),
                        new EvilHangman(Paths.get("files", fnameE), StandardCharsets.UTF_8, 7));
                String outputs = OUTPUT.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs)) {
                    return new Result("ERRORE output: " + errOut(corrOut, outputs));
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_HED(String fnameH, String fnameE, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman, EvilHangman e DummyGames", sc, ms, () -> {
            try {
                testWord = tw;
                INPUT.setContent(sessionToString(session));
                OUTPUT.getBuffer();
                TextUIWGames.start(new Hangman(Paths.get("files", fnameH), StandardCharsets.UTF_8, 7),
                        new EvilHangman(Paths.get("files", fnameE), StandardCharsets.UTF_8, 7),
                        new DummyGame(), new DummyGame2());
                String outputs = OUTPUT.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs)) {
                    return new Result("ERRORE output: " + errOut(corrOut, outputs));
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static String TW_H5 = "inchiostratore", TW_H6 = "razziatori";
    private static String[][] SESSION_HE2 = {
            {"1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (7)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Massimo numero di errori:","4"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (4)\n" +
                    "2. Lunghezza parola (7)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Lunghezza parola:","11"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (4)\n" +
                    "2. Lunghezza parola (11)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","r"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e, i, r\n" +
                    "Indovina:","a"},
            {"Parola: a _ _ _ _ _ a _ a _ _\n" +
                    "Mancanti: e, i, r\n" +
                    "Indovina:","o"},
            {"Parola: a _ _ o _ _ a _ a _ o\n" +
                    "Mancanti: e, i, r\n" +
                    "Indovina:","u"},
            {"Parola: a _ _ o _ _ a _ a _ o\n" +
                    "Mancanti: e, i, r, u (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Non hai indovinato, la parola era: abboccavamo\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","m"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","r"},
            {"Parola: _ _ _ _ _ _ _ _ r _ _ _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _ _ _ _ r a _ _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","t"},
            {"Parola: _ _ _ _ _ _ _ t r a t _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","s"},
            {"Parola: _ _ _ _ _ _ s t r a t _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","c"},
            {"Parola: _ _ c _ _ _ s t r a t _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","n"},
            {"Parola: _ n c _ _ _ s t r a t _ r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","o"},
            {"Parola: _ n c _ _ o s t r a t o r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","i"},
            {"Parola: i n c _ i o s t r a t o r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","h"},
            {"Parola: i n c h i o s t r a t o r _\n" +
                    "Mancanti: m\n" +
                    "Indovina:","e"},
            {"HAI INDOVINATO: inchiostratore\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Fine",""}};

    private static String[][] SESSION_HE3 = {
            {"1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:", "a"},
            {"Errore, input ammessi: [1, 2, 3]\n" +
                    "Digita un intero da 1 a 3:", "1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:", "dd"},
            {"Errore, input ammessi: [1, 2]\n" +
                    "Digita un intero da 1 a 2:", "1"},
            {"Massimo numero di errori:", "b"},
            {"Errore, input ammessi: [1, 2, 3, 4, 5, 6, 7]\n" +
                    "Massimo numero di errori:", "3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:", "2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:", "boh"},
            {"Non hai indovinato, la parola era: razziatori\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:", "2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (7)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:", "8"},
            {"Errore, input ammessi: [1, 2, 3]\n" +
                    "Digita un intero da 1 a 3:", "1"},
            {"Massimo numero di errori:", "g"},
            {"Errore, input ammessi: [1, 2, 3, 4, 5, 6, 7]\n" +
                    "Massimo numero di errori:", "3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (7)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:", "2"},
            {"Lunghezza parola:", "30"},
            {"Errore, input ammessi: [7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26]\n" +
                    "Lunghezza parola:", "20"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (20)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:", "3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:", "a"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:", "u"},
            {"Parola: _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a, u\n" +
                    "Indovina:", "$stop"},
            {"Gioco interrotto\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:", "3"},
            {"Fine", ""}
    };
    private static String[][] SESSION_HED = {
            {"1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. DUMMYGAME\n" +
                    "4. DUMMYGAME2\n" +
                    "5. Quit\n" +
                    "Digita un intero da 1 a 5:", "3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "Inizio", "gdhghg ddd"},
            {"Sbagliato, riprova", "dd"},
            {"Sbagliato, riprova", "ss"},
            {"Hai perso!\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. DUMMYGAME\n" +
                    "4. DUMMYGAME2\n" +
                    "5. Quit\n" +
                    "Digita un intero da 1 a 5:", "4"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v2)\n" +
                    "2. parametro 2 (1.5)\n" +
                    "3. parametro 3 (val 2)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "3"},
            {"parametro 3:", "val 1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v2)\n" +
                    "2. parametro 2 (1.5)\n" +
                    "3. parametro 3 (val 1)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "4"},
            {"Per interrompere il gioco digita $stop\n" +
                    "Prova a indovinare", "gg hh"},
            {"Sbagliato, riprova ancora", "s b ee"},
            {"Incredibile! Hai vinto!\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. DUMMYGAME\n" +
                    "4. DUMMYGAME2\n" +
                    "5. Quit\n" +
                    "Digita un intero da 1 a 5:", "2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (7)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:", "3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:", "a"},
            {"Parola: _ _ _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:", "aa"},
            {"Non hai indovinato, la parola era: beccute\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. DUMMYGAME\n" +
                    "4. DUMMYGAME2\n" +
                    "5. Quit\n" +
                    "Digita un intero da 1 a 5:", "4"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v2)\n" +
                    "2. parametro 2 (1.5)\n" +
                    "3. parametro 3 (val 1)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "2"},
            {"parametro 2:", "0"},
            {"Errore, input ammessi: [1.5, 2.33, 0.67]\n" +
                    "parametro 2:", "0.67"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v2)\n" +
                    "2. parametro 2 (0.67)\n" +
                    "3. parametro 3 (val 1)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "1"},
            {"parametro 1:", "f"},
            {"Errore, input ammessi: [v1, v2, v3, v4]\n" +
                    "parametro 1:", "v3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v3)\n" +
                    "2. parametro 2 (0.67)\n" +
                    "3. parametro 3 (val 1)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "3"},
            {"parametro 3:", "val3"},
            {"Errore, input ammessi: [val 1, val 2, val 3]\n" +
                    "parametro 3:", "val 2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. parametro 1 (v3)\n" +
                    "2. parametro 2 (0.67)\n" +
                    "3. parametro 3 (val 2)\n" +
                    "4. Inizia a giocare\n" +
                    "Digita un intero da 1 a 4:", "4"},
            {"Per interrompere il gioco digita $stop\n" +
                    "Prova a indovinare", "a"},
            {"Sbagliato, riprova ancora", "b"},
            {"Sbagliato, riprova ancora", "gg"},
            {"Hai perso! Era troppo difficile\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. DUMMYGAME\n" +
                    "4. DUMMYGAME2\n" +
                    "5. Quit\n" +
                    "Digita un intero da 1 a 5:", "5"},
            {"Fine", ""}
    };



    private static class DummyGame implements WGame {
        @Override
        public String name() { return getClass().getSimpleName().toUpperCase(); }
        @Override
        public String info() { return "Indovina?"; }
        @Override
        public List<Param<?>> params() { return params; }
        @Override
        public WGState newGame() {
            if (active) throw new IllegalStateException();
            active = true;
            count = 3;
            return new SimpleWGState(Action.CONTINUE, "Inizio");
        }
        @Override
        public WGState player(String s) {
            if (!active) throw new IllegalStateException();
            if (s.equals("boh")) {
                active = false;
                return new SimpleWGState(Action.END, "Hai vinto!");
            }
            count--;
            if (count == 0) {
                active = false;
                return new SimpleWGState(Action.END, "Hai perso!");
            }
            return new SimpleWGState(Action.CONTINUE, "Sbagliato, riprova");
        }
        @Override
        public void abort() { active = false; }

        private List<Param<?>> params = Collections.unmodifiableList(new ArrayList<>());
        private boolean active = false;
        private int count;
    }

    private static class DummyGame2 implements WGame {
        @Override
        public String name() { return getClass().getSimpleName().toUpperCase(); }
        @Override
        public String info() { return "Indovina??"; }
        @Override
        public List<Param<?>> params() { return params; }
        @Override
        public WGState newGame() {
            if (active) throw new IllegalStateException();
            active = true;
            count = 3;
            return new SimpleWGState(Action.CONTINUE, "Prova a indovinare");
        }
        @Override
        public WGState player(String s) {
            if (!active) throw new IllegalStateException();
            String[] tt = s.split(" ");
            if (tt.length == 3) {
                active = false;
                return new SimpleWGState(Action.END, "Incredibile! Hai vinto!");
            }
            count--;
            if (count == 0) {
                active = false;
                return new SimpleWGState(Action.END, "Hai perso! Era troppo difficile");
            }
            return new SimpleWGState(Action.CONTINUE, "Sbagliato, riprova ancora");
        }
        @Override
        public void abort() { active = false; }

        private Param<String> p1 = new SimpleParam<>("parametro 1", 1, "v1","v2","v3","v4");
        private Param<Double> p2 = new SimpleParam<>("parametro 2", 0, 1.5,2.33,0.67);
        private Param<String> p3 = new SimpleParam<>("parametro 3", 1, "val 1","val 2","val 3");
        private List<Param<?>> params = Collections.unmodifiableList(Arrays.asList(p1,p2,p3));
        private boolean active = false;
        private int count;
    }





    public static String choose(Collection<String> coll) {
        if (testing && testWord != null) {
            return testWord;
        } else {
            List<String> list = new ArrayList<>(coll);
            return list.get(RND.nextInt(list.size()));
        }
    }

    /*
    private static void test() {
        testing = true;
        testWord = null;
        totalScore = 0;
        boolean ok = test_isAlphaLowercase(0.5f, 2000);
        if (ok) ok = test_lines(1f, 8000);
        if (ok) ok = test_lengths(1f, 3000);
        if (ok) ok = test_stringsOfLength(0.5f, 5000);
        if (ok) ok = test_partition(2f, 15000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H1, TW_H1, 3, 15000);
        if (ok) ok = test_ui_H("parole.txt", SESSION_H2, TW_H2, 3, 15000);
        if (ok) ok = test_ui_H("parole2.txt", SESSION_H3, TW_H3, 3, 15000);
        if (ok) ok = test_ui_E("parole.txt", SESSION_E1, 3, 15000);
        if (ok) ok = test_ui_E("parole2.txt", SESSION_E2, 4, 15000);
        if (ok) test_ui_HE("parole.txt", "parole2.txt", SESSION_HE, TW_H4, 4, 15000);
        System.out.println("Punteggio totale: "+totalScore);
        testing = false;
    }
    */

    private static boolean test_isAlphaLowercase(float sc, int ms) {
        return runTest("Test isAlphaLowercase", sc, ms, () -> {
            try {
                for (String[] p : ALPHA)
                    if (Utils.isAlphaLowercase(p[0]) != p[1].isEmpty())
                        return new Result("ERRORE "+p[0]);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_lines(float sc, int ms) {
        return runTest("Test lines", sc, ms, () -> {
            try {
                Path p = Paths.get("files", "parole.txt");
                List<String> list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 5);
                if (list.size() != 3914) return new Result("ERRORE "+list.size() + " != " + 3914);
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 10);
                if (list.size() != 59571) return new Result("ERRORE "+list.size() + " != " + 59571);
                p = Paths.get("files", "parole2.txt");
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 6);
                if (list.size() != 21348) return new Result("ERRORE "+list.size() + " != " + 21348);
                list = Utils.lines(p, StandardCharsets.UTF_8, s -> s.length() > 11);
                if (list.size() != 188273) return new Result("ERRORE "+list.size() + " != " + 188273);
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_choose(float sc, int ms) {
        return runTest("Test choose", sc, ms, () -> {
            try {
                Map<String,Integer> map = new HashMap<>();
                STRLIST.forEach(s -> map.put(s, 0));
                int n = 100*STRLIST.size();
                for (int i = 0 ; i < n ; i++) {
                    String r = Utils.choose(STRLIST);
                    if (!map.containsKey(r))
                        return new Result("ERRORE stringa ritornata non appartiene alla collezione");
                    map.merge(r, 1, Integer::sum);
                }
                for (String k : map.keySet())
                    if (map.get(k) < 50)
                        return new Result("ERRORE scelta non uniformemente random");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_lengths(float sc, int ms) {
        return runTest("Test lengths", sc, ms, () -> {
            try {
                Integer[] lens = Utils.lengths(STRLIST);
                Integer[] cl = {1,2,3,5,9};
                if (!Arrays.equals(lens, cl))
                    return new Result("ERRORE");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_stringsOfLength(float sc, int ms) {
        return runTest("Test stringsOfLength", sc, ms, () -> {
            try {
                Set<String> set = Utils.stringsOfLength(STRLIST, 1);
                if (!(set != null && eqLen(set, 1) && included(set, STRLIST) && set.size() == 11))
                    return new Result("ERRORE");
                set = Utils.stringsOfLength(STRLIST, 2);
                if (!(set != null && eqLen(set, 2) && included(set, STRLIST) && set.size() == 5))
                    return new Result("ERRORE");
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_partition(float sc, int ms) {
        return runTest("Test partition", sc, ms, () -> {
            try {
                BiPredicate<String,String> pred = (s1,s2) -> s1.charAt(0) == s2.charAt(0);
                List<Set<String>> list = Utils.partition(STRLIST, pred);
                int n = 0;
                for (Set<String> s : list) {
                    if (!included(s, STRLIST)) return new Result("ERRORE");
                    for (String s1 : s)
                        for (String s2 : s)
                            if (!pred.test(s1, s2)) return new Result("ERRORE");
                    n += s.size();
                }
                if (n != STRLIST.size()) return new Result("ERRORE");
                for (String s : STRLIST) {
                    boolean found = false;
                    for (Set<String> ss : list)
                        if (ss.contains(s)) found = true;
                    if (!found) return new Result("ERRORE");
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_H(String fname, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman", sc, ms, () -> {
            try {
                testWord = tw;
                INPUT.setContent(sessionToString(session));
                OUTPUT.getBuffer();
                TextUIWGames.start(new Hangman(Paths.get("files", fname), StandardCharsets.UTF_8, 5));
                String outputs = OUTPUT.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs)) {
                    return new Result("ERRORE output: " + errOut(corrOut, outputs));
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_HE(String fnameH, String fnameE, String[][] session, String tw, float sc, int ms) {
        return runTest("Test TextUIWGames con Hangman e EvilHangman", sc, ms, () -> {
            try {
                testWord = tw;
                INPUT.setContent(sessionToString(session));
                OUTPUT.getBuffer();
                TextUIWGames.start(new Hangman(Paths.get("files", fnameH), StandardCharsets.UTF_8, 5),
                        new EvilHangman(Paths.get("files", fnameE), StandardCharsets.UTF_8, 5));
                String outputs = OUTPUT.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs)) {
                    return new Result("ERRORE output: " + errOut(corrOut, outputs));
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }

    private static boolean test_ui_E(String fname, String[][] session, float sc, int ms) {
        return runTest("Test TextUIWGames con EvilHangman", sc, ms, () -> {
            try {
                INPUT.setContent(sessionToString(session));
                OUTPUT.getBuffer();
                TextUIWGames.start(new EvilHangman(Paths.get("files", fname), StandardCharsets.UTF_8, 5));
                String outputs = OUTPUT.getBuffer();
                String corrOut = "";
                for (String[] t : session)
                    corrOut += t[0]+"\n";
                if (!eqOut(corrOut, outputs)) {
                    return new Result("ERRORE output: " + errOut(corrOut, outputs));
                }
            } catch (Throwable t) { return handleThrowable(t); }
            return new Result(); });
    }



    private static String eqOutS(String out1, String out2) {
        out1 = out1.trim().toLowerCase();
        out2 = out2.trim().toLowerCase();
        String[] ow1 = out1.split("\\s+");
        String[] ow2 = out2.split("\\s+");
        if (ow1.length != ow2.length) return "Numero tokens: "+ow1.length+" != "+ow2.length;
        for (int i = 0 ; i < ow1.length ; i++)
            if (!Objects.equals(ow1[i], ow2[i]))
                return ow1[i]+" != "+ow2[i];
        return null;
    }


    private static boolean eqOut(String out1, String out2) {
        out1 = out1.trim().toLowerCase();
        out2 = out2.trim().toLowerCase();
        String[] ow1 = out1.split("\\s+");
        String[] ow2 = out2.split("\\s+");
        return Arrays.equals(ow1, ow2);
    }


    private static boolean eqLen(Collection<String> coll, int len) {
        for (String s : coll)
            if (s == null || s.length() != len)
                return false;
        return true;
    }

    private static boolean included(Collection<String> c1, Collection<String> c2) {
        for (String s : c1)
            if (!c2.contains(s))
                return false;
        return true;
    }

    private static final String[][] ALPHA = {{"abchkl",""},{"dfj",""},{"w",""},{"abdf5g","0"},
            {"sdfwgbdgbd6jk","0"},{"",""},{"a abd","0"},{"abe nm","0"}};
    private static final List<String> STRLIST = Arrays.asList("a","b","c","d","e","f","g",
            "h","aa","bb","cc","dd","m","n","o","abs","abcdf","ab","abcdfsfsf","bbb");
    private static final String TW_H1 = "malvisto", TW_H2 = "opineremo", TW_H3 = "tramestii",
    TW_H4 = "divagata";
    private static final String[][] SESSION_H1 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ a _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ a _ _ _ _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","i"},
            {"Parola: _ a _ _ i _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","o"},
            {"Parola: _ a _ _ i _ _ o\n" +
                    "Mancanti: e\n" +
                    "Indovina:","r"},
            {"Parola: _ a _ _ i _ _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","m"},
            {"Parola: m a _ _ i _ _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","s"},
            {"Parola: m a _ _ i s _ o\n" +
                    "Mancanti: e, r\n" +
                    "Indovina:","n"},
            {"Parola: m a _ _ i s _ o\n" +
                    "Mancanti: e, r, n\n" +
                    "Indovina:","malvisto"},
            {"HAI INDOVINATO: malvisto\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static final String[][] SESSION_H2 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ e _ e _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ e _ e _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","o"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a\n" +
                    "Indovina:","a"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a\n" +
                    "Indovina:","t"},
            {"Parola: o _ _ _ e _ e _ o\n" +
                    "Mancanti: a, t\n" +
                    "Indovina:","r"},
            {"Parola: o _ _ _ e r e _ o\n" +
                    "Mancanti: a, t\n" +
                    "Indovina:","s"},
            {"Parola: o _ _ _ e r e _ o\n" +
                    "Mancanti: a, t, s (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Parola: o _ _ _ e r e m o\n" +
                    "Mancanti: a, t, s (Hai esaurito il numero di errori)\n" +
                    "Indovina:","c"},
            {"Non hai indovinato, la parola era: opineremo\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static final String[][] SESSION_H3 = {{"1. L'IMPICCATO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (2)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ e _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ a _ e _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","i"},
            {"Parola: _ _ a _ e _ _ i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","t"},
            {"Parola: t _ a _ e _ t i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","r"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: \n" +
                    "Indovina:","n"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: n\n" +
                    "Indovina:","c"},
            {"Parola: t r a _ e _ t i i\n" +
                    "Mancanti: n, c (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Parola: t r a m e _ t i i\n" +
                    "Mancanti: n, c (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"HAI INDOVINATO: tramestii\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""},};
    private static final String[][] SESSION_E1 = {{"1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:", "1"},{"Modifica i parametri di gioco o inizia a giocare:\n" +
            "1. Massimo numero di errori (6)\n" +
            "2. Lunghezza parola (5)\n" +
            "3. Inizia a giocare\n" +
            "Digita un intero da 1 a 3:","3"},{"Per interrompere il gioco digita $stop\n" +
            "L'IMPICCATO DIABOLICO\n" +
            "Trova la parola indovinando una lettera alla volta\n" +
            "Se credi di aver indovinato immetti l'intera parola\n" +
            "Parola: _ _ _ _ _\n" +
            "Mancanti: \n" +
            "Indovina:","a"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a\n" +
            "Indovina:","e"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a, e\n" +
            "Indovina:","i"},{"Parola: _ _ _ _ _\n" +
            "Mancanti: a, e, i\n" +
            "Indovina:","o"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i\n" +
            "Indovina:","t"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t\n" +
            "Indovina:","r"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t, r\n" +
            "Indovina:","s"},{"Parola: _ o _ _ o\n" +
            "Mancanti: a, e, i, t, r, s (Hai esaurito il numero di errori)\n" +
            "Indovina:","c"},{"Non hai indovinato, la parola era: bollo\n" +
            "\n" +
            "1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","2"},{"Fine",""}};
    private static String[][] SESSION_E2 = {{"1. L'IMPICCATO DIABOLICO\n" +
            "2. Quit\n" +
            "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Lunghezza parola:","8"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (8)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ i _ _ i\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","o"},
            {"Parola: _ o _ _ i _ _ i\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","r"},
            {"Parola: _ o _ _ i _ _ i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"Parola: _ o _ _ i s _ i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Parola: _ o _ _ i s t i\n" +
                    "Mancanti: a, e, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","m"},
            {"Non hai indovinato, la parola era: bobbisti\n" +
                    "\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (8)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Lunghezza parola:","9"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (9)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","o"},
            {"Parola: _ _ _ _ _ _ _ _ o\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","a"},
            {"Parola: _ _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i\n" +
                    "Indovina:","r"},
            {"Parola: _ _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","s"},
            {"Parola: s _ a _ _ a _ _ o\n" +
                    "Mancanti: e, i, r (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Non hai indovinato, la parola era: sbaffando\n" +
                    "\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:",""},
            {"Errore, input ammessi: [1, 2]\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (9)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","$stop"},
            {"Gioco interrotto\n" +
                    "1. L'IMPICCATO DIABOLICO\n" +
                    "2. Quit\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Fine",""}};
    private static String[][] SESSION_HE = {{"1. L'IMPICCATO\n" +
            "2. L'IMPICCATO DIABOLICO\n" +
            "3. Quit\n" +
            "Digita un intero da 1 a 3:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Massimo numero di errori:","3"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","e"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, e\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, e, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","o"},
            {"Parola: _ o _ _ o\n" +
                    "Mancanti: a, e, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","t"},
            {"Non hai indovinato, la parola era: bollo\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","1"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (6)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","1"},
            {"Massimo numero di errori:","4"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (4)\n" +
                    "2. Inizia a giocare\n" +
                    "Digita un intero da 1 a 2:","2"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ a _ a _ a\n" +
                    "Mancanti: \n" +
                    "Indovina:","r"},
            {"Parola: _ _ _ a _ a _ a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","t"},
            {"Parola: _ _ _ a _ a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","g"},
            {"Parola: _ _ _ a g a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","d"},
            {"Parola: d _ _ a g a t a\n" +
                    "Mancanti: r\n" +
                    "Indovina:","divagata"},
            {"HAI INDOVINATO: divagata\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","2"},
            {"Modifica i parametri di gioco o inizia a giocare:\n" +
                    "1. Massimo numero di errori (3)\n" +
                    "2. Lunghezza parola (5)\n" +
                    "3. Inizia a giocare\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Per interrompere il gioco digita $stop\n" +
                    "L'IMPICCATO DIABOLICO\n" +
                    "Trova la parola indovinando una lettera alla volta\n" +
                    "Se credi di aver indovinato immetti l'intera parola\n" +
                    "Parola: _ _ _ _ _\n" +
                    "Mancanti: \n" +
                    "Indovina:","a"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a\n" +
                    "Indovina:","u"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, u\n" +
                    "Indovina:","i"},
            {"Parola: _ _ _ _ _\n" +
                    "Mancanti: a, u, i (Hai esaurito il numero di errori)\n" +
                    "Indovina:","o"},
            {"Non hai indovinato, la parola era: beffe\n" +
                    "\n" +
                    "1. L'IMPICCATO\n" +
                    "2. L'IMPICCATO DIABOLICO\n" +
                    "3. Quit\n" +
                    "Digita un intero da 1 a 3:","3"},
            {"Fine",""}};


    private static void print(Result r) {
        System.out.println(!r.fatal && r.err == null ? "OK" : r.err);
    }
    private static void print(String m) { System.out.print(m); }
    private static void println(String m) { print(m+"\n"); }

    private static Result handleThrowable(Throwable t) {
        String msg = "";
        boolean fatal = false;
        if (t instanceof Exception)
            msg += "Eccezione inattesa: ";
        else {
            msg += "ERRORE GRAVE, impossibile continuare il test: ";
            fatal = true;
        }
        t.printStackTrace();
        return new Result(msg+t + "  \n", fatal);
    }

    private static boolean runTest(String msg, float score, int ms, Callable<Result> test) {
        FutureTask<Result> future = new FutureTask<>(test);
        Thread t = new Thread(future);
        t.setDaemon(true);
        print(msg+" ");
        OUTPUT.standardOutput(false);
        INPUT.standardInput(false);
        OUTPUT.setThread(t);
        INPUT.setThread(t);
        t.start();
        Result res = null;
        try {
            res = future.get(ms, TimeUnit.MILLISECONDS);
        } catch (CancellationException | InterruptedException | TimeoutException | ExecutionException e) {}
        future.cancel(true);
        OUTPUT.setThread(Thread.currentThread());
        INPUT.setThread(Thread.currentThread());
        OUTPUT.standardOutput(true);
        INPUT.standardInput(true);
        if (res == null)
            println("ERRORE: limite di tempo superato ("+ms+" ms)");
        else if (res.fatal) {
            println(res.err);
            return false;
        } else if (res.err != null) {
            println(res.err);
        } else {
            println(" score "+score);
            totalScore += score;
        }
        return true;
    }

    private static class Result {
        public final String err;
        public final boolean fatal;

        public Result(String e, boolean f) {
            err = e;
            fatal = f;
        }

        public Result() { this(null, false); }
        public Result(String e) { this(e, false); }
    }

    private static class Locker {
        public Locker() {
            lock = new ReentrantLock(true);
        }

        public synchronized boolean acquireLock() {
            try {
                if (Thread.currentThread().getId() == MAIN_THREAD.getId()) {
                    while (!lock.tryLock()) ;
                } else
                    lock.lockInterruptibly();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return false; }
            return true;
        }

        public synchronized void releaseLock() { lock.unlock(); }


        private final ReentrantLock lock;
    }

    private static class Output extends OutputStream {
        public void standardOutput(boolean std) {
            if (!lock.acquireLock()) return;
            standardOut = std;
            lock.releaseLock();
        }
        @Override
        public void write(int b) throws IOException {
            if (!lock.acquireLock()) return;
            try {
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    if (standardOut) {
                        STD_OUT.write(b);
                    } else
                        buffer.offer(b);
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
        }

        public String getBuffer() {
            if (!lock.acquireLock()) return null;
            String s = "";
            try {
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    int size = buffer.size();
                    if (size > 0) {
                        Integer[] aux = buffer.toArray(new Integer[size]);
                        int[] cps = new int[size];
                        for (int i = 0; i < cps.length; i++) cps[i] = aux[i];
                        s = new String(cps, 0, cps.length);
                        buffer.clear();
                    }
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
            return s;
        }

        public void setThread(Thread t) {
            if (!lock.acquireLock()) return;
            currThread = t;
            lock.releaseLock();
        }

        private final Locker lock = new Locker();
        private final Queue<Integer> buffer = new ConcurrentLinkedDeque<>();
        private volatile Thread currThread = null;
        private volatile boolean standardOut = false;
    }

    private static class Input extends InputStream {
        public void standardInput(boolean std) {
            if (!lock.acquireLock()) return;
            standardIn = std;
            lock.releaseLock();
        }

        public void setContent(String s) {
            if (!lock.acquireLock()) return;
            try {
                content = new ByteArrayInputStream(s.getBytes());
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
        }

        @Override
        public int read() throws IOException {
            int b = -1;
            try {
                if (!lock.acquireLock()) return b;
                if (currThread != null && Thread.currentThread().getId() == currThread.getId()) {
                    if (standardIn) {
                        b = STD_IN.read();
                    } else
                        b = content.read();
                }
            } catch (Throwable e) {}
            finally { lock.releaseLock(); }
            return b;
        }

        public void setThread(Thread t) {
            if (!lock.acquireLock()) return;
            currThread = t;
            lock.releaseLock();
        }

        private final Locker lock = new Locker();
        private volatile Thread currThread = null;
        private volatile ByteArrayInputStream content = null;
        private volatile boolean standardIn = false;
    }

    private static String sessionToString(String[][] session) {
        String s = "";
        for (String[] t : session)
            s += t[1]+"\n";
        return s;
    }


    private static final PrintStream STD_OUT = System.out;
    private static final InputStream STD_IN = System.in;
    private static final Output OUTPUT = new Output();
    private static final Input INPUT = new Input();
    private static final Thread MAIN_THREAD = Thread.currentThread();
    private static double totalScore;
    private static volatile boolean testing = false;
    private static volatile String testWord = null;
    private static final Random RND = new Random();

    private static final Set<String> S1 = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
    private static final Set<String> S2 = new HashSet<>(Arrays.asList("ad", "ai", "al", "ce", "e'", "ci",
            "da", "di", "do", "ed", "fa", "fu", "ha", "ho", "il", "in", "io", "la", "le", "li", "lo", "ma",
            "me", "mi", "ne", "no", "os", "pi", "pu", "re", "sa", "se", "si", "so", "st", "su", "te", "ti",
            "to", "tu", "ud", "un", "va", "vi"));
}
