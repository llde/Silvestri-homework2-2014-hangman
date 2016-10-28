package hw2.game.util;

import hw2.test.PartialGrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** <b>IMPLEMENTARE I METODI SECONDO LE SPECIFICHE DATE NEI JAVADOC.</b>
 * <br>
 * Classe con metodi di utilità per implementare giochi.*/
public class Utils {
    /**
     * Ritorna true se la stringa s non contiene caratteri che non sono alfabetici
     * minuscoli, cioè i caratteri abcdefghijklmnopqrstuvwxyz. Se la stringa è vuota,
     * ritorna true.
     *
     * @param s una stringa
     * @return true se s non contiene caratteri che non sono alfabetici minuscoli
     */
    private static final char[] caratterialfabeticiminuscoli = "abcdefghijklmnopqrstuvwxyz".toCharArray();   //Oblivion Rules!!!

    public static boolean isAlphaLowercase(String s) {
        if(s.length() <= 0) return true;
        for (char ch : s.toCharArray()) {
            if (Arrays.binarySearch(caratterialfabeticiminuscoli, ch) < 0) return false;
        }
        return true;
    }

    /**
     * Ritorna la lista delle linee del file di percorso p che non soddisfano il
     * predicato filter. Ad esempio, se filter è true se la linea ha lunghezza
     * minore di 5, allora le linee ritornate sono solamente quelle di lunghezza
     * maggiore od uguale a 5.
     *
     * @param p      percorso di un file (di testo)
     * @param filter filtro per le linee non volute
     * @return lista delle linee del file filtrate
     * @throws java.io.IOException se si verifica un errore leggendo il file
     */
    public static List<String> lines(Path p, Charset cs, Predicate<String> filter)
            throws IOException {
        //     throw new UnsupportedOperationException();
        BufferedReader x = Files.newBufferedReader(p, cs);
        Stream<String> lines = x.lines();
        lines = lines.filter(filter.negate());
        List<String> Federation = lines.collect(Collectors.toList());
        return Collections.unmodifiableList(Federation);

    }

    /**
     * ATTENZIONE: questo metodo non deve essere implementato e deve essere usato
     * nei giochi in cui si deve scegliere una stringa random, come ad esempio
     * nel gioco Hangman.
     * <p>
     * Ritorna una stringa scelta in modo random uniforme dalle stringhe in coll.
     *
     * @param coll una collezione di stringhe
     * @return una stringa random scelta da coll
     */
    public static String choose(Collection<String> coll) {
        return PartialGrade.choose(coll);
    }

    /**
     * Ritorna l'array ordinato in senso crescente e senza ripetizioni delle
     * lunghezze delle stringhe in coll.
     *
     * @param coll collezione di stringhe
     * @return array ordinato e senza ripetizioni delle lunghezze delle
     * stringhe in coll.
     */
    public static Integer[] lengths(Collection<String> coll) {
        Integer[] lunghezze = new Integer[0];
        for (String str : coll) {
            if (Arrays.binarySearch(lunghezze, str.length()) >= 0) continue;
            lunghezze = Arrays.copyOf(lunghezze, lunghezze.length + 1);
            lunghezze[lunghezze.length - 1] = str.length();
            Arrays.sort(lunghezze);
        }
        Arrays.sort(lunghezze);
        return lunghezze;
    }

    /**
     * Ritorna l'insieme di stringhe in coll che hanno lunghezza len.
     *
     * @param coll una collezione di stringhe
     * @param len  la lunghezza voluta
     * @return l'insieme di stringhe in coll che hanno lunghezza len
     */
    public static Set<String> stringsOfLength(Collection<String> coll, int len) {
        Set<String> Doctor = coll.stream().filter(str -> (str.length()) == len).collect(Collectors.toSet());
        return Doctor;
    }

    /**
     * Ritorna la lista di insiemi che partizionano le stringhe in coll secondo
     * la relazione di equivalenza equiv. Più precisamente gli insiemi ritornati
     * sono disgiunti, la loro unione è uguale all'insieme di stringhe in coll e
     * due stringhe x e y appartengono allo stesso insieme se e solo se equiv
     * applicato a x e y ritorna true. Ad esempio, supponendo che equiv applicata
     * a x, y sia true se e solo se le stringhe x e y hanno la lettera 'a' nelle
     * stesse posizioni,
     * <pre>
     *     coll = ["pizza","torre","porta","pazza","perle","torte"]
     *     partition(coll, equiv)  ritorna
     *     [["perle","torte","torre"],["pizza","porta"],["pazza"]]
     * </pre>
     * Si assume che equiv sia una relazione di equivalenza sull'insieme di
     * stringhe in coll. Cioè equiv deve essere riflessiva, simmetrica e
     * transitiva.
     *
     * @param coll  una collezione di stringhe
     * @param equiv una relazione di equivalenza su coll
     * @return la lista degli insiemi che partizionano coll rispetto a equiv
     */
    //Potrebbe sfanculare di brutto con predicati più complessi.
    public static List<Set<String>> partition(Collection<String> coll,
                                              BiPredicate<String, String> equiv) {
        List<Set<String>> partitions = new ArrayList<>();
        for(String str: coll) {
            Predicate<String> pred = (s1) -> equiv.test(str,s1);
            Predicate<String> pred1 = pred.negate();
            Set<String> Cardassians = coll.stream().filter(pred).collect(Collectors.toSet());
            coll = coll.stream().filter(pred1).collect(Collectors.toList());
            if(Cardassians.isEmpty()) {
                if(coll.isEmpty()||coll.size() < 1) break;
                else continue;
            }
             partitions.add(Cardassians);
             if(coll.isEmpty()||coll.size() < 1) break;
        }
       /* for(String el : coll) {
            int exec = 1;
            if (partitions.isEmpty()) {
                Set<String> tr = new HashSet<>();
                tr.add(el);
                partitions.add(tr);
                continue;
            }
            for (Set<String> t : partitions) {
                for (String str : t) {
                    if (equiv.test(str, el)) {
                        t.add(el);
                        exec = 0;
                        break;
                    }
                    break;
                }
            }
            if (exec == 0) {
                continue;
            }
            Set<String> NewSet = new HashSet<>();
            NewSet.add(el);
            partitions.add(NewSet);
        } */
        return partitions;
    }
}
